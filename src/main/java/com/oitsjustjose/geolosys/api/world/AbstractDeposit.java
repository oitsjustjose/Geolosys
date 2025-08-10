package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.world.IChunkGennedCapability;
import com.oitsjustjose.geolosys.common.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class AbstractDeposit {

    protected final HashMap<String, HashMap<BlockState, Float>> matcherToOreWeightPair;
    protected final HashMap<BlockState, Float> sampleWeightPair;
    protected final TagKey<Biome> biomeKey;
    protected final HashSet<BlockState> matchers;
    protected final int generationWeight;

    public AbstractDeposit(
            HashMap<String, HashMap<BlockState, Float>> matcherToOreWeightPair,
            HashMap<BlockState, Float> sampleWeightPair,
            TagKey<Biome> biomeKey,
            HashSet<BlockState> matchers,
            int generationWeight
    ) {
        this.matcherToOreWeightPair = matcherToOreWeightPair;
        this.sampleWeightPair = sampleWeightPair;
        this.biomeKey = biomeKey;
        this.matchers = matchers;
        this.generationWeight = generationWeight;
    }

    /**
     * Uses {@link DepositUtils#pick(HashMap, RandomSource)} to find a random ore block to
     * return.
     *
     * @return the random ore block chosen (based on weight) Can be null to
     * represent "density" of the ore -- null results should be used to
     * determine if the block in the world should be replaced. If null,
     * don't replace 😉
     */
    @Nullable
    public BlockState getOre(BlockState currentState, RandomSource rand) {
        String res = Utils.getRegistryName(currentState);
        if (this.matcherToOreWeightPair.containsKey(res)) {
            // Return a choice from a specialized set here
            HashMap<BlockState, Float> mp = this.matcherToOreWeightPair.get(res);
            return DepositUtils.pick(mp, rand);
        }
        return DepositUtils.pick(this.matcherToOreWeightPair.get("default"), rand);
    }

    /**
     * Gets a flat set of all Ore Blocks for this deposit
     *
     * @return Null if there are no ores in this deposit, otherwise a Set of the ores for all materials
     */
    @Nullable
    public HashSet<BlockState> getAllOres() {
        HashSet<BlockState> ret = new HashSet<>();
        this.matcherToOreWeightPair.values().forEach(x -> ret.addAll(x.keySet()));
        ret.remove(Blocks.AIR.defaultBlockState());
        return ret.isEmpty() ? null : ret;
    }

    /**
     * Uses {@link DepositUtils#pick(HashMap, RandomSource)} to find a random pluton sample
     * to return.
     *
     * @return the random pluton sample chosen (based on weight) Can be null to
     * represent "density" of the samples -- null results should be used to
     * determine if the sample in the world should be replaced. If null,
     * don't replace 😉
     */
    @Nullable
    public BlockState getSample(RandomSource rand) {
        return DepositUtils.pick(this.sampleWeightPair, rand);
    }

    public int getGenerationWeight() {
        return this.generationWeight;
    }

    public boolean canPlaceInBiome(Holder<Biome> b) {
        return b.is(this.biomeKey);
    }

    public HashSet<BlockState> getMatchers() {
        return this.matchers == null ? DepositUtils.getDefaultMatchers() : this.matchers;
    }

    protected void validate(HashMap<String, HashMap<BlockState, Float>> oreBlocks, HashMap<BlockState, Float> sampleBlocks) {
        // Verify that blocks.default exists.
        if (!oreBlocks.containsKey("default")) {
            throw new RuntimeException("Pluton blocks should always have a default key");
        }

        var cumulOreWtMap = new HashMap<String, Float>();
        for (Map.Entry<String, HashMap<BlockState, Float>> i : oreBlocks.entrySet()) {
            if (!cumulOreWtMap.containsKey(i.getKey())) {
                cumulOreWtMap.put(i.getKey(), 0.0F);
            }

            for (Map.Entry<BlockState, Float> j : i.getValue().entrySet()) {
                cumulOreWtMap.compute(i.getKey(), (k, v) -> (v == null ? 0 : v) + j.getValue());
            }

            if (!DepositUtils.nearlyEquals(cumulOreWtMap.get(i.getKey()), 1.0F)) {
                throw new RuntimeException("Sum of weights for pluton blocks should equal 1.0");
            }
        }

        var sumWtSamples = 0.0F;
        for (Map.Entry<BlockState, Float> e : sampleBlocks.entrySet()) {
            sumWtSamples += e.getValue();
        }

        if (sumWtSamples != 0.0F /* (allow empty samples) */ && !DepositUtils.nearlyEquals(sumWtSamples, 1.0F)) {
            throw new RuntimeException("Sum of weights for pluton samples should equal 1.0");
        }
    }

    /**
     * Handles full-on generation of this type of pluton. Requires 0 arguments as
     * everything is self-contained in this class
     *
     * @return (int) the number of pluton resource blocks placed. If 0 -- this
     * should be evaluted as a false for use of Mojang's sort-of sketchy
     * generation code in
     */
    public abstract int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated);

    /**
     * Handles what to do after the world has generated
     */
    public abstract void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated);
}
