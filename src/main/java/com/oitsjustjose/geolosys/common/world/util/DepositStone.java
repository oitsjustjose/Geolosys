package com.oitsjustjose.geolosys.common.world.util;

import java.util.List;

import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class DepositStone implements IOre
{
    private IBlockState block;
    private int yMin;
    private int yMax;
    private int chance;
    private int size = 96;

    public DepositStone(IBlockState stoneBlock, int yMin, int yMax, int chance)
    {
        this.block = stoneBlock;
        this.yMin = yMin;
        this.yMax = yMax;
        this.chance = chance;
    }

    @Override
    public IBlockState getOre()
    {
        return this.block;
    }

    @Override
    public IBlockState getSample()
    {
        return null;
    }

    @Override
    public String getFriendlyName()
    {
        return new ItemStack(this.block.getBlock(), 1, this.block.getBlock().getMetaFromState(this.block))
                .getDisplayName();
    }

    @Override
    public int getYMin()
    {
        return this.yMin;
    }

    @Override
    public int getYMax()
    {
        return this.yMax;
    }

    @Override
    public int getChance()
    {
        return this.chance;
    }

    @Override
    public int getSize()
    {
        return this.size;
    }

    @Override
    public int[] getDimensionBlacklist()
    {
        return new int[]
        { -1, 1 };
    }

    @Override
    public boolean canReplace(IBlockState state)
    {
        for (IBlockState s : GeolosysAPI.replacementMats)
        {
            if (Utils.doStatesMatch(state, s))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<IBlockState> getBlockStateMatchers()
    {
        return null;
    }
}