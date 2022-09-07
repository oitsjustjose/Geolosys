package com.oitsjustjose.geolosys.common.utils;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class Utils {
    public static ItemStack blockStateToStack(BlockState state) {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2) {
        return getRegistryName(state1).equals(getRegistryName(state2));
    }

    public static String getRegistryName(Block block) {
        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).toString();
    }

    public static String getRegistryName(BlockState state) {
        return getRegistryName(state.getBlock());
    }

    public static BlockPos getTopSolidBlock(LevelReader world, BlockPos start) {
        BlockPos retPos = new BlockPos(start.getX(), world.getHeight() - 1, start.getZ());
        while (retPos.getY() > 0) {
            if (world.getBlockState(retPos).getMaterial().isSolid()) {
                break;
            }
            retPos = retPos.below();
        }
        return retPos;
    }

    public static MutableComponent tryTranslate(String transKey, Object... values) {
        try {
            TranslatableContents contents = new TranslatableContents(transKey, values);
            return contents.resolve(null, null, 0);
        } catch (CommandSyntaxException ex) {
            return Component.empty().append(transKey);
        }
    }
}
