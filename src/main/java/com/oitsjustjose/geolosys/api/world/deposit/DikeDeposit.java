package com.oitsjustjose.geolosys.api.world.deposit;

import com.google.gson.*;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.DepositUtils;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.data.serializer.SerializerUtils;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.SampleUtils;
import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class DikeDeposit implements IDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_dike";

    private HashMap<BlockState, Float> oreToWtMap = new HashMap<>();
    private HashMap<BlockState, Float> sampleToWtMap = new HashMap<>();
    private int yMin;
    private int yMax;
    private int baseRadius;
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

    public DikeDeposit(HashMap<BlockState, Float> oreBlocks, HashMap<BlockState, Float> sampleBlocks, int yMin,
            int yMax, int baseRadius, int genWt, String[] dimFilter, boolean isDimFilterBl,
            @Nullable List<BiomeDictionary.Type> biomeTypes, @Nullable List<Biome> biomeFilter,
            @Nullable boolean isBiomeFilterBl, HashSet<BlockState> blockStateMatchers) {
        this.oreToWtMap = oreBlocks;
        this.sampleToWtMap = sampleBlocks;
        this.yMin = yMin;
        this.yMax = yMax;
        this.baseRadius = baseRadius;
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
    @Nullable
    public HashSet<BlockState> getAllOres() {
        HashSet<BlockState> ret = new HashSet<BlockState>();
        this.oreToWtMap.keySet().forEach((bs) -> {
            if (bs != null) {
                ret.add(bs);
            }
        });
        return ret.isEmpty() ? null : ret;
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
        ret.append("Dike deposit with Blocks=");
        ret.append(Arrays.toString(this.oreToWtMap.keySet().toArray()));
        ret.append(", Samples=");
        ret.append(Arrays.toString(this.sampleToWtMap.keySet().toArray()));
        ret.append(", Y Range=[");
        ret.append(this.yMin);
        ret.append(",");
        ret.append(this.yMax);
        ret.append("], Radius of Base=");
        ret.append(this.baseRadius);
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
    public int generate(WorldGenLevel reader, BlockPos pos, IDepositCapability cap) {
        /* Dimension checking is done in PlutonRegistry#pick */
        /* Check biome allowance */
        if (!DepositUtils.canPlaceInBiome(reader.getBiome(pos), this.biomeFilter, this.biomeTypeFilter,
                this.isBiomeFilterBl)) {
            return 0;
        }

        ChunkPos thisChunk = new ChunkPos(pos);
        int height = Math.abs((this.yMax - this.yMin));
        int x = thisChunk.getMinBlockX() + reader.getRandom().nextInt(16);
        int z = thisChunk.getMinBlockZ() + reader.getRandom().nextInt(16);
        int yMin = this.yMin + reader.getRandom().nextInt(height / 4);
        int yMax = this.yMax - reader.getRandom().nextInt(height / 4);
        int max = Utils.getTopSolidBlock(reader, pos).getY();
        if (yMin > max) {
            yMin = Math.max(yMin, max);
        } else if (yMin == yMax) {
            yMax = this.yMax;
        }
        BlockPos basePos = new BlockPos(x, yMin, z);

        int totlPlaced = 0;
        int htRnd = Math.abs((yMax - yMin));
        int rad = this.baseRadius / 2;
        boolean shouldSub = false;

        for (int dY = yMin; dY <= yMax; dY++) {
            for (int dX = -rad; dX <= rad; dX++) {
                for (int dZ = -rad; dZ <= rad; dZ++) {
                    float dist = (dX * dX) + (dZ * dZ);
                    if (dist > rad) {
                        continue;
                    }

                    BlockPos placePos = new BlockPos(basePos.getX() + dX, dY, basePos.getZ() + dZ);
                    BlockState tmp = this.getOre();
                    if (tmp == null) {
                        continue;
                    }

                    // Skip this block if it can't replace the target block
                    if (!this.getBlockStateMatchers()
                            .contains(FeatureUtils.tryGetBlockState(reader, thisChunk, placePos))) {
                        continue;
                    }

                    if (FeatureUtils.tryPlaceBlock(reader, new ChunkPos(pos), placePos, tmp, cap)) {
                        totlPlaced++;
                    }
                }
            }

            // flip at around the halfway point.
            if (yMin + (htRnd / 2) <= dY) {
                shouldSub = true;
            }
            if (reader.getRandom().nextInt(3) == 0) {
                rad += shouldSub ? -1 : 1;
                if (rad <= 0) {
                    return totlPlaced;
                }
            }
        }

        return totlPlaced;
    }

    /**
     * Handles what to do after the world has generated
     */
    @Override
    public void afterGen(WorldGenLevel reader, BlockPos pos, IDepositCapability cap) {
        // Debug the pluton
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.debug("Generated {} in Chunk {} (Pos [{} {} {}])", this.toString(),
                    new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }

        ChunkPos thisChunk = new ChunkPos(pos);
        int maxSampleCnt = Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(),
                (this.baseRadius / CommonConfig.MAX_SAMPLES_PER_CHUNK.get())
                        + (this.baseRadius % CommonConfig.MAX_SAMPLES_PER_CHUNK.get()));
        maxSampleCnt = Math.max(maxSampleCnt, 1);
        for (int i = 0; i < maxSampleCnt; i++) {
            BlockPos samplePos = SampleUtils.getSamplePosition(reader, new ChunkPos(pos));
            BlockState tmp = this.getSample();

            if (tmp == null) {
                continue;
            }

            if (samplePos == null || SampleUtils.inNonWaterFluid(reader, samplePos)) {
                continue;
            }

            if (SampleUtils.isInWater(reader, samplePos) && tmp.hasProperty(BlockStateProperties.WATERLOGGED)) {
                tmp = tmp.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
            }

            FeatureUtils.tryPlaceBlock(reader, thisChunk, samplePos, tmp, cap);
            FeatureUtils.fixSnowyBlock(reader, samplePos);
        }
    }

    @Override
    public HashSet<BlockState> getBlockStateMatchers() {
        return this.blockStateMatchers == null ? DepositUtils.getDefaultMatchers() : this.blockStateMatchers;
    }

    public static DikeDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
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
            int baseRadius = json.get("baseRadius").getAsInt();
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

            return new DikeDeposit(oreBlocks, sampleBlocks, yMin, yMax, baseRadius, genWt, dimFilter, isDimFilterBl,
                    biomeTypeFilter, biomeFilter, isBiomeFilterBl, blockStateMatchers);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse JSON file: {}", json.toString());
            return null;
        }
    }

    public JsonElement serialize(DikeDeposit dep, JsonSerializationContext ctx) {
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
        config.addProperty("baseRadius", this.baseRadius);
        config.addProperty("generationWeight", this.genWt);
        config.add("dimensions", dimensions);
        config.add("biomes", biomes);

        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
