package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.world.IChunkGennedCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public interface IDeposit {
    int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated);

    void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated);

    HashSet<BlockState> getAllOres();

    int getGenWt();

    boolean canPlaceInBiome(Holder<Biome> biome);

    HashSet<BlockState> getBlockStateMatchers();

    default void validate(HashMap<String, HashMap<BlockState, Float>> oreBlocks, HashMap<BlockState, Float> sampleBlocks) {
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
                cumulOreWtMap.compute(i.getKey(), (k, v) -> v + j.getValue());
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
}
