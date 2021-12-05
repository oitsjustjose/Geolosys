package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class PlutonRegistry {
    private ArrayList<IDeposit> deposits;

    public PlutonRegistry() {
        this.deposits = new ArrayList<>();
    }

    public void clear() {
        this.deposits = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<IDeposit> getOres() {
        return (ArrayList<IDeposit>) this.deposits.clone();
    }

    public boolean addDeposit(IDeposit ore) {
        return this.deposits.add(ore);
    }

    @Nullable
    public IDeposit pick(WorldGenLevel level, BlockPos pos) {
        @SuppressWarnings("unchecked")
        ArrayList<IDeposit> choices = (ArrayList<IDeposit>) this.deposits.clone();
        // Dimension Filtering done here!
        choices.removeIf((dep) -> {
            ResourceLocation dim = level.getLevel().dimension().location();
            boolean isDimFilterBl = dep.isDimensionFilterBl();
            for (String dim2Raw : dep.getDimensionFilter()) {
                boolean match = new ResourceLocation(dim2Raw).equals(dim);
                if ((isDimFilterBl && match) || (!isDimFilterBl && !match)) {
                    return true;
                }
            }
            return false;
        });

        if (choices.size() == 0) {
            return null;
        }

        /* 1/3 chance to lean towards a biome restricted deposit */
        if (level.getRandom().nextInt(3) == 0) {
            // Only remove the entries if there's at least one.
            if (choices.stream()
                    .anyMatch((dep) -> dep.hasBiomeRestrictions() && dep.canPlaceInBiome(level.getBiome(pos)))) {
                choices.removeIf((dep) -> !(dep.hasBiomeRestrictions() && dep.canPlaceInBiome(level.getBiome(pos))));
            }
        }

        int totalWt = 0;
        for (IDeposit d : choices) {
            totalWt += d.getGenWt();
        }

        int rng = level.getRandom().nextInt(totalWt);
        for (IDeposit d : choices) {
            int wt = d.getGenWt();
            if (rng < wt) {
                return d;
            }
            rng -= wt;
        }

        Geolosys.getInstance().LOGGER.error("Could not reach decision on pluton to generate at PlutonRegistry#pick");
        return null;
    }

    // Collects UNDERGROUND_ORES & UNDERGROUND_DECORATION to make it easier to
    // iterate
    private static final List<GenerationStep.Decoration> decorations = new LinkedList<>();
    static {
        decorations.add(GenerationStep.Decoration.UNDERGROUND_ORES);
        decorations.add(GenerationStep.Decoration.UNDERGROUND_DECORATION);
    }

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        BiomeGenerationSettingsBuilder settings = evt.getGeneration();

        // Removes vanilla ores
        // if (CommonConfig.REMOVE_VANILLA_ORES.get()) {
        // for (GenerationStep.Decoration deco : decorations) {
        // FeatureUtils.destroyFeature(settings.getFeatures(deco),
        // OreRemover.filterFeatures(settings.getFeatures(deco)));
        // }
        // }

        PlacedFeature feature = new DepositFeature(NoneFeatureConfiguration.CODEC)
                .configured(NoneFeatureConfiguration.NONE)
                .placed(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(384)));
        // TODO: Try out these other ones?
        // .placed(List.of(RarityFilter.onAverageOnceEvery(6),
        // InSquarePlacement.spread(), y, BiomeFilter.biome()));
        settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);

        // public static final PlacedFeature ORE_GRANITE_UPPER =
        // PlacementUtils.register("ore_granite_upper",
        // OreFeatures.ORE_GRANITE.placed(rareOrePlacement(6,
        // HeightRangePlacement.uniform(VerticalAnchor.absolute(64),
        // VerticalAnchor.absolute(128)))));
        // private static List<PlacementModifier> orePlacement(PlacementModifier
        // p_195347_, PlacementModifier p_195348_) {
        // return List.of(p_195347_, InSquarePlacement.spread(), p_195348_,
        // BiomeFilter.biome());
        // }

        // private static List<PlacementModifier> rareOrePlacement(int p_195350_,
        // PlacementModifier p_195351_) {
        // return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
        // }
    }
}
