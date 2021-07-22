package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
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
    public IDeposit pick(ISeedReader reader, BlockPos pos, Random rand) {
        @SuppressWarnings("unchecked")
        ArrayList<IDeposit> choices = (ArrayList<IDeposit>) this.deposits.clone();
        // Dimension Filtering done here!
        choices.removeIf((dep) -> {
            ResourceLocation dim = reader.getWorld().getDimensionKey().getLocation();
            boolean isDimFilterBl = dep.isDimensionFilterBl();
            for (String dim2Raw : dep.getDimensionFilter()) {
                boolean match = new ResourceLocation(dim2Raw).equals(dim);
                if (match && !isDimFilterBl || !match && isDimFilterBl) {
                    return false;
                }
            }
            return true;
        });

        if (choices.size() == 0) {
            return null;
        }

        /* 1/3 chance to lean towards a biome restricted deposit */
        if (rand.nextInt(3) > 1) {
            // Only remove the entries if there's at least one.
            if (choices.stream().anyMatch((dep) -> {
                return dep.hasBiomeRestrictions() && dep.canPlaceInBiome(reader.getBiome(pos));
            })) {
                choices.removeIf((dep) -> {
                    return !(dep.hasBiomeRestrictions() && dep.canPlaceInBiome(reader.getBiome(pos)));
                });
            }
        }

        int totalWt = 0;
        for (IDeposit d : choices) {
            totalWt += d.getGenWt();
        }

        int rng = rand.nextInt(totalWt);
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
    private static final List<GenerationStage.Decoration> decorations = new LinkedList<>();
    static {
        decorations.add(GenerationStage.Decoration.UNDERGROUND_ORES);
        decorations.add(GenerationStage.Decoration.UNDERGROUND_DECORATION);
    }

    @SubscribeEvent
    public void onBiomesLoaded(BiomeLoadingEvent evt) {
        BiomeGenerationSettingsBuilder settings = evt.getGeneration();

        // Removes vanilla ores
        if (CommonConfig.REMOVE_VANILLA_ORES.get()) {
            for (GenerationStage.Decoration deco : decorations) {
                FeatureUtils.destroyFeature(settings.getFeatures(deco),
                        OreRemover.filterFeatures(settings.getFeatures(deco)));
            }
        }

        DepositFeature o = new DepositFeature(NoFeatureConfig.field_236558_a_);

        settings.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                o.withConfiguration(NoFeatureConfig.field_236559_b_));
    }
}