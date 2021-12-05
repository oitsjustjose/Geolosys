package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

public interface IDeposit {
    public int generate(WorldGenLevel level, BlockPos pos, IDepositCapability cap);

    public void afterGen(WorldGenLevel level, BlockPos pos, IDepositCapability cap);

    public HashSet<BlockState> getAllOres();

    public int getGenWt();

    public boolean canPlaceInBiome(Biome biome);

    public boolean hasBiomeRestrictions();

    public String[] getDimensionFilter();

    public boolean isDimensionFilterBl();

    public HashSet<BlockState> getBlockStateMatchers();
}
