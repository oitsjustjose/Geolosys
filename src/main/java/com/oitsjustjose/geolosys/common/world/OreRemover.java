package com.oitsjustjose.geolosys.common.world;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.NoExposedOreFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.ReplaceBlockFeature;

public class OreRemover {

    private static List<Block> toRm = Arrays.asList(Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRANITE,
            Blocks.INFESTED_STONE);

    // List of removed features
    public static List<Supplier<ConfiguredFeature<?, ?>>> removed = new LinkedList<>();

    // Validates, removes and logs each feature
    private static void featureRemover(Block targetBlock, Supplier<ConfiguredFeature<?, ?>> targetFeature) {
        if (targetBlock != null) {
            if (toRm.contains(targetBlock)) {
                removed.add(targetFeature);
                if (CommonConfig.ADVANCED_DEBUG_WORLD_GEN.get()) {
                    Geolosys.getInstance().LOGGER.debug("{} removed from worldgen", targetBlock.getRegistryName());
                }
            }
        }
    }

    // Filters the features before sending em to the featureRemover()
    public static List<Supplier<ConfiguredFeature<?, ?>>> filterFeatures(
            List<Supplier<ConfiguredFeature<?, ?>>> features) {
        for (Supplier<ConfiguredFeature<?, ?>> feature : features) {
            Block targetBlock = null;
            ConfiguredFeature<?, ?> targetFeature = FeatureUtils.getFeature(feature.get());

            if (targetFeature.feature instanceof OreFeature || targetFeature.feature instanceof NoExposedOreFeature) {
                // Normal ores
                OreFeatureConfig config = (OreFeatureConfig) targetFeature.config;
                targetBlock = config.state.getBlock();
                featureRemover(targetBlock, feature);
            } else if (targetFeature.feature instanceof ReplaceBlockFeature) {
                // Emeralds
                ReplaceBlockConfig config = (ReplaceBlockConfig) targetFeature.config;
                targetBlock = config.state.getBlock();
                featureRemover(targetBlock, feature);
            }

        }
        return removed;
    }
}
