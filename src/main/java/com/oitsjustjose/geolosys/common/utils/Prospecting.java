package com.oitsjustjose.geolosys.common.utils;

import java.util.HashSet;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

public class Prospecting {
    private static HashSet<BlockState> depositBlocks;
    private static HashSet<BlockState> detectionBlacklist;

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

    public static HashSet<BlockState> getDetectionBlacklist() {
        if (detectionBlacklist == null) {
            detectionBlacklist = new HashSet<>();
            populateDetectionBlacklist();
        }
        return detectionBlacklist;
    }

    public static void populateDetectionBlacklist() {
        detectionBlacklist = new HashSet<>();

        CommonConfig.PRO_PICK_DETECTION_BLACKLIST.get().forEach(s -> {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
            if (block != null) {
                detectionBlacklist.add(block.defaultBlockState());
            } else {
                Geolosys.getInstance().LOGGER
                        .warn("The item {} in the proPickDetectionBlacklist config option was not valid", s);
            }
        });
    }

    public static boolean isBlacklistedFromDetection(BlockState test) {
        return getDetectionBlacklist().contains(test.getBlock().defaultBlockState());
    }
}
