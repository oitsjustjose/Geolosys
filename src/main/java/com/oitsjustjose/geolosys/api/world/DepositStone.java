package com.oitsjustjose.geolosys.api.world;

import java.util.List;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;

public class DepositStone implements IOre
{
    private BlockState block;
    private int yMin;
    private int yMax;
    private int chance;
    private int size;
    private int[] dimBlacklist;

    public DepositStone(BlockState stoneBlock, int yMin, int yMax, int chance, int size, int[] dimBlacklist)
    {
        this.block = stoneBlock;
        this.yMin = yMin;
        this.yMax = yMax;
        this.chance = chance;
        this.size = size;
        this.dimBlacklist = dimBlacklist;
    }

    public int[] getDimensionBlacklist()
    {
        return this.dimBlacklist;
    }

    public BlockState getOre()
    {
        return this.block;
    }

    public BlockState getSample()
    {
        return null;
    }

    public int getYMin()
    {
        return this.yMin;
    }

    public int getYMax()
    {
        return this.yMax;
    }

    public int getChance()
    {
        return this.chance;
    }

    public int getSize()
    {
        return this.size;
    }

    public boolean canReplace(BlockState state)
    {
        for (BlockState s : GeolosysAPI.replacementMats)
        {
            if (Utils.doStatesMatch(state, s))
            {
                return true;
            }
        }
        return false;
    }

    public List<BlockState> getBlockStateMatchers()
    {
        return null;
    }

    public boolean oreMatches(BlockState other)
    {
        return Utils.doStatesMatch(other, this.block);
    }

    public boolean sampleMatches(BlockState other)
    {
        return true;
    }

    public float getDensity()
    {
        return 1.0F;
    }
}