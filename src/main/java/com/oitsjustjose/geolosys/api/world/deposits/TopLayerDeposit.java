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

public class TopLayerDeposit extends AbstractDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_top_layer";

    private final int radius;
    private final int depth;
    private final float sampleChance;

    public TopLayerDeposit(HashMap<String, HashMap<BlockState, Float>> matcherToOreWeightPair, HashMap<BlockState, Float> sampleWeightPair, int radius, int depth, float sampleChance, int generationWeight, TagKey<Biome> biomeKey, HashSet<BlockState> matchers) {
        super(matcherToOreWeightPair, sampleWeightPair, biomeKey, matchers, generationWeight);
        this.radius = radius;
        this.depth = depth;
        this.sampleChance = sampleChance;
        validate(matcherToOreWeightPair, sampleWeightPair);
    }


    @Override
    public String toString() {
        return "Top Layer deposit with Blocks=" + this.getAllOres() + ", Samples=" + Arrays.toString(this.sampleWeightPair.keySet().toArray()) + ", Radius=" + this.radius + ", Depth=" + this.depth;
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
        int z = ((thisChunk.getMinBlockZ() + thisChunk.getMaxBlockZ()) / 2) - level.getRandom().nextInt(8) + level.getRandom().nextInt(16);
        int radX = (this.radius / 2) + level.getRandom().nextInt(this.radius / 2);
        int radZ = (this.radius / 2) + level.getRandom().nextInt(this.radius / 2);

        BlockPos basePos = new BlockPos(x, 0, z);

        for (int dX = -radX; dX <= radX; dX++) {
            for (int dZ = -radZ; dZ <= radZ; dZ++) {
                if (((dX * dX) + (dZ * dZ)) > this.radius + level.getRandom().nextInt(Math.max(1, this.radius / 2))) {
                    continue;
                }

                BlockPos baseForXZ = Utils.getTopSolidBlock(level, basePos.offset(dX, 0, dZ));

                for (int i = 0; i < this.depth; i++) {
                    BlockPos placePos = baseForXZ.below(i);
                    BlockState current = level.getBlockState(placePos);
                    BlockState tmp = this.getOre(current, level.getRandom());
                    boolean isTop = i == 0;

                    if (tmp == null) {
                        continue;
                    } else if (tmp.hasProperty(BlockStateProperties.BOTTOM)) {
                        tmp = tmp.setValue(BlockStateProperties.BOTTOM, !isTop);
                    }

                    // Skip this block if it can't replace the target block or doesn't have a
                    // manually-configured replacer in the blocks object
                    if (!(this.getMatchers().contains(current) || this.matcherToOreWeightPair.containsKey(Utils.getRegistryName(current)))) {
                        continue;
                    }

                    if (FeatureUtils.enqueueBlockPlacement(level, thisChunk, placePos, tmp, deposits, chunksGenerated)) {
                        totlPlaced++;
                        if (isTop && level.getRandom().nextFloat() <= this.sampleChance) {
                            BlockState smpl = this.getSample(level.getRandom());
                            if (smpl != null) {
                                FeatureUtils.enqueueBlockPlacement(level, thisChunk, placePos.above(), smpl, deposits, chunksGenerated);
                                FeatureUtils.fixSnowyBlock(level, placePos);
                            }
                        }
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
    }

    public static TopLayerDeposit deserialize(JsonObject json) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<String, HashMap<BlockState, Float>> oreBlocks = SerializerUtils.buildMultiBlockMatcherMap(json.get("blocks").getAsJsonObject());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils.buildMultiBlockMap(json, "samples");
            int radius = json.get("radius").getAsInt();
            int depth = json.get("depth").getAsInt();
            float sampleChance = json.get("chanceForSample").getAsFloat();
            int genWt = json.get("generationWeight").getAsInt();
            TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, new ResourceLocation(json.get("biomeTag").getAsString().replace("#", "")));

            // Block State Matchers
            HashSet<BlockState> blockStateMatchers = DepositUtils.getDefaultMatchers();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = SerializerUtils.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new TopLayerDeposit(oreBlocks, sampleBlocks, radius, depth, sampleChance, genWt, biomeTag, blockStateMatchers);
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
        config.addProperty("radius", this.radius);
        config.addProperty("depth", this.depth);
        config.addProperty("chanceForSample", this.sampleChance);
        config.addProperty("generationWeight", this.generationWeight);
        config.addProperty("biomeTag", this.biomeKey.location().toString());
        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
