package com.oitsjustjose.geolosys.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

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

    public static String blockStateToName(IBlockState state, World world, BlockPos pos, EntityPlayer player)
    {
        RayTraceResult target = new RayTraceResult(player);
        return state.getBlock().getPickBlock(state, target, world, pos, player).getDisplayName();
    }

    public static String blockStateToName(IBlockState state)
    {
        return blockStateToStack(state).getDisplayName();
    }

    public static ItemStack blockStateToStack(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state));
    }

    public static boolean doStatesMatch(IBlockState state1, IBlockState state2)
    {
        return (state1.getBlock() == state2.getBlock()
                && state1.getBlock().getMetaFromState(state1) == state2.getBlock().getMetaFromState(state2));
    }

}
