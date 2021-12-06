package com.oitsjustjose.geolosys.common.world;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class OreRemover {

    private static List<Block> toRm = Arrays.asList(Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.LAPIS_ORE,
            Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.NETHER_QUARTZ_ORE,
            Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS, Blocks.DIORITE, Blocks.ANDESITE, Blocks.GRANITE,
            Blocks.INFESTED_STONE);

    // Validates, removes and logs each feature
    private static List<ConfiguredFeature<?, ?>> featureRemover(Block targetBlock,
            ConfiguredFeature<?, ?> targetFeature) {
        List<ConfiguredFeature<?, ?>> removed = new LinkedList<ConfiguredFeature<?, ?>>();

        if (targetBlock != null) {
            if (toRm.contains(targetBlock)) {
                removed.add(targetFeature);
                if (CommonConfig.ADVANCED_DEBUG_WORLD_GEN.get()) {
                    Geolosys.getInstance().LOGGER.debug("{} removed from worldgen", targetBlock.getRegistryName());
                }
            }
        }
        return removed;
    }

    // Filters the features before sending em to the featureRemover()
    public static List<Supplier<PlacedFeature>> filterFeatures(List<Supplier<PlacedFeature>> features) {
        List<Supplier<PlacedFeature>> removed = new LinkedList<Supplier<PlacedFeature>>();
        for (Supplier<PlacedFeature> feature : features) {
            feature.get().getFeatures().forEach((confFeat) -> {
                List<OreConfiguration.TargetBlockState> targets = null;
                if (confFeat.config instanceof OreConfiguration) {
                    targets = ((OreConfiguration) confFeat.config).targetStates;
                } else if (confFeat.config instanceof ReplaceBlockConfiguration) {
                    targets = ((ReplaceBlockConfiguration) confFeat.config).targetStates;
                }

                if (targets != null) {
                    targets.forEach((x) -> featureRemover(x.state.getBlock(), confFeat));
                    removed.add(feature);
                }
            });
        }

        return removed;
    }
}
