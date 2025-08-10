package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.AbstractDeposit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class PlutonRegistry {
    private ArrayList<AbstractDeposit> deposits;

    public PlutonRegistry() {
        this.deposits = new ArrayList<>();
    }

    public void clear() {
        this.deposits = new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<AbstractDeposit> getOres() {
        return (ArrayList<AbstractDeposit>) this.deposits.clone();
    }

    public void addDeposit(AbstractDeposit ore) {
        this.deposits.add(ore);
    }

    @Nullable
    public AbstractDeposit pick(WorldGenLevel level, BlockPos pos) {
        @SuppressWarnings("unchecked")
        ArrayList<AbstractDeposit> choices = (ArrayList<AbstractDeposit>) this.deposits.clone();
        // Dimension Filtering done here!
        choices.removeIf((dep) -> !dep.canPlaceInBiome(level.getBiome(pos)));

        if (choices.isEmpty()) {
            return null;
        }

        int totalWt = 0;
        for (AbstractDeposit d : choices) {
            totalWt += d.getGenerationWeight();
        }

        int rng = level.getRandom().nextInt(totalWt);
        for (AbstractDeposit d : choices) {
            int wt = d.getGenerationWeight();
            if (rng < wt) {
                return d;
            }
            rng -= wt;
        }

        Geolosys.getInstance().LOGGER.error("Could not reach decision on pluton to generate at PlutonRegistry#pick");
        return null;
    }
}
