package com.oitsjustjose.geolosys.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.api.world.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.api.world.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.api.world.DepositStone;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils {
    private static HashSet<BlockState> defaultMatchersCached = null;

    public static ItemStack blockStateToStack(BlockState state) {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2) {
        return (state1.getBlock().getRegistryName() == state2.getBlock().getRegistryName());
    }

    @SuppressWarnings("deprecation")
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
    public static HashSet<BlockState> getDefaultMatchers() {
        // If the cached data isn't there yet, load it.
        if (defaultMatchersCached == null) {
            defaultMatchersCached = new HashSet<BlockState>();
            GeolosysAPI.plutonRegistry.getStones().forEach(x -> defaultMatchersCached.add(x.getOre()));

            CommonConfig.DEFAULT_REPLACEMENT_MATS.get().forEach(s -> {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
                if (block == null || !addDefaultMatcher(block)) {
                    Geolosys.getInstance().LOGGER.warn("{} is not a valid block. Please verify.", s);
                }
            });
        }

        return (HashSet<BlockState>) defaultMatchersCached.clone();
    }

    @SuppressWarnings("deprecation")
    public static boolean addDefaultMatcher(Block block) {
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

    public static void logDeposit(IDeposit dep) {
        if (dep instanceof DepositStone) {
            Geolosys.getInstance().LOGGER.info("Registered a {} stone pluton.",
                    dep.getOre().getBlock().getRegistryName());
        } else if (dep instanceof DepositMultiOre) {
            Geolosys.getInstance().LOGGER.info(
                    "Registered a {} ore pluton with blocks={}, samples={}, and density={}. This ore {} custom biome registries.",
                    dep.getPlutonType().toString().toLowerCase(), ((DepositMultiOre) dep).oreBlocks,
                    ((DepositMultiOre) dep).sampleBlocks, dep.getDensity(),
                    (dep instanceof DepositMultiOreBiomeRestricted) ? "has" : "does not have");
        } else {
            Geolosys.getInstance().LOGGER.info(
                    "Registered a {} {} ore pluton with sample {}, and density={}. This ore {} custom biome registries.",
                    dep.getPlutonType().toString().toLowerCase(), dep.getOre().getBlock().getRegistryName(),
                    dep.getSampleBlock().getBlock().getRegistryName(), dep.getDensity(),
                    (dep instanceof DepositBiomeRestricted) ? "has" : "does not have");
        }
    }
}