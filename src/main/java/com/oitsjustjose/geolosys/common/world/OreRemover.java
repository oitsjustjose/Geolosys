package com.oitsjustjose.geolosys.common.world;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class OreRemover {
    private static List<Holder<PlacedFeature>> toRm = Arrays.asList(
            OrePlacements.ORE_GOLD_DELTAS,
            OrePlacements.ORE_QUARTZ_DELTAS,
            OrePlacements.ORE_GOLD_NETHER,
            OrePlacements.ORE_QUARTZ_NETHER,
            OrePlacements.ORE_GRANITE_UPPER,
            OrePlacements.ORE_GRANITE_LOWER,
            OrePlacements.ORE_DIORITE_UPPER,
            OrePlacements.ORE_DIORITE_LOWER,
            OrePlacements.ORE_ANDESITE_UPPER,
            OrePlacements.ORE_ANDESITE_LOWER,
            OrePlacements.ORE_COAL_UPPER,
            OrePlacements.ORE_COAL_LOWER,
            OrePlacements.ORE_IRON_UPPER,
            OrePlacements.ORE_IRON_MIDDLE,
            OrePlacements.ORE_IRON_SMALL,
            OrePlacements.ORE_GOLD_EXTRA,
            OrePlacements.ORE_GOLD,
            OrePlacements.ORE_GOLD_LOWER,
            OrePlacements.ORE_REDSTONE,
            OrePlacements.ORE_REDSTONE_LOWER,
            OrePlacements.ORE_DIAMOND,
            OrePlacements.ORE_DIAMOND_LARGE,
            OrePlacements.ORE_DIAMOND_BURIED,
            OrePlacements.ORE_LAPIS,
            OrePlacements.ORE_LAPIS_BURIED,
            OrePlacements.ORE_INFESTED,
            OrePlacements.ORE_EMERALD,
            OrePlacements.ORE_ANCIENT_DEBRIS_LARGE,
            OrePlacements.ORE_ANCIENT_DEBRIS_SMALL,
            OrePlacements.ORE_COPPER,
            OrePlacements.ORE_COPPER_LARGE);

    // Filters the features before sending em to the featureRemover()
    public static List<Holder<PlacedFeature>> filterFeatures(List<Holder<PlacedFeature>> features) {
        return features.parallelStream().filter(feature -> {
            for (Holder<PlacedFeature> ore : toRm) {
                try {
                    if (feature.is(ore.unwrap().orThrow())) {
                        return true;
                    }
                } catch (NoSuchElementException ex) {
                    Geolosys.getInstance().LOGGER.warn("Failed ot unwrapKey: {}", ex.getMessage());
                }
            }

            return false;
        }).toList();
    }
}
