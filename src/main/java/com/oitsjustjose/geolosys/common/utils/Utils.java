package com.oitsjustjose.geolosys.common.utils;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

public class Utils
{
    public static ItemStack blockStateToStack(BlockState state)
    {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2)
    {
        return (state1.getBlock() == state2.getBlock());
    }
}