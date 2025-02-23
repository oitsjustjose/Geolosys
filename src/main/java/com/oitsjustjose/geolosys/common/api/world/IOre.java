package com.oitsjustjose.geolosys.common.api.world;

import com.oitsjustjose.geolosys.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IOre {
    IBlockState getOre();

    IBlockState getSample();

    String getFriendlyName();

    default String getFriendlyName(World world, BlockPos pos, EntityPlayer player) {
        return Utils.blockStateToName(this.getOre(), world, pos, player);
    }

    int getYMin();

    int getYMax();

    int getChance();

    int getSize();

    int[] getDimensionBlacklist();

    boolean canReplace(IBlockState state);

    boolean oreMatches(IBlockState other);

    boolean sampleMatches(IBlockState other);

    List<IBlockState> getBlockStateMatchers();

    float getDensity();
}