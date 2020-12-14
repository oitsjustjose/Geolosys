package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.WorldGenRegion;

public class SampleUtils {
    private static ArrayList<BlockState> samplePlacementBlacklist = new ArrayList<>();
    private static Random random = new Random();

    @Nullable
    public static BlockPos getSamplePosition(IWorld iworld, ChunkPos chunkPos, int depositHeight) {

        if (!(iworld instanceof WorldGenRegion)) {
            return null;
        }

        WorldGenRegion world = (WorldGenRegion) iworld;

        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);

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
     * @param world: an IWorld instance
     * @param pos:   The current searching position that will be used to confirm
     * @return true if the block below is solid on top AND isn't in the blacklist
     */
    public static boolean canPlaceOn(IWorld world, BlockPos pos) {
        return !samplePlacementBlacklist.contains(world.getBlockState(pos.down()))
                && Block.hasEnoughSolidSide(world, pos.down(), Direction.UP);
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(IWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        return BlockTags.LEAVES.contains(state.getBlock()) || mat.isReplaceable();
    }

    /**
     * @param ore an instance of an IDeposit to determine the sample count of
     * @return an integer bound by {@link CommonConfig}'s MAX_SAMPLES_PER_CHUNK
     */
    public static int getSampleCount(IDeposit ore) {
        int count = (ore.getSize() / CommonConfig.MAX_SAMPLES_PER_CHUNK.get())
                + (ore.getSize() % CommonConfig.MAX_SAMPLES_PER_CHUNK.get());
        return Math.min(CommonConfig.MAX_SAMPLES_PER_CHUNK.get(), count);
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).getMaterial().isLiquid() && !isInWater(world, pos);
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
