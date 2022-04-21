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
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.BiomeDictionary;

public class TopLayerDeposit implements IDeposit {
    public static final String JSON_TYPE = "geolosys:deposit_top_layer";

    private HashMap<String, HashMap<BlockState, Float>> oreToWtMap = new HashMap<>();
    private HashMap<BlockState, Float> sampleToWtMap = new HashMap<>();
    private int radius;
    private int depth;
    private float sampleChance;
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

    public TopLayerDeposit(HashMap<String, HashMap<BlockState, Float>> oreBlocks,
            HashMap<BlockState, Float> sampleBlocks, int radius,
            int depth, float sampleChance, int genWt, String[] dimFilter, boolean isDimFilterBl,
            @Nullable List<BiomeDictionary.Type> biomeTypes, @Nullable List<Biome> biomeFilter,
            @Nullable boolean isBiomeFilterBl, HashSet<BlockState> blockStateMatchers) {
        this.oreToWtMap = oreBlocks;
        this.sampleToWtMap = sampleBlocks;
        this.radius = radius;
        this.depth = depth;
        this.sampleChance = sampleChance;
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
        return null;
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
        ret.append("Top Layer deposit with Blocks=");
        ret.append(this.getAllOres());
        ret.append(", Samples=");
        ret.append(Arrays.toString(this.sampleToWtMap.keySet().toArray()));
        ret.append(", Radius=");
        ret.append(this.radius);
        ret.append(", Depth=");
        ret.append(this.depth);
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
        ChunkPos thisChunk = new ChunkPos(pos);

        int x = ((thisChunk.getMinBlockX() + thisChunk.getMaxBlockX()) / 2) - level.getRandom().nextInt(8)
                + level.getRandom().nextInt(16);
        int z = ((thisChunk.getMinBlockZ() + thisChunk.getMaxBlockZ()) / 2) - level.getRandom().nextInt(8)
                + level.getRandom().nextInt(16);
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
                    BlockState tmp = this.getOre(current);
                    boolean isTop = i == 0;

                    if (tmp == null) {
                        continue;
                    } else if (tmp.hasProperty(BlockStateProperties.BOTTOM)) {
                        tmp = tmp.setValue(BlockStateProperties.BOTTOM, !isTop);
                    }

                    // Skip this block if it can't replace the target block or doesn't have a
                    // manually-configured replacer in the blocks object
                    if (!(this.getBlockStateMatchers().contains(current)
                            || this.oreToWtMap.containsKey(current.getBlock().getRegistryName().toString()))) {
                        continue;
                    }

                    if (FeatureUtils.tryPlaceBlock(level, thisChunk, placePos, tmp, cap)) {
                        totlPlaced++;
                        if (isTop && level.getRandom().nextFloat() <= this.sampleChance) {
                            BlockState smpl = this.getSample();
                            if (smpl != null) {
                                FeatureUtils.tryPlaceBlock(level, thisChunk, placePos.above(), smpl, cap);
                                FeatureUtils.fixSnowyBlock(level, placePos);
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
    public void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability cap) {
        // Debug the pluton
        if (CommonConfig.DEBUG_WORLD_GEN.get()) {
            Geolosys.getInstance().LOGGER.info("Generated {} in Chunk {} (Pos [{} {} {}])", this.toString(),
                    new ChunkPos(pos), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    @Override
    public HashSet<BlockState> getBlockStateMatchers() {
        return this.blockStateMatchers == null ? DepositUtils.getDefaultMatchers() : this.blockStateMatchers;
    }

    public static TopLayerDeposit deserialize(JsonObject json, JsonDeserializationContext ctx) {
        if (json == null) {
            return null;
        }

        try {
            // Plutons 101 -- basics and intro to getting one gen'd
            HashMap<String, HashMap<BlockState, Float>> oreBlocks = SerializerUtils
                    .buildMultiBlockMatcherMap(json.get("blocks").getAsJsonObject());
            HashMap<BlockState, Float> sampleBlocks = SerializerUtils
                    .buildMultiBlockMap(json.get("samples").getAsJsonArray());
            int radius = json.get("radius").getAsInt();
            int depth = json.get("depth").getAsInt();
            float sampleChance = json.get("chanceForSample").getAsFloat();
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

            return new TopLayerDeposit(oreBlocks, sampleBlocks, radius, depth, sampleChance, genWt, dimFilter,
                    isDimFilterBl, biomeTypeFilter, biomeFilter, isBiomeFilterBl, blockStateMatchers);
        } catch (Exception e) {
            Geolosys.getInstance().LOGGER.error("Failed to parse: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    public JsonElement serialize(TopLayerDeposit dep, JsonSerializationContext ctx) {
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
        config.addProperty("radius", this.radius);
        config.addProperty("depth", this.depth);
        config.addProperty("chanceForSample", this.sampleChance);
        config.addProperty("generationWeight", this.genWt);
        config.add("dimensions", dimensions);
        config.add("biomes", biomes);

        // Glue the two parts of this together.
        json.addProperty("type", JSON_TYPE);
        json.add("config", config);
        return json;
    }
}
