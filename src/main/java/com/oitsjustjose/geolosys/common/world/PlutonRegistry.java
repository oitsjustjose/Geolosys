package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.GenerationStep;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public void addDeposit(IDeposit ore) {
        this.deposits.add(ore);
    }

    @Nullable
    public IDeposit pick(WorldGenLevel level, BlockPos pos) {
        @SuppressWarnings("unchecked")
        ArrayList<IDeposit> choices = (ArrayList<IDeposit>) this.deposits.clone();
        // Dimension Filtering done here!
        choices.removeIf((dep) -> !dep.canPlaceInBiome(level.getBiome(pos)));

        if (choices.size() == 0) {
            return null;
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

//    private static final List<GenerationStep.Decoration> decorations = new LinkedList<>();
//
//    static {
//        decorations.add(GenerationStep.Decoration.UNDERGROUND_ORES);
//        decorations.add(GenerationStep.Decoration.UNDERGROUND_DECORATION);
//    }

//    @SubscribeEvent
//    public void onBiomesLoaded(BiomeLoadingEvent evt) {
//        BiomeGenerationSettingsBuilder gen = evt.getGeneration();
//
//        if (CommonConfig.REMOVE_VANILLA_ORES.get()) {
//            for (GenerationStep.Decoration stage : decorations) {
//                List<Holder<PlacedFeature>> feats = gen.getFeatures(stage);
//                List<Holder<PlacedFeature>> filtered = OreRemover.filterFeatures(feats);
//                Geolosys.getInstance().LOGGER.info("Removing {} Vanilla Ore Entries", filtered.size());
//                for (Holder<PlacedFeature> feature : filtered) {
//                    feats.remove(feature);
//                }
//            }
//        }
//
//        gen.addFeature(GenerationStep.Decoration.RAW_GENERATION, GeolosysFeatures.DEPOSITS_PLACED);
//        gen.addFeature(GenerationStep.Decoration.RAW_GENERATION, GeolosysFeatures.REMOVE_VEINS_PLACED);
//    }
}
