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
    public int generate(WorldGenLevel level, BlockPos pos, IDepositCapability deposits,
            IChunkGennedCapability chunksGenerated);

    public void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability deposits,
            IChunkGennedCapability chunksGenerated);

    public HashSet<BlockState> getAllOres();

    public int getGenWt();

    public boolean canPlaceInBiome(Holder<Biome> biome);

    public boolean hasBiomeRestrictions();

    public String[] getDimensionFilter();

    public boolean isDimensionFilterBl();

    public HashSet<BlockState> getBlockStateMatchers();
}
