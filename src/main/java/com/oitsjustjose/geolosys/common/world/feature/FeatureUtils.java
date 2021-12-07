package com.oitsjustjose.geolosys.common.world.feature;

import com.oitsjustjose.geolosys.common.world.capability.IDepositCapability;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class FeatureUtils {

    public static boolean isInChunk(ChunkPos chunkPos, BlockPos pos) {
        return new ChunkPos(pos).equals(chunkPos);
    }

    public static BlockState tryGetBlockState(WorldGenLevel level, ChunkPos chunk, BlockPos pos) {
        if (level.hasChunk(chunk.x, chunk.z)) {
            return level.getBlockState(pos);
        }

        return Blocks.BARRIER.defaultBlockState();
    }

    public static boolean tryPlaceBlock(WorldGenLevel level, ChunkPos chunk, BlockPos pos, BlockState state,
            IDepositCapability cap) {
        if (isInChunk(chunk, pos) && level.hasChunk(chunk.x, chunk.z)) {
            boolean ret = level.setBlock(pos, state, 2 | 16);
            return ret;
        }

        cap.putPendingBlock(new BlockPos(pos), state);
        return false;
    }

    public static void fixSnowyBlock(WorldGenLevel level, BlockPos posPlaced) {
        BlockState below = level.getBlockState(posPlaced.below());
        if (below.hasProperty(BlockStateProperties.SNOWY)) {
            level.setBlock(posPlaced.below(), below.setValue(BlockStateProperties.SNOWY, Boolean.valueOf(false)),
                    2 | 16);
        }
    }
}
