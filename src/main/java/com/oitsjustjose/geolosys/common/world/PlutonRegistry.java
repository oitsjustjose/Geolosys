package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
        if (CommonConfig.REMOVE_VANILLA_ORES.get()) {
            for (GenerationStep.Decoration deco : decorations) {
                List<Supplier<PlacedFeature>> feats = settings.getFeatures(deco);
                FeatureUtils.destroyFeature(feats, OreRemover.filterFeatures(feats));
            }
        }

        ConfiguredFeature<NoneFeatureConfiguration, ?> depositFeature = new DepositFeature(
                NoneFeatureConfiguration.CODEC).configured(NoneFeatureConfiguration.NONE);

        net.minecraft.data.worldgen.features.FeatureUtils.register(
                String.format("geolosys:%s_%s_deposits", evt.getName().getNamespace(), evt.getName().getPath()),
                depositFeature);

        PlacedFeature feature = depositFeature.placed(
                HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(384)));
        settings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, feature);
    }
}
