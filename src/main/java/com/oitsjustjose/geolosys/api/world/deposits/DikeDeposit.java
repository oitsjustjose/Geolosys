package com.oitsjustjose.geolosys.api.world.deposits;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.AbstractDeposit;
import com.oitsjustjose.geolosys.api.world.DepositUtils;
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.world.IChunkGennedCapability;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.data.serializer.SerializerUtils;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.SampleUtils;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class DikeDeposit extends AbstractDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_dike";

    private final int yMin;
    private final int yMax;
    private final int baseRadius;

    public DikeDeposit(HashMap<String, HashMap<BlockState, Float>> matcherToOreWeightPair, HashMap<BlockState, Float> sampleWeightPair, int yMin, int yMax, int baseRadius, int generationWeight, TagKey<Biome> biomeKey, HashSet<BlockState> matchers) {
        super(matcherToOreWeightPair, sampleWeightPair, biomeKey, matchers, generationWeight);
        this.yMin = yMin;
        this.yMax = yMax;
        this.baseRadius = baseRadius;
        validate(matcherToOreWeightPair, sampleWeightPair);
    }

    @Override
    public String toString() {
        return "Dike deposit with Blocks=" + this.getAllOres() + ", Samples=" + Arrays.toString(this.sampleWeightPair.keySet().toArray()) + ", Y Range=[" + this.yMin + "," + this.yMax + "], Radius of Base=" + this.baseRadius;
    }

    @Override
    public int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated) {
        /* Dimension checking is done in PlutonRegistry#pick */
        /* Check biome allowance */
        if (!this.canPlaceInBiome(level.getBiome(pos))) {
            return 0;
        }

        ChunkPos thisChunk = new ChunkPos(pos);
        int height = Math.abs((this.yMax - this.yMin));
        int x = thisChunk.getMinBlockX() + level.getRandom().nextInt(16);
        int z = thisChunk.getMinBlockZ() + level.getRandom().nextInt(16);

        int max = Utils.getTopSolidBlock(level, pos).getY() - 1;

        int yStart = this.yMin + level.getRandom().nextInt(height / 4);
        yStart = yStart > max ? yMin : yStart;

        int yEnd = this.yMax - level.getRandom().nextInt(height / 4);
        yEnd = Math.min(yEnd, max);

        BlockPos basePos = new BlockPos(x, yStart, z);

        int totlPlaced = 0;
        int htRnd = Math.abs((yEnd - yStart));
        int rad = this.baseRadius / 2;
        boolean shouldSub = false;

        for (int dY = yStart; dY <= yEnd; dY++) {
            for (int dX = -rad; dX <= rad; dX++) {
                for (int dZ = -rad; dZ <= rad; dZ++) {
                    float dist = (dX * dX) + (dZ * dZ);
                    if (dist > rad) {
                        continue;
                    }

                    BlockPos placePos = new BlockPos(basePos.getX() + dX, dY, basePos.getZ() + dZ);
                    BlockState current = level.getBlockState(placePos);
                    BlockState tmp = this.getOre(current, level.getRandom());
                    if (tmp == null) {
                        continue;
                    }

                    // Skip this block if it can't replace the target block or doesn't have a
                    // manually-configured replacer in the blocks object
                    if (!(this.getMatchers().contains(current) || this.matcherToOreWeightPair.containsKey(Utils.getRegistryName(current)))) {
                        continue;
                    }

                    if (FeatureUtils.enqueueBlockPlacement(level, new ChunkPos(pos), placePos, tmp, deposits, chunksGenerated)) {
                        totlPlaced++;
                    }
                }
            }

            // flip at around the halfway point.
            if (yStart + (htRnd / 2) <= dY) {
                shouldSub = true;
            }
            if (level.getRandom().nextInt(3) == 0) {
                rad += shouldSub ? -1 : 1;
                if (rad <= 0) {
                    return totlPlaced;
                }
            }
        }

        return totlPlaced;
    }

    @Override
    public void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated) {
        // Debug the pluton
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.info("Generated {} in Chunk {} (Pos [{} {} {}])", this.toString(), new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }

        ChunkPos thisChunk = new ChunkPos(pos);
        int maxSampleCnt = Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(), (this.baseRadius / CommonConfig.MAX_SAMPLES_PER_CHUNK.get()) + (this.baseRadius % CommonConfig.MAX_SAMPLES_PER_CHUNK.get()));
        maxSampleCnt = Math.max(maxSampleCnt, 1);
        for (int i = 0; i < maxSampleCnt; i++) {
            BlockState tmp = this.getSample(level.getRandom());
            if (tmp == null) {
                continue;
            }

            BlockPos samplePos = SampleUtils.getSamplePosition(level, new ChunkPos(pos));
            if (samplePos == null || SampleUtils.inNonWaterFluid(level, samplePos)) {
                continue;
            }

            if (SampleUtils.isInWater(level, samplePos) && tmp.hasProperty(BlockStateProperties.WATERLOGGED)) {
                tmp = tmp.setValue(BlockStateProperties.WATERLOGGED, Boolean.TRUE);
            }

            FeatureUtils.enqueueBlockPlacement(level, thisChunk, samplePos, tmp, deposits, chunksGenerated);
            FeatureUtils.fixSnowyBlock(level, samplePos);
        }
    }

    public static DikeDeposit deserialize(JsonObject json) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<String, HashMap<BlockState, Float>> oreBlocks = SerializerUtils.buildMultiBlockMatcherMap(json.get("blocks").getAsJsonObject());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils.buildMultiBlockMap(json, "samples");
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            int baseRadius = json.get("baseRadius").getAsInt();
            int genWt = json.get("generationWeight").getAsInt();
            TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, new ResourceLocation(json.get("biomeTag").getAsString().replace("#", "")));

            // Block State Matchers
            HashSet<BlockState> blockStateMatchers = DepositUtils.getDefaultMatchers();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = SerializerUtils.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new DikeDeposit(oreBlocks, sampleBlocks, yMin, yMax, baseRadius, genWt, biomeTag, blockStateMatchers);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse: {}", e.getMessage());
            return null;
        }
    }

    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        JsonObject config = new JsonObject();

        // Add basics of Plutons
        config.add("blocks", SerializerUtils.deconstructMultiBlockMatcherMap(this.matcherToOreWeightPair));
        config.add("samples", SerializerUtils.deconstructMultiBlockMap(this.sampleWeightPair));
        config.addProperty("yMin", this.yMin);
        config.addProperty("yMax", this.yMax);
        config.addProperty("baseRadius", this.baseRadius);
        config.addProperty("generationWeight", this.generationWeight);
        config.addProperty("biomeTag", this.biomeKey.location().toString());
        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
