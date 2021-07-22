package com.oitsjustjose.geolosys.api.world.deposit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSerializationContext;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.DepositUtils;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.data.serializer.SerializerUtils;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.SampleUtils;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DenseDeposit implements IDeposit {
    public static final String JSON_TYPE = "geolosys:ore_deposit_dense";

    private HashMap<BlockState, Float> oreToWtMap = new HashMap<>();
    private HashMap<BlockState, Float> sampleToWtMap = new HashMap<>();
    private int yMin;
    private int yMax;
    private int size;
    private int genWt;
    private HashSet<BlockState> blockStateMatchers;
    private String[] dimFilter;
    private boolean isDimFilterBl;

    // Optional biome stuff!
    @Nullable
    private List<BiomeDictionary.Type> biomeTypeFilter;
    @Nullable
    private List<Biome> biomeFilter;
    @Nullable
    private boolean isBiomeFilterBl;

    private float sumWtOres = 0.0F;
    private float sumWtSamples = 0.0F;

    public DenseDeposit(HashMap<BlockState, Float> oreBlocks, HashMap<BlockState, Float> sampleBlocks, int yMin,
            int yMax, int size, int genWt, String[] dimFilter, boolean isDimFilterBl,
            @Nullable List<BiomeDictionary.Type> biomeTypes, @Nullable List<Biome> biomeFilter,
            @Nullable boolean isBiomeFilterBl, HashSet<BlockState> blockStateMatchers) {
        this.oreToWtMap = oreBlocks;
        this.sampleToWtMap = sampleBlocks;
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.genWt = genWt;
        this.dimFilter = dimFilter;
        this.isDimFilterBl = isDimFilterBl;
        this.biomeTypeFilter = biomeTypes;
        this.isBiomeFilterBl = isBiomeFilterBl;
        this.blockStateMatchers = blockStateMatchers;
        this.biomeFilter = biomeFilter;

        for (Entry<BlockState, Float> e : this.oreToWtMap.entrySet()) {
            this.sumWtOres += e.getValue();
        }
        assert sumWtOres == 1.0F : "Sum of weights for pluton blocks should equal 1.0";

        for (Entry<BlockState, Float> e : this.sampleToWtMap.entrySet()) {
            this.sumWtSamples += e.getValue();
        }
        assert sumWtSamples == 1.0F : "Sum of weights for pluton samples should equal 1.0";
    }

    /**
     * Uses {@link DepositUtils#pick(HashMap, float)} to find a random ore block to
     * return.
     * 
     * @return the random ore block chosen (based on weight) Can be null to
     *         represent "density" of the ore -- null results should be used to
     *         determine if the block in the world should be replaced. If null,
     *         don't replace 😉
     */
    @Nullable
    public BlockState getOre() {
        return DepositUtils.pick(this.oreToWtMap, this.sumWtOres);
    }

    /**
     * Uses {@link DepositUtils#pick(HashMap, float)} to find a random pluton sample
     * to return.
     * 
     * @return the random pluton sample chosen (based on weight) Can be null to
     *         represent "density" of the samples -- null results should be used to
     *         determine if the sample in the world should be replaced. If null,
     *         don't replace 😉
     */
    @Nullable
    public BlockState getSample() {
        return DepositUtils.pick(this.sampleToWtMap, this.sumWtSamples);
    }

    @Override
    public boolean canPlaceInBiome(Biome b) {
        return DepositUtils.canPlaceInBiome(b, this.biomeFilter, this.biomeTypeFilter, this.isBiomeFilterBl);
    }

    @Override
    public boolean hasBiomeRestrictions() {
        return this.biomeFilter != null || this.biomeTypeFilter != null;
    }

    @Override
    public int getGenWt() {
        return this.genWt;
    }

    @Override
    public String[] getDimensionFilter() {
        return this.dimFilter;
    }

    @Override
    public boolean isDimensionFilterBl() {
        return this.isDimFilterBl;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("Dense deposit with Blocks=");
        ret.append(Arrays.toString(this.oreToWtMap.keySet().toArray()));
        ret.append(", Samples=");
        ret.append(Arrays.toString(this.sampleToWtMap.keySet().toArray()));
        ret.append(", Y Range=[");
        ret.append(this.yMin);
        ret.append(",");
        ret.append(this.yMax);
        ret.append("], Size=");
        ret.append(this.size);
        return ret.toString();
    }

    /**
     * Handles full-on generation of this type of pluton (dense). Requires 0
     * arguments as everything is self-contained in this class
     * 
     * @return (int) the number of pluton resource blocks placed. If 0 -- this
     *         should be evaluted as a false for use of Mojang's sort-of sketchy
     *         generation code in
     *         {@link DepositFeature#generate(net.minecraft.world.ISeedReader, net.minecraft.world.gen.ChunkGenerator, java.util.Random, net.minecraft.util.math.BlockPos, net.minecraft.world.gen.feature.NoFeatureConfig)}
     */
    @Override
    public int generate(ISeedReader reader, BlockPos pos, IDepositCapability cap) {
        /* Dimension checking is done in PlutonRegistry#pick */

        /* Check biome allowance */
        if (!DepositUtils.canPlaceInBiome(reader.getBiome(pos), this.biomeFilter, this.biomeTypeFilter,
                this.isBiomeFilterBl)) {
            return 0;
        }

        int totlPlaced = 0;

        int randY = this.yMin + reader.getRandom().nextInt(this.yMax - this.yMin);
        int max = DepositUtils.getMaxTerrainHeight(reader, pos.getX(), pos.getZ());
        if (randY > max) {
            randY = Math.max(yMin, max);
        }

        float ranFlt = reader.getRandom().nextFloat() * (float) Math.PI;
        double x1 = (float) (pos.getX() + 8) + MathHelper.sin(ranFlt) * (float) this.size / 8.0F;
        double x2 = (float) (pos.getX() + 8) - MathHelper.sin(ranFlt) * (float) this.size / 8.0F;
        double z1 = (float) (pos.getZ() + 8) + MathHelper.cos(ranFlt) * (float) this.size / 8.0F;
        double z2 = (float) (pos.getZ() + 8) - MathHelper.cos(ranFlt) * (float) this.size / 8.0F;
        double y1 = randY + reader.getRandom().nextInt(3) - 2;
        double y2 = randY + reader.getRandom().nextInt(3) - 2;

        for (int i = 0; i < this.size; ++i) {
            float radScl = (float) i / (float) this.size;
            double xn = x1 + (x2 - x1) * (double) radScl;
            double yn = y1 + (y2 - y1) * (double) radScl;
            double zn = z1 + (z2 - z1) * (double) radScl;
            double noise = reader.getRandom().nextDouble() * (double) this.size / 16.0D;
            double radius = (double) (MathHelper.sin((float) Math.PI * radScl) + 1.0F) * noise + 1.0D;
            int xmin = MathHelper.floor(xn - radius / 2.0D);
            int ymin = MathHelper.floor(yn - radius / 2.0D);
            int zmin = MathHelper.floor(zn - radius / 2.0D);
            int xmax = MathHelper.floor(xn + radius / 2.0D);
            int ymax = MathHelper.floor(yn + radius / 2.0D);
            int zmax = MathHelper.floor(zn + radius / 2.0D);

            for (int x = xmin; x <= xmax; ++x) {
                double layerRadX = ((double) x + 0.5D - xn) / (radius / 2.0D);

                if (layerRadX * layerRadX < 1.0D) {
                    for (int y = ymin; y <= ymax; ++y) {
                        double layerRadY = ((double) y + 0.5D - yn) / (radius / 2.0D);

                        if (layerRadX * layerRadX + layerRadY * layerRadY < 1.0D) {
                            for (int z = zmin; z <= zmax; ++z) {
                                double layerRadZ = ((double) z + 0.5D - zn) / (radius / 2.0D);

                                if (layerRadX * layerRadX + layerRadY * layerRadY + layerRadZ * layerRadZ < 1.0D) {
                                    BlockPos placePos = new BlockPos(x, y, z);
                                    BlockState state = reader.getBlockState(placePos);
                                    BlockState tmp = this.getOre();
                                    if (tmp == null) {
                                        continue;
                                    }

                                    for (BlockState matcherState : (this.blockStateMatchers == null
                                            ? DepositUtils.getDefaultMatchers()
                                            : this.blockStateMatchers)) {
                                        if (Utils.doStatesMatch(matcherState, state)) {
                                            if (FeatureUtils.tryPlaceBlock(reader, new ChunkPos(pos), placePos, tmp,
                                                    cap)) {
                                                totlPlaced++;
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return totlPlaced;
    }

    /**
     * Handles what to do after the world has generated
     */
    @Override
    public void afterGen(ISeedReader reader, BlockPos pos) {
        // Debug the pluton
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.debug("Generated {} in Chunk {} (Pos [{} {} {}])", this.toString(),
                    new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }

        int maxSampleCnt = Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(),
                (this.size / CommonConfig.MAX_SAMPLES_PER_CHUNK.get())
                        + (this.size % CommonConfig.MAX_SAMPLES_PER_CHUNK.get()));
        for (int i = 0; i < maxSampleCnt; i++) {
            BlockPos samplePos = SampleUtils.getSamplePosition(reader, new ChunkPos(pos), this.yMax);
            BlockState tmp = this.getSample();

            if (tmp == null) {
                continue;
            }

            if (samplePos == null || SampleUtils.inNonWaterFluid(reader, samplePos)) {
                continue;
            }

            // Prevent replacing the block with the same block
            if (reader.getBlockState(samplePos) != tmp) {
                // Waterlog if possible
                if (SampleUtils.isInWater(reader, samplePos) && tmp.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    reader.setBlockState(samplePos, tmp.with(BlockStateProperties.WATERLOGGED, Boolean.TRUE), 2 | 16);
                } else { // Default to std state if not possible
                    reader.setBlockState(samplePos, tmp, 2 | 16);
                }
            }
        }
    }

    public static DenseDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<BlockState, Float> oreBlocks = SerializerUtils
                    .buildMultiBlockMap(json.get("blocks").getAsJsonArray());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils
                    .buildMultiBlockMap(json.get("samples").getAsJsonArray());
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            int size = json.get("size").getAsInt();
            int genWt = json.get("generationWeight").getAsInt();

            // Dimensions
            String[] dimFilter = SerializerUtils.getDimFilter(json);
            boolean isDimFilterBl = SerializerUtils.getIsDimFilterBl(json);

            // Biomes
            boolean isBiomeFilterBl = true;
            List<BiomeDictionary.Type> biomeTypeFilter = null;
            List<Biome> biomeFilter = null;
            if (json.has("biomes")) {
                String[] biomeArrRaw = SerializerUtils.getBiomeFilter(json);
                isBiomeFilterBl = SerializerUtils.getIsBiomeFilterBl(json);
                biomeTypeFilter = SerializerUtils.extractBiomeTypes(biomeArrRaw);
                biomeFilter = SerializerUtils.extractBiomes(biomeArrRaw);
            }

            // Block State Matchers
            HashSet<BlockState> blockStateMatchers = DepositUtils.getDefaultMatchers();
            if (json.has("blockStateMatchers")) {
                blockStateMatchers = SerializerUtils.toBlockStateList(json.get("blockStateMatchers").getAsJsonArray());
            }

            return new DenseDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, genWt, dimFilter, isDimFilterBl,
                    biomeTypeFilter, biomeFilter, isBiomeFilterBl, blockStateMatchers);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", e);
            return null;
        }
    }

    public JsonElement serialize(DenseDeposit dep, JsonSerializationContext ctx) {
        JsonObject json = new JsonObject();
        JsonObject config = new JsonObject();
        JsonParser parser = new JsonParser();

        // Custom logic for the biome filtering
        JsonObject biomes = new JsonObject();
        biomes.addProperty("isBlacklist", this.isBiomeFilterBl);
        biomes.add("filter", SerializerUtils.deconstructBiomes(this.biomeFilter, this.biomeTypeFilter));

        // Custom logic for the dimension filtering
        JsonObject dimensions = new JsonObject();
        dimensions.addProperty("isBlacklist", this.isDimFilterBl);
        dimensions.add("filter", parser.parse(Arrays.toString(this.dimFilter)));

        // Add basics of Plutons
        config.add("blocks", SerializerUtils.deconstructMultiBlockMap(this.oreToWtMap));
        config.add("samples", SerializerUtils.deconstructMultiBlockMap(this.oreToWtMap));
        config.addProperty("yMin", this.yMin);
        config.addProperty("yMax", this.yMax);
        config.addProperty("size", this.size);
        config.addProperty("generationWeight", this.genWt);
        config.add("dimensions", dimensions);
        config.add("biomes", biomes);

        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
