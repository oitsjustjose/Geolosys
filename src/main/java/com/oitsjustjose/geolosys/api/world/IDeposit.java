package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.common.utils.Utils;
import net.minecraft.block.BlockState;

import java.util.List;

public interface IDeposit
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

    public String[] getDimensionBlacklist();

    public boolean canReplace(BlockState state);

    public boolean oreMatches(BlockState other);

    public boolean sampleMatches(BlockState other);

    public List<BlockState> getBlockStateMatchers();

    public float getDensity();
}