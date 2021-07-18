package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.world.feature.FeatureUtils;
import com.oitsjustjose.geolosys.common.world.feature.DepositFeature;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlutonRegistry {
    private ArrayList<IDeposit> deposits;
    private ArrayList<IDeposit> depositWtMap;

    public PlutonRegistry() {
        this.deposits = new ArrayList<>();
        this.depositWtMap = new ArrayList<>();
    }

    public void clear() {
        this.deposits = new ArrayList<>();
        this.depositWtMap = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<IDeposit> getOres() {
        return (ArrayList<IDeposit>) this.deposits.clone();
    }

    public boolean addDeposit(IDeposit ore) {
        for (int i = 0; i < ore.getGenWt(); i++) {
            depositWtMap.add(ore);
        }

        return this.deposits.add(ore);
    }

    public IDeposit pickPluton(ISeedReader reader, BlockPos pos, Random rand) {
        if (this.depositWtMap.size() > 0) {
            // Sometimes bias towards specific biomes where applicable
            if (rand.nextBoolean()) {
                Biome b = reader.getBiome(pos);
                ArrayList<IDeposit> forBiome = new ArrayList<>();
                for (IDeposit d : this.deposits) {
                    if (d.canPlaceInBiome(b)) {
                        forBiome.add(d);
                    }
                }
                if (forBiome.size() > 0) {
                    int pick = rand.nextInt(forBiome.size());
                    return forBiome.get(pick);
                }
            }
            int pick = rand.nextInt(this.depositWtMap.size());
            return this.depositWtMap.get(pick);

        }
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
        // TODO:TODO:TODO:TODO:TODO:TODO:TODO:TODO:TODO:TODO:TODO:TODO:TODO:
        // Only add a deposit to a biome if it needs to be added
        // (i.e. the whole biome whitelist thing)
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