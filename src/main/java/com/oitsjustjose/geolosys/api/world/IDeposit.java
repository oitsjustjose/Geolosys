package com.oitsjustjose.geolosys.api.world;

import java.util.HashSet;

import com.oitsjustjose.geolosys.api.PlutonType;

import net.minecraft.block.BlockState;

public interface IDeposit {
    BlockState getOre();

    BlockState getSampleBlock();

    int getYMin();

    int getYMax();

    int getChance();

    int getSize();

    String[] getDimensionFilter();

    boolean isDimensionFilterBlacklist();

    boolean canReplace(BlockState state);

    boolean oreMatches(BlockState other);

    boolean sampleMatches(BlockState other);

    HashSet<BlockState> getBlockStateMatchers();

    PlutonType getPlutonType();

    float getDensity();
}