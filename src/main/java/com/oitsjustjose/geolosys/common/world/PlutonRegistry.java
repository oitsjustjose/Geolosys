package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.IDeposit;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.GenerationStep;
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

    private static final List<GenerationStep.Decoration> decorations = new LinkedList<>();
    static {
        decorations.add(GenerationStep.Decoration.UNDERGROUND_ORES);
        decorations.add(GenerationStep.Decoration.UNDERGROUND_DECORATION);
    }

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        BiomeGenerationSettingsBuilder gen = evt.getGeneration();

        // if (CommonConfig.REMOVE_VANILLA_ORES.get()) {
        // for (GenerationStep.Decoration stage : decorations) {
        // List<Supplier<PlacedFeature>> feats = gen.getFeatures(stage);
        // List<Supplier<PlacedFeature>> filtered = OreRemover.filterFeatures(feats);
        // for (Supplier<PlacedFeature> feature : filtered) {
        // feats.remove(feature);
        // }
        // }
        // }

        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, GeolosysFeatures.DEPOSITS_ALL_PLACED);
        gen.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, GeolosysFeatures.REMOVE_VEINS_ALL_PLACED);
    }
}
