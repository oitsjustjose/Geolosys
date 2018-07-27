package com.oitsjustjose.geolosys.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class Utils
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

    public static ItemStack blockStateToStack(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state));
    }

    public static boolean doStatesMatch(IBlockState state1, IBlockState state2)
    {
        return (state1.getBlock() == state2.getBlock() && state1.getBlock().getMetaFromState(state1) == state2.getBlock().getMetaFromState(state2));
    }
}
