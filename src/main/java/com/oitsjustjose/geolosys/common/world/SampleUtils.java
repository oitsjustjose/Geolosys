package com.oitsjustjose.geolosys.common.world;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SampleUtils {
    @Nullable
    public static BlockPos getSamplePosition(WorldGenLevel level, ChunkPos chunkPos) {
        return getSamplePosition(level, chunkPos, -1);
    }

    @Nullable
    public static BlockPos getSamplePosition(WorldGenLevel level, ChunkPos chunkPos, int spread) {

        if (!(level instanceof WorldGenRegion region)) {
            return null;
        }

        var usedSpread = Math.max(8, spread);
        var xCenter = (chunkPos.getMinBlockX() + chunkPos.getMaxBlockX()) / 2;
        var zCenter = (chunkPos.getMinBlockZ() + chunkPos.getMaxBlockZ()) / 2;

        // Only put things in the negative X|Z if the spread is provided.
        var blockPosX = xCenter
                + (level.getRandom().nextInt(usedSpread) * ((level.getRandom().nextBoolean()) ? 1 : -1));
        var blockPosZ = zCenter
                + (level.getRandom().nextInt(usedSpread) * ((level.getRandom().nextBoolean()) ? 1 : -1));

        if (!region.hasChunk(chunkPos.x, chunkPos.z)) {
            return null;
        }

        var searchPos = new BlockPos(blockPosX, region.getHeight(), blockPosZ);

        // With worlds being so much deeper,
        // it makes most sense to take a top-down approach
        while (searchPos.getY() > region.getMinBuildHeight()) {
            var blockToPlaceOn = region.getBlockState(searchPos);
            // Check if the location itself is solid
            if (Block.isFaceFull(blockToPlaceOn.getShape(region, searchPos), Direction.UP)) {
                if (!blockToPlaceOn.is(Constants.SUPPORTS_SAMPLE)) {
                    searchPos = searchPos.below();
                    continue;
                }
                var actualPlacePos = searchPos.above();
                if (canReplace(region, actualPlacePos)) {
                    return actualPlacePos;
                }
            }
            searchPos = searchPos.below();
        }

        return null;
    }

    /**
     * @param level an WorldGenLevel instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(WorldGenLevel level, BlockPos pos) {
        var state = level.getBlockState(pos);
        return state.canBeReplaced() || state.isAir();
    }

    /**
     * @param level an WorldGenLevel instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(WorldGenLevel level, BlockPos pos) {
        return level.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param level an WorldGenLevel instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(WorldGenLevel level, BlockPos pos) {
        return (!level.getBlockState(pos).getFluidState().isEmpty()) && !isInWater(level, pos);
    }

    /**
     * @param posA
     * @param posB
     * @param range An integer representing how far is acceptable to be considered
     *              in range
     * @return true if within range
     */
    public static boolean isWithinRange(int posA, int posB, int range) {
        return (Math.abs(posA - posB) <= range);
    }
}
