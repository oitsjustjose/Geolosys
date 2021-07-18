package com.oitsjustjose.geolosys.common.utils;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

public class Utils {
    public static ItemStack blockStateToStack(BlockState state) {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2) {
        return (state1.getBlock().getRegistryName() == state2.getBlock().getRegistryName());
    }

    public static String dimensionToString(ISeedReader reader) {
        return reader.getWorld().getDimensionKey().getLocation().toString();
    }

    public static BlockPos getTopSolidBlock(IWorld world, BlockPos start) {
        BlockPos retPos = new BlockPos(start.getX(), world.getHeight() - 1, start.getZ());
        while (retPos.getY() > 0) {
            if (world.getBlockState(retPos).getMaterial().isSolid()) {
                break;
            }
            retPos = retPos.down();
        }
        return retPos;
    }

    public static boolean canMine(BlockState state, ItemStack stack) {
        int harvestLvl = stack.getHarvestLevel(ToolType.PICKAXE, null, null);
        return stack.getToolTypes().contains(ToolType.PICKAXE) && state.getHarvestLevel() <= harvestLvl;
    }
}