package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.RemoveVeinsFeature;

import net.minecraft.resources.ResourceLocation;
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

    public static final ConfiguredFeature<?, ?> DEPOSITS_ALL = DEPOSIT_FEATURE
            .configured(NoneFeatureConfiguration.NONE);
    public static final ConfiguredFeature<?, ?> REMOVE_VEINS_ALL = REMOVE_VEIN_FEATURE
            .configured(NoneFeatureConfiguration.NONE);

    public static PlacedFeature DEPOSITS_ALL_PLACED = DEPOSITS_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));
    public static PlacedFeature REMOVE_VEINS_ALL_PLACED = REMOVE_VEINS_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64),
                    VerticalAnchor.absolute(320)));

    public static void register(final RegistryEvent.Register<Feature<?>> featureRegistryEvent) {
        featureRegistryEvent.getRegistry().register(DEPOSIT_FEATURE);
        featureRegistryEvent.getRegistry().register(REMOVE_VEIN_FEATURE);
    }
}
