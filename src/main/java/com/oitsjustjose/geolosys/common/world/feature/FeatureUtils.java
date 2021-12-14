package com.oitsjustjose.geolosys.common.world.feature;

import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FeatureUtils {
    public static boolean tryPlaceBlock(WorldGenLevel level, ChunkPos chunk, BlockPos pos, BlockState state,
            IDepositCapability cap) {
        if (!level.setBlock(pos, state, 2 | 16)) {
            cap.putPendingBlock(new BlockPos(pos), state);
            return false;
        }
        return true;
    }

    public static void fixSnowyBlock(WorldGenLevel level, BlockPos posPlaced) {
        BlockState below = level.getBlockState(posPlaced.below());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            level.setBlock(posPlaced.below(), below.setValue(BlockStateProperties.SNOWY, Boolean.valueOf(false)),
                    2 | 16);
        }
    }
}
