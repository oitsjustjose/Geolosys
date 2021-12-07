package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class GeolosysFeatures {
    public static final ConfiguredFeature<?, ?> DEPOSITS_ALL = new DepositFeature(
            NoneFeatureConfiguration.CODEC).configured(NoneFeatureConfiguration.NONE);
    public static PlacedFeature DEPOSITS_ALL_PLACED = DEPOSITS_ALL.placed(
            HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(384)));

    public static void init() {
        Registry<ConfiguredFeature<?, ?>> r = BuiltinRegistries.CONFIGURED_FEATURE;
        BuiltinRegistries.register(r, new ResourceLocation(Constants.MODID, "deposits"), DEPOSITS_ALL);
    }

}
