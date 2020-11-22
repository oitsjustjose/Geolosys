package com.oitsjustjose.geolosys.common.world;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FeatureStripper {
    private static final BlockState[] match = new BlockState[] { Blocks.ANDESITE.getDefaultState(),
            Blocks.DIORITE.getDefaultState(), Blocks.GRANITE.getDefaultState(), Blocks.COAL_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(), Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(), Blocks.IRON_ORE.getDefaultState(), Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.NETHER_QUARTZ_ORE.getDefaultState(), Blocks.REDSTONE_ORE.getDefaultState() };

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        // BiomeGenerationSettingsBuilder settings = evt.getGeneration();

        // List<Supplier<ConfiguredFeature<?, ?>>> features = settings
        //         .getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES);

    }
}