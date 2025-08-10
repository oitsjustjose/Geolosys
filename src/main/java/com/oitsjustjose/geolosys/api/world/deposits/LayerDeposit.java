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

public class LayerDeposit extends AbstractDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_layer";

    private final int yMin;
    private final int yMax;
    private final int radius;
    private final int depth;

    public LayerDeposit(HashMap<String, HashMap<BlockState, Float>> matcherToOreWeightPair, HashMap<BlockState, Float> sampleWeightPair, int yMin, int yMax, int radius, int depth, int generationWeight, TagKey<Biome> biomeKey, HashSet<BlockState> matchers) {
        super(matcherToOreWeightPair, sampleWeightPair, biomeKey, matchers, generationWeight);
        this.yMin = yMin;
        this.yMax = yMax;
        this.radius = radius;
        this.depth = depth;
        validate(matcherToOreWeightPair, sampleWeightPair);
    }

    @Override
    public String toString() {
        return "Layer deposit with Blocks=" + this.getAllOres() + ", Samples=" + Arrays.toString(this.sampleWeightPair.keySet().toArray()) + ", Y Range=[" + this.yMin + "," + this.yMax + "], Radius=" + this.radius + ", Depth=" + this.depth;
    }

    @Override
    public int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated) {
        /* Dimension checking is done in PlutonRegistry#pick */
        /* Check biome allowance */
        if (!this.canPlaceInBiome(level.getBiome(pos))) {
            return 0;
        }

        int totlPlaced = 0;

        ChunkPos thisChunk = new ChunkPos(pos);

        int x = ((thisChunk.getMinBlockX() + thisChunk.getMaxBlockX()) / 2) - level.getRandom().nextInt(8) + level.getRandom().nextInt(16);
        int y = this.yMin + level.getRandom().nextInt(Math.abs(this.yMax - this.yMin));
        int z = ((thisChunk.getMinBlockZ() + thisChunk.getMaxBlockZ()) / 2) - level.getRandom().nextInt(8) + level.getRandom().nextInt(16);
        int max = Utils.getTopSolidBlock(level, pos).getY();
        if (y > max) {
            y = Math.max(yMin, max);
        }

        BlockPos basePos = new BlockPos(x, y, z);

        for (int dX = -this.radius; dX <= this.radius; dX++) {
            for (int dZ = -this.radius; dZ <= this.radius; dZ++) {
                for (int dY = 0; dY < depth; dY++) {
                    float dist = dX * dX + dZ * dZ;
                    if (dist > this.radius * 2) {
                        continue;
                    }

                    BlockPos placePos = basePos.offset(dX, dY, dZ);
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

                    if (FeatureUtils.enqueueBlockPlacement(level, thisChunk, placePos, tmp, deposits, chunksGenerated)) {
                        totlPlaced++;
                    }
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
        int maxSampleCnt = Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(), (this.radius / CommonConfig.MAX_SAMPLES_PER_CHUNK.get()) + (this.radius % CommonConfig.MAX_SAMPLES_PER_CHUNK.get()));
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

    public static LayerDeposit deserialize(JsonObject json) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<String, HashMap<BlockState, Float>> oreBlocks = SerializerUtils.buildMultiBlockMatcherMap(json.get("blocks").getAsJsonObject());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils.buildMultiBlockMap(json, "samples");
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            int radius = json.get("radius").getAsInt();
            int depth = json.get("depth").getAsInt();
            int genWt = json.get("generationWeight").getAsInt();
            TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, new ResourceLocation(json.get("biomeTag").getAsString().replace("#", "")));
            // Block State Matchers
            HashSet<BlockState> blockStateMatchers = DepositUtils.getDefaultMatchers();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = SerializerUtils.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new LayerDeposit(oreBlocks, sampleBlocks, yMin, yMax, radius, depth, genWt, biomeTag, blockStateMatchers);
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
        config.addProperty("radius", this.radius);
        config.addProperty("depth", this.depth);
        config.addProperty("generationWeight", this.generationWeight);
        config.addProperty("biomeTag", this.biomeKey.location().toString());

        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
