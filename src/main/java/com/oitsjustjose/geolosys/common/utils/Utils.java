package com.oitsjustjose.geolosys.common.utils;

import java.util.ArrayList;
import java.util.Objects;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

public class Utils {
    private static ArrayList<BlockState> defaultMatchersCached = null;

    public static String blockStateToName(BlockState state) {
        return I18n.format(state.getBlock().getTranslationKey());
    }

    public static ItemStack blockStateToStack(BlockState state) {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2) {
        return (state1.getBlock().getRegistryName() == state2.getBlock().getRegistryName());
    }

    public static String dimensionToString(IWorld world) {
        if (world instanceof World) {
            return Objects.requireNonNull(((World) world).getDimensionKey().getLocation().toString());
        } else if (world instanceof ServerWorld) {
            return Objects.requireNonNull(((ServerWorld) world).getDimensionKey().getLocation().toString());
        } else if (world instanceof WorldGenRegion) {
            return Objects
                    .requireNonNull(((WorldGenRegion) world).getWorld().getDimensionKey().getLocation().toString());
        }
        Geolosys.getInstance().LOGGER
                .warn("Utils.dimensionToString called on IWorld object that couldn't be interpreted");
        return "ERR";
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

    @SuppressWarnings("unchecked")
    public static ArrayList<BlockState> getDefaultMatchers() {
        return (ArrayList<BlockState>) defaultMatchersCached.clone();
    }

    @SuppressWarnings("deprecation")
    public static boolean addDefaultMatcher(Block block) {
        if (defaultMatchersCached == null) {
            defaultMatchersCached = new ArrayList<BlockState>();
            GeolosysAPI.plutonRegistry.getStones().forEach(x -> defaultMatchersCached.add(x.getOre()));
        }
        BlockState defaultState = block.getDefaultState();
        if (!defaultState.isAir()) {
            defaultMatchersCached.add(defaultState);
            return true;
        }
        return false;
    }

    public static boolean canMine(BlockState state, ItemStack stack) {
        int harvestLvl = stack.getHarvestLevel(ToolType.PICKAXE, null, null);
        return stack.getToolTypes().contains(ToolType.PICKAXE) && state.getHarvestLevel() <= harvestLvl;
    }
}