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
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class SparseDeposit extends AbstractDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_sparse";

    private final int yMin;
    private final int yMax;
    private final int size;
    private final int spread;

    public SparseDeposit(HashMap<String, HashMap<BlockState, Float>> matcherToOreWeightPair, HashMap<BlockState, Float> sampleWeightPair, int yMin, int yMax, int size, int spread, int generationWeight, TagKey<Biome> biomeKey, HashSet<BlockState> matchers) {
        super(matcherToOreWeightPair, sampleWeightPair, biomeKey, matchers, generationWeight);
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.spread = spread;
        validate(matcherToOreWeightPair, sampleWeightPair);
    }

    @Override
    public String toString() {
        return "Sparse deposit with Blocks=" + this.getAllOres() + ", Samples=" + Arrays.toString(this.sampleWeightPair.keySet().toArray()) + ", Y Range=[" + this.yMin + "," + this.yMax + "], Size of deposit =" + this.size + ", Spread=" + this.spread;
    }

    @Override
    public int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated) {
        /* Dimension checking is done in PlutonRegistry#pick */
        /* Check biome allowance */
        if (!this.canPlaceInBiome(level.getBiome(pos))) {
            return 0;
        }

        int totlPlaced = 0;
        int totlPnding = 0;
        ChunkPos thisChunk = new ChunkPos(pos);
        int randY = this.yMin + level.getRandom().nextInt(this.yMax - this.yMin);
        int max = Utils.getTopSolidBlock(level, pos).getY();
        if (randY > max) {
            randY = Math.max(yMin, max);
        }

        float ranFlt = level.getRandom().nextFloat() * (float) Math.PI;
        double x1 = (float) (pos.getX() + 8) + Mth.sin(ranFlt) * (float) this.size / 8.0F;
        double x2 = (float) (pos.getX() + 8) - Mth.sin(ranFlt) * (float) this.size / 8.0F;
        double z1 = (float) (pos.getZ() + 8) + Mth.cos(ranFlt) * (float) this.size / 8.0F;
        double z2 = (float) (pos.getZ() + 8) - Mth.cos(ranFlt) * (float) this.size / 8.0F;
        double y1 = randY + level.getRandom().nextInt(3) - 2;
        double y2 = randY + level.getRandom().nextInt(3) - 2;

        for (int i = 0; i < this.size; ++i) {
            float radScl = (float) i / (float) this.size;
            double xn = x1 + (x2 - x1) * (double) radScl;
            double yn = y1 + (y2 - y1) * (double) radScl;
            double zn = z1 + (z2 - z1) * (double) radScl;
            double noise = level.getRandom().nextDouble() * (double) this.size / 16.0D;
            double radius = (double) (Mth.sin((float) Math.PI * radScl) + 1.0F) * noise + 1.0D;
            int xmin = Mth.floor(xn - radius / 2.0D);
            int ymin = Mth.floor(yn - radius / 2.0D);
            int zmin = Mth.floor(zn - radius / 2.0D);
            int xmax = Mth.floor(xn + radius / 2.0D);
            int ymax = Mth.floor(yn + radius / 2.0D);
            int zmax = Mth.floor(zn + radius / 2.0D);

            for (int x = xmin; x <= xmax; ++x) {
                double layerRadX = ((double) x + 0.5D - xn) / (radius / 2.0D);

                if (layerRadX * layerRadX < 1.0D) {
                    for (int y = ymin; y <= ymax; ++y) {
                        double layerRadY = ((double) y + 0.5D - yn) / (radius / 2.0D);

                        if (layerRadX * layerRadX + layerRadY * layerRadY < 1.0D) {
                            for (int z = zmin; z <= zmax; ++z) {
                                double layerRadZ = ((double) z + 0.5D - zn) / (radius / 2.0D);

                                if (layerRadX * layerRadX + layerRadY * layerRadY + layerRadZ * layerRadZ < 1.0D) {

                                    // Randomize spread on the X and Z Axes
                                    int xSpread = level.getRandom().nextInt(this.spread) * (level.getRandom().nextBoolean() ? 1 : -1);
                                    int zSpread = level.getRandom().nextInt(this.spread) * (level.getRandom().nextBoolean() ? 1 : -1);

                                    BlockPos placePos = new BlockPos(x + xSpread, y, z + zSpread);
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
                                    } else {
                                        totlPnding++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return totlPlaced + totlPnding;
    }

    @Override
    public void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated) {
        // Debug the pluton
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.info("Generated {} in Chunk {} (Pos [{} {} {}])", this.toString(), new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }

        ChunkPos thisChunk = new ChunkPos(pos);
        int maxSampleCnt = (int) ((float) Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(), (this.size / CommonConfig.MAX_SAMPLES_PER_CHUNK.get()) + (this.size % CommonConfig.MAX_SAMPLES_PER_CHUNK.get())) * ((float) spread / 16.0F));

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

    public static SparseDeposit deserialize(JsonObject json) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<String, HashMap<BlockState, Float>> oreBlocks = SerializerUtils.buildMultiBlockMatcherMap(json.get("blocks").getAsJsonObject());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils.buildMultiBlockMap(json, "samples");
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            int spread = json.get("spread").getAsInt();
            int size = json.get("size").getAsInt();
            int genWt = json.get("generationWeight").getAsInt();
            TagKey<Biome> biomeTag = TagKey.create(Registries.BIOME, new ResourceLocation(json.get("biomeTag").getAsString().replace("#", "")));

            // Block State Matchers
            HashSet<BlockState> blockStateMatchers = DepositUtils.getDefaultMatchers();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = SerializerUtils.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new SparseDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, spread, genWt, biomeTag, blockStateMatchers);
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
        config.addProperty("size", this.size);
        config.addProperty("spread", this.spread);
        config.addProperty("generationWeight", this.generationWeight);
        config.addProperty("biomeTag", this.biomeKey.location().toString());
        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
