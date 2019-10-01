package com.oitsjustjose.geolosys.api.world;

import net.minecraft.block.BlockState;

import java.util.List;

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

    float getDensity();
}