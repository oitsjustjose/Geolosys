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
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.BiomeDictionary;

public class SparseDeposit implements IDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_sparse";

    private HashMap<String, HashMap<BlockState, Float>> oreToWtMap = new HashMap<>();
    private HashMap<BlockState, Float> sampleToWtMap = new HashMap<>();
    private int yMin;
    private int yMax;
    private int size;
    private int spread;
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

    /* Hashmap of blockMatcher.getRegistryName(): sumWt */
    private HashMap<String, Float> cumulOreWtMap = new HashMap<>();
    private float sumWtSamples = 0.0F;

    public SparseDeposit(HashMap<String, HashMap<BlockState, Float>> oreBlocks, HashMap<BlockState, Float> sampleBlocks,
            int yMin,
            int yMax, int size, int spread, int genWt, String[] dimFilter, boolean isDimFilterBl,
            @Nullable List<BiomeDictionary.Type> biomeTypes, @Nullable List<Biome> biomeFilter,
            @Nullable boolean isBiomeFilterBl, HashSet<BlockState> blockStateMatchers) {
        this.oreToWtMap = oreBlocks;
        this.sampleToWtMap = sampleBlocks;
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.spread = spread;
        this.genWt = genWt;
        this.dimFilter = dimFilter;
        this.isDimFilterBl = isDimFilterBl;
        this.biomeTypeFilter = biomeTypes;
        this.isBiomeFilterBl = isBiomeFilterBl;
        this.blockStateMatchers = blockStateMatchers;
        this.biomeFilter = biomeFilter;

        // Verify that blocks.default exists.
        if (!this.oreToWtMap.containsKey("default")) {
            throw new RuntimeException("Pluton blocks should always have a default key");
        }

        for (Entry<String, HashMap<BlockState, Float>> i : this.oreToWtMap.entrySet()) {
            if (!this.cumulOreWtMap.containsKey(i.getKey())) {
                this.cumulOreWtMap.put(i.getKey(), 0.0F);
            }

            for (Entry<BlockState, Float> j : i.getValue().entrySet()) {
                float v = this.cumulOreWtMap.get(i.getKey());
                this.cumulOreWtMap.put(i.getKey(), v + j.getValue());
            }

            if (this.cumulOreWtMap.get(i.getKey()) != 1.0F) {
                throw new RuntimeException("Sum of weights for pluton blocks should equal 1.0");
            }
        }

        for (Entry<BlockState, Float> e : this.sampleToWtMap.entrySet()) {
            this.sumWtSamples += e.getValue();
        }

        if (sumWtSamples != 1.0F) {
            throw new RuntimeException("Sum of weights for pluton samples should equal 1.0");
        }
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
    public BlockState getOre(BlockState currentState) {
        String res = currentState.getBlock().getRegistryName().toString();
        if (this.oreToWtMap.containsKey(res)) {
            // Return a choice from a specialized set here
            HashMap<BlockState, Float> mp = this.oreToWtMap.get(res);
            return DepositUtils.pick(mp, this.cumulOreWtMap.get(res));
        }
        return DepositUtils.pick(this.oreToWtMap.get("default"), this.cumulOreWtMap.get("default"));
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
    @Nullable
    public HashSet<BlockState> getAllOres() {
        HashSet<BlockState> ret = new HashSet<BlockState>();
        this.oreToWtMap.values().forEach(x -> x.keySet().forEach(y -> ret.add(y)));
        ret.remove(Blocks.AIR.defaultBlockState());
        return ret.isEmpty() ? null : ret;
    }

    @Override
    public boolean canPlaceInBiome(Holder<Biome> b) {
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
        ret.append("Sparse deposit with Blocks=");
        ret.append(this.getAllOres());
        ret.append(", Samples=");
        ret.append(Arrays.toString(this.sampleToWtMap.keySet().toArray()));
        ret.append(", Y Range=[");
        ret.append(this.yMin);
        ret.append(",");
        ret.append(this.yMax);
        ret.append("], Size of deposit =");
        ret.append(this.size);
        ret.append(", Spread=");
        ret.append(this.spread);
        return ret.toString();
    }

    /**
     * Handles full-on generation of this type of pluton. Requires 0 arguments as
     * everything is self-contained in this class
     * 
     * @return (int) the number of pluton resource blocks placed. If 0 -- this
     *         should be evaluted as a false for use of Mojang's sort-of sketchy
     *         generation code in
     */
    @Override
    public int generate(WorldGenLevel level, BlockPos pos, IDepositCapability cap) {
        /* Dimension checking is done in PlutonRegistry#pick */
        /* Check biome allowance */
        if (!DepositUtils.canPlaceInBiome(level.getBiome(pos), this.biomeFilter, this.biomeTypeFilter,
                this.isBiomeFilterBl)) {
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
                                    int xSpread = level.getRandom().nextInt(this.spread)
                                            * (level.getRandom().nextBoolean() ? 1 : -1);
                                    int zSpread = level.getRandom().nextInt(this.spread)
                                            * (level.getRandom().nextBoolean() ? 1 : -1);

                                    BlockPos placePos = new BlockPos(x + xSpread, y, z + zSpread);
                                    BlockState current = level.getBlockState(placePos);
                                    BlockState tmp = this.getOre(current);
                                    if (tmp == null) {
                                        continue;
                                    }
                                    // Skip this block if it can't replace the target block or doesn't have a
                                    // manually-configured replacer in the blocks object
                                    if (!(this.getBlockStateMatchers().contains(current)
                                            || this.oreToWtMap
                                                    .containsKey(current.getBlock().getRegistryName().toString()))) {
                                        continue;
                                    }

                                    if (FeatureUtils.tryPlaceBlock(level, thisChunk, placePos, tmp, cap)) {
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

    /**
     * Handles what to do after the world has generated
     */
    @Override
    public void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability cap) {
        // Debug the pluton
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.info("Generated {} in Chunk {} (Pos [{} {} {}])", this.toString(),
                    new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }

        ChunkPos thisChunk = new ChunkPos(pos);
        int maxSampleCnt = (Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(),
                (this.size / CommonConfig.MAX_SAMPLES_PER_CHUNK.get())
                        + (this.size % CommonConfig.MAX_SAMPLES_PER_CHUNK.get())))
                * ((int) (spread / 16));

        for (int i = 0; i < maxSampleCnt; i++) {
            BlockState tmp = this.getSample();
            if (tmp == null) {
                continue;
            }

            BlockPos samplePos = SampleUtils.getSamplePosition(level, new ChunkPos(pos));
            if (samplePos == null || SampleUtils.inNonWaterFluid(level, samplePos)) {
                continue;
            }

            if (SampleUtils.isInWater(level, samplePos) && tmp.hasProperty(BlockStateProperties.WATERLOGGED)) {
                tmp = tmp.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
            }

            FeatureUtils.tryPlaceBlock(level, thisChunk, samplePos, tmp, cap);
            FeatureUtils.fixSnowyBlock(level, samplePos);
        }
    }

    @Override
    public HashSet<BlockState> getBlockStateMatchers() {
        return this.blockStateMatchers == null ? DepositUtils.getDefaultMatchers() : this.blockStateMatchers;
    }

    public static SparseDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<String, HashMap<BlockState, Float>> oreBlocks = SerializerUtils
                    .buildMultiBlockMatcherMap(json.get("blocks").getAsJsonObject());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils
                    .buildMultiBlockMap(json.get("samples").getAsJsonArray());
            int yMin = json.get("yMin").getAsInt();
            int yMax = json.get("yMax").getAsInt();
            int spread = json.get("spread").getAsInt();
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

            return new SparseDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, spread, genWt, dimFilter, isDimFilterBl,
                    biomeTypeFilter, biomeFilter, isBiomeFilterBl, blockStateMatchers);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public JsonElement serialize(SparseDeposit dep, JsonSerializationContext ctx) {
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
        config.add("blocks", SerializerUtils.deconstructMultiBlockMatcherMap(this.oreToWtMap));
        config.add("samples", SerializerUtils.deconstructMultiBlockMap(this.sampleToWtMap));
        config.addProperty("yMin", this.yMin);
        config.addProperty("yMax", this.yMax);
        config.addProperty("size", this.size);
        config.addProperty("spread", this.spread);
        config.addProperty("generationWeight", this.genWt);
        config.add("dimensions", dimensions);
        config.add("biomes", biomes);

        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
