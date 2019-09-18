package com.oitsjustjose.geolosys.common.api.world;

import java.util.List;

import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IOre
{
    public IBlockState getOre();

    public IBlockState getSample();

    public String getFriendlyName();

    public default String getFriendlyName(World world, BlockPos pos, EntityPlayer player)
    {
        return Utils.blockStateToName(this.getOre(), world, pos, player);
    }

    public int getYMin();

    public int getYMax();

    public int getChance();

    public int getSize();

    public int[] getDimensionBlacklist();

    public boolean canReplace(IBlockState state);

    public boolean oreMatches(IBlockState other);

    public boolean sampleMatches(IBlockState other);

    public List<IBlockState> getBlockStateMatchers();

    public float getDensity();
}