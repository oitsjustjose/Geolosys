package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;

public class OreRemover {
    private static List<Block> toRm = Arrays.asList(Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRANITE);

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
