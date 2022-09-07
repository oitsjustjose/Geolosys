package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.world.IChunkGennedCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

public interface IDeposit {
    int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated);

    void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability deposits, IChunkGennedCapability chunksGenerated);

    HashSet<BlockState> getAllOres();

    int getGenWt();

    boolean canPlaceInBiome(Holder<Biome> biome);

    HashSet<BlockState> getBlockStateMatchers();
}
