package com.oitsjustjose.geolosys.common.world.util;

import java.util.List;

import net.minecraft.block.state.IBlockState;

public interface IOre
{
    public IBlockState getOre();

    public IBlockState getSample();

    public String getFriendlyName();

    public int getYMin();

    public int getYMax();

    public int getChance();

    public int getSize();

    public int[] getDimensionBlacklist();

    public boolean canReplace(IBlockState state);

    public List<IBlockState> getBlockStateMatchers();
}