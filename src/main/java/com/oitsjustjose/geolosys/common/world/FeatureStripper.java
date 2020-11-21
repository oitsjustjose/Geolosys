package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureStripper {
    private static final BlockState[] match = new BlockState[] { Blocks.ANDESITE.getDefaultState(),
            Blocks.DIORITE.getDefaultState(), Blocks.GRANITE.getDefaultState(), Blocks.COAL_ORE.getDefaultState(),
            Blocks.DIAMOND_ORE.getDefaultState(), Blocks.EMERALD_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(), Blocks.IRON_ORE.getDefaultState(), Blocks.LAPIS_ORE.getDefaultState(),
            Blocks.NETHER_QUARTZ_ORE.getDefaultState(), Blocks.REDSTONE_ORE.getDefaultState() };

    public static void strip() {
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES).removeIf((x) -> {
                if (x.config instanceof DecoratedFeatureConfig) {
                    DecoratedFeatureConfig decConf = (DecoratedFeatureConfig) x.config;
                    // Handles most ores
                    if (decConf.feature.config instanceof OreFeatureConfig) {
                        OreFeatureConfig featureConf = (OreFeatureConfig) decConf.feature.config;

                        for (BlockState state2 : match) {
                            if (Utils.doStatesMatch(featureConf.state, state2)) {
                                return true;
                            }
                        }
                    }
                    // Handles extra emeralds in mountains
                    else if (decConf.feature.config instanceof ReplaceBlockConfig) {
                        ReplaceBlockConfig featureConf = (ReplaceBlockConfig) decConf.feature.config;

                        for (BlockState state2 : match) {
                            if (Utils.doStatesMatch(featureConf.state, state2)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            });
            // Handles Nether Ores
            biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_DECORATION).removeIf((x) -> {
                if (x.config instanceof DecoratedFeatureConfig) {
                    DecoratedFeatureConfig decConf = (DecoratedFeatureConfig) x.config;
                    if (decConf.feature.config instanceof OreFeatureConfig) {
                        OreFeatureConfig featureConf = (OreFeatureConfig) decConf.feature.config;

                        for (BlockState state2 : match) {
                            if (Utils.doStatesMatch(featureConf.state, state2)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            });
        }
    }
}