package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.RemoveVeinsFeature;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.event.RegistryEvent;

public class GeolosysFeatures {
    private static final Feature<NoneFeatureConfiguration> DEPOSIT_FEATURE = new DepositFeature(
            NoneFeatureConfiguration.CODEC).withRegistryName(Constants.MODID, "deposits");
    private static final Feature<NoneFeatureConfiguration> REMOVE_VEIN_FEATURE = new RemoveVeinsFeature(
            NoneFeatureConfiguration.CODEC).withRegistryName(Constants.MODID, "remove_veins");

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> DEPOSITS_ALL = FeatureUtils
            .register(DEPOSIT_FEATURE.getRegistryName().toString(), DEPOSIT_FEATURE);
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> REMOVE_VEINS_ALL = FeatureUtils
            .register(REMOVE_VEIN_FEATURE.getRegistryName().toString(), REMOVE_VEIN_FEATURE);

    public static Holder<PlacedFeature> DEPOSITS_ALL_PLACED = PlacementUtils.register(
            DEPOSIT_FEATURE.getRegistryName().toString(), DEPOSITS_ALL,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));

    public static Holder<PlacedFeature> REMOVE_VEINS_ALL_PLACED = PlacementUtils.register(
            REMOVE_VEIN_FEATURE.getRegistryName().toString(), REMOVE_VEINS_ALL,
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));

    public static void register(final RegistryEvent.Register<Feature<?>> featureRegistryEvent) {
        featureRegistryEvent.getRegistry().register(DEPOSIT_FEATURE);
        featureRegistryEvent.getRegistry().register(REMOVE_VEIN_FEATURE);
    }
}
