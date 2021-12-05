package com.oitsjustjose.geolosys.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public class SampleUtils {
    private static ArrayList<BlockState> samplePlacementBlacklist = new ArrayList<>();
    private static Random random = new Random();

    @Nullable
    public static BlockPos getSamplePosition(WorldGenLevel reader, ChunkPos chunkPos) {
        return getSamplePosition(reader, chunkPos, -1);
    }

    @Nullable
    public static BlockPos getSamplePosition(WorldGenLevel reader, ChunkPos chunkPos, int spread) {

        if (!(reader instanceof WorldGenRegion)) {
            return null;
        }

        WorldGenRegion world = (WorldGenRegion) reader;
        int usedSpread = Math.max(8, spread);
        int xCenter = (chunkPos.getMinBlockX() + chunkPos.getMaxBlockX()) / 2;
        int zCenter = (chunkPos.getMinBlockZ() + chunkPos.getMaxBlockZ()) / 2;

        // Only put things in the negative X|Z if the spread is provided.
        int blockPosX = xCenter + (random.nextInt(usedSpread) * ((reader.getRandom().nextBoolean()) ? 1 : -1));
        int blockPosZ = zCenter + (random.nextInt(usedSpread) * ((reader.getRandom().nextBoolean()) ? 1 : -1));

        if (!world.hasChunk(chunkPos.x, chunkPos.z)) {
            return null;
        }

        BlockPos searchPosUp = new BlockPos(blockPosX, world.getSeaLevel(), blockPosZ);
        BlockPos searchPosDown = new BlockPos(blockPosX, world.getSeaLevel(), blockPosZ);

        // Try to get something _above_ sea level first
        while (searchPosUp.getY() < world.getHeight()) {
            BlockState stateBelow = world.getBlockState(searchPosUp.below());
            if (Block.isShapeFullBlock(stateBelow.getShape(world, searchPosUp.below()))) {
                if (canReplace(world, searchPosUp) && canReplace(world, searchPosUp.above())
                        && canPlaceOn(world, searchPosUp)) {
                    return searchPosUp;
                }
            }
            searchPosUp = searchPosUp.above();
        }

        // If all else fails try something below sea level..
        while (searchPosDown.getY() > 0) {
            BlockState stateBelow = world.getBlockState(searchPosDown.below());
            if (Block.isShapeFullBlock(stateBelow.getShape(world, searchPosDown.below()))) {
                if (canReplace(world, searchPosDown) && canReplace(world, searchPosDown.above())
                        && canPlaceOn(world, searchPosDown)) {
                    return searchPosDown;
                }
            }
            searchPosDown = searchPosDown.below();
        }

        return null;
    }

    /**
     * Determines if the sample can be placed on this block
     * 
     * @param world: an ISeedReader instance
     * @param pos:   The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(WorldGenLevel reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        return !samplePlacementBlacklist.contains(reader.getBlockState(pos.below()))
                && Block.isShapeFullBlock(state.getShape(reader, pos));
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(WorldGenLevel reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        Material mat = state.getMaterial();
        return BlockTags.LEAVES.contains(state.getBlock()) || mat.isReplaceable();
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(WorldGenLevel reader, BlockPos pos) {
        return reader.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(WorldGenLevel reader, BlockPos pos) {
        return reader.getBlockState(pos).getMaterial().isLiquid() && !isInWater(reader, pos);
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

    public static void addSamplePlacementBlacklist(Block block) {
        samplePlacementBlacklist.add(block.defaultBlockState());
    }

}
