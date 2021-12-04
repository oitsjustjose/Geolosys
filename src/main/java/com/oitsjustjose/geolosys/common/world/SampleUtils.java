package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.WorldGenRegion;

public class SampleUtils {
    private static ArrayList<BlockState> samplePlacementBlacklist = new ArrayList<>();
    private static Random random = new Random();

    @Nullable
    public static BlockPos getSamplePosition(ISeedReader reader, ChunkPos chunkPos) {
        return getSamplePosition(reader, chunkPos, -1);
    }

    @Nullable
    public static BlockPos getSamplePosition(ISeedReader reader, ChunkPos chunkPos, int spread) {

        if (!(reader instanceof WorldGenRegion)) {
            return null;
        }

        WorldGenRegion world = (WorldGenRegion) reader;
        int usedSpread = Math.max(8, spread);
        int xCenter = (chunkPos.getXStart() + chunkPos.getXEnd()) / 2;
        int zCenter = (chunkPos.getZStart() + chunkPos.getZEnd()) / 2;

        // Only put things in the negative X|Z if the spread is provided.
        int blockPosX = xCenter + (random.nextInt(usedSpread) * ((reader.getRandom().nextBoolean()) ? 1 : -1));
        int blockPosZ = zCenter + (random.nextInt(usedSpread) * ((reader.getRandom().nextBoolean()) ? 1 : -1));

        if (!world.chunkExists(chunkPos.x, chunkPos.z)) {
            return null;
        }

        BlockPos searchPosUp = new BlockPos(blockPosX, world.getSeaLevel(), blockPosZ);
        BlockPos searchPosDown = new BlockPos(blockPosX, world.getSeaLevel(), blockPosZ);

        // Try to get something _above_ sea level first
        while (searchPosUp.getY() < world.getHeight()) {
            if (Block.hasEnoughSolidSide(world, searchPosUp.down(), Direction.UP)) {
                if (canReplace(world, searchPosUp) && canReplace(world, searchPosUp.up())
                        && canPlaceOn(world, searchPosUp)) {
                    return searchPosUp;
                }
            }
            searchPosUp = searchPosUp.up();
        }

        // If all else fails try something below sea level..
        while (searchPosDown.getY() > 0) {
            if (Block.hasEnoughSolidSide(world, searchPosDown.down(), Direction.UP)) {
                if (canReplace(world, searchPosDown) && canReplace(world, searchPosDown.up())
                        && canPlaceOn(world, searchPosDown)) {
                    return searchPosDown;
                }
            }
            searchPosDown = searchPosDown.down();
        }

        // Or just forget about it I guess :P
        return null;
    }

    /**
     * Determines if the sample can be placed on this block
     * 
     * @param world: an ISeedReader instance
     * @param pos:   The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(ISeedReader reader, BlockPos pos) {
        return !samplePlacementBlacklist.contains(reader.getBlockState(pos.down()))
                && Block.hasEnoughSolidSide(reader, pos.down(), Direction.UP);
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(ISeedReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        Material mat = state.getMaterial();
        return BlockTags.LEAVES.contains(state.getBlock()) || mat.isReplaceable();
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(ISeedReader reader, BlockPos pos) {
        return reader.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param reader an ISeedReader instance
     * @param pos    A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(ISeedReader reader, BlockPos pos) {
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
        samplePlacementBlacklist.add(block.getDefaultState());
    }

}
