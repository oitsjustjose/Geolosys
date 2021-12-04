package com.oitsjustjose.geolosys.common.utils;

import java.util.HashSet;

import com.oitsjustjose.geolosys.api.GeolosysAPI;

import net.minecraft.block.BlockState;

public class Prospecting {
    private static HashSet<BlockState> depositBlocks;

    public static HashSet<BlockState> getDepositBlocks() {
        if (depositBlocks == null) {
            depositBlocks = new HashSet<BlockState>();
            populateDepositBlocks();
        }
        return depositBlocks;
    }

    public static void populateDepositBlocks() {
        depositBlocks = new HashSet<BlockState>();

        GeolosysAPI.plutonRegistry.getOres().forEach((pluton) -> {
            HashSet<BlockState> ores = pluton.getAllOres();
            if (ores != null) {
                depositBlocks.addAll(ores);
            }
        });
    }
}
