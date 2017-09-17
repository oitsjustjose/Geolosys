package com.oitsjustjose.geolosys.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class HelperFunctions
{
    public static IBlockState getStateFromMeta(Block block, int meta)
    {
        try
        {
            return block.getStateForPlacement(null, null, EnumFacing.UP, 0F, 0F, 0F, meta, null, null);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

}
