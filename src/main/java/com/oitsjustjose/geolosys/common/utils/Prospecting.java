package com.oitsjustjose.geolosys.common.utils;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;

public class Prospecting {
    private static HashSet<BlockState> depositBlocks;

    public static HashSet<BlockState> getDepositBlocks() {
        if (depositBlocks == null) {
            depositBlocks = new HashSet<>();
            populateDepositBlocks();
        }
        return depositBlocks;
    }

    public static void populateDepositBlocks() {
        depositBlocks = new HashSet<>();

        GeolosysAPI.plutonRegistry.getOres().forEach((pluton) -> {
            HashSet<BlockState> ores = pluton.getAllOres();
            if (ores != null) {
                depositBlocks.addAll(ores);
            }
        });
    }
}
