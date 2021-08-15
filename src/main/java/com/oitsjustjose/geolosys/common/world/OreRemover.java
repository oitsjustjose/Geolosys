package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoExposedOreFeature;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.feature.ReplaceBlockFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

public class OreRemover {

    private static List<Block> toRm = Arrays.asList(Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRANITE,
            Blocks.INFESTED_STONE, Blocks.MAGMA_BLOCK, Blocks.BLACKSTONE, Blocks.BASALT);

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

    // Not used anymore, you may remove
    public static int process(BiomeGenerationSettingsBuilder settings) {
        Collection<Supplier<ConfiguredFeature<?, ?>>> toKeep = new ArrayList<Supplier<ConfiguredFeature<?, ?>>>();
        int total = 0;
        int newTotal = 0;

        for (Decoration stage : Decoration.values()) {
            int start = settings.getFeatures(stage).size();
            total += start;

            settings.getFeatures(stage).forEach((supp) -> {
                ConfiguredFeature<?, ?> configuredFeature = supp.get();
                IFeatureConfig config = configuredFeature.config;

                while (config instanceof DecoratedFeatureConfig) {
                    DecoratedFeatureConfig dconfig = (DecoratedFeatureConfig) config;
                    config = dconfig.feature.get().config;
                }

                if (config instanceof OreFeatureConfig) {
                    OreFeatureConfig conf = (OreFeatureConfig) config;
                    if (!toRm.contains(conf.state.getBlock())) {
                        toKeep.add(supp);
                    }
                } else if (config instanceof ReplaceBlockConfig) {
                    ReplaceBlockConfig conf = (ReplaceBlockConfig) config;
                    if (!toRm.contains(conf.state.getBlock())) {
                        toKeep.add(supp);
                    }
                } else {
                    toKeep.add(supp);
                }
            });

            settings.getFeatures(stage).clear();
            settings.getFeatures(stage).addAll(toKeep);

            newTotal += settings.getFeatures(stage).size();
        }

        return newTotal - total;
    }
}
