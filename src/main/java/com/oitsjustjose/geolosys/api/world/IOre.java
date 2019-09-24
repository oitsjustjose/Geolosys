package com.oitsjustjose.geolosys.api.world;

import java.util.List;

import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;

public interface IOre
{
    public BlockState getOre();

    public BlockState getSample();

    public default String getFriendlyName()
    {
        return Utils.blockStateToName(this.getOre());
    }

    public int getYMin();

    public int getYMax();

    public int getChance();

    public int getSize();

    public int[] getDimensionBlacklist();

    public boolean canReplace(BlockState state);

    public boolean oreMatches(BlockState other);

    public boolean sampleMatches(BlockState other);

    public List<BlockState> getBlockStateMatchers();

    public float getDensity();
}