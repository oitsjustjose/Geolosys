package com.oitsjustjose.geolosys.common.world.util;

import java.util.ArrayList;

import net.minecraft.block.state.IBlockState;

public interface IMultiOre extends IOre
{
    public ArrayList<IBlockState> getOres();

    public ArrayList<IBlockState> getSamples();
}