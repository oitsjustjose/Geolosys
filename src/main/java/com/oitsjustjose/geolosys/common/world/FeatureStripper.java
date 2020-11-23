package com.oitsjustjose.geolosys.common.world;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FeatureStripper {
    private static final List<Feature<? extends IFeatureConfig>> matches = Arrays.asList(
            Features.ORE_ANDESITE.getFeature(), Features.ORE_DIORITE.getFeature(), Features.ORE_GRANITE.getFeature(),
            Features.ORE_COAL.getFeature(), Features.ORE_DIAMOND.getFeature(), Features.ORE_EMERALD.getFeature(),
            Features.ORE_GOLD.getFeature(), Features.ORE_IRON.getFeature(), Features.ORE_LAPIS.getFeature(),
            Features.ORE_DEBRIS_SMALL.getFeature(), Features.ORE_DEBRIS_LARGE.getFeature(),
            Features.ORE_GOLD_NETHER.getFeature(), Features.ORE_QUARTZ_NETHER.getFeature(),
            Features.ORE_QUARTZ_DELTAS.getFeature());

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        BiomeGenerationSettingsBuilder settings = evt.getGeneration();

        List<Supplier<ConfiguredFeature<?, ?>>> features = settings
                .getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES);

        features.forEach(supp -> {
            if (supp.get().getFeature() instanceof OreFeature) {
                Geolosys.getInstance().LOGGER.info("Found an ore :)");
            }

            // if (supp.get().getFeature()) {
            Geolosys.getInstance().LOGGER.info(supp.get().getConfig().getClass());
            // }

            // boolean ret = matches.includes((Feature<? extends IFeatureConfig>)
            // supp.get().getFeature());
            // Geolosys.getInstance().LOGGER.info()
            // Features.ORE_COAL.getFeature());
        });
    }
}