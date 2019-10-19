package com.oitsjustjose.geolosys.api.world;

import java.util.List;

import com.oitsjustjose.geolosys.common.world.feature.PlutonType;

import net.minecraft.block.BlockState;

public interface IDeposit
{
    BlockState getOre();

    BlockState getSample();

    String getFriendlyName();

    int getYMin();

    int getYMax();

    int getChance();

    int getSize();

    String[] getDimensionBlacklist();

    boolean canReplace(BlockState state);

    boolean oreMatches(BlockState other);

    boolean sampleMatches(BlockState other);

    List<BlockState> getBlockStateMatchers();

    PlutonType getPlutonType();

    float getDensity();
}