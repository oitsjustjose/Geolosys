package com.oitsjustjose.geolosys.common.world;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;

public class SampleUtils {
    private static ArrayList<BlockState> samplePlacementBlacklist = new ArrayList<>();
    private static Random random = new Random();

    @Nullable
    public static BlockPos getSamplePosition(IWorld world, ChunkPos chunkPos, int depositHeight) {
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, 0, blockPosZ);

        while (searchPos.getY() < world.getHeight()) {
            world.getBlockState(searchPos.down()).getBlock();
            if (Block.hasEnoughSolidSide(world, searchPos.down(), Direction.UP)) {
                // If the current block is air
                if (canReplace(world, searchPos)) {
                    // If the block above this state is air,
                    if (canReplace(world, searchPos.up())) {
                        // If the block we're going to place on top of is blacklisted
                        if (canPlaceOn(world, searchPos)) {
                            // If it's above sea level it's fine
                            if (searchPos.getY() > world.getSeaLevel()) {
                                return searchPos;
                            }
                            // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at
                            // least above the deposit
                            else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12)
                                    && searchPos.getY() < depositHeight) {

                                return searchPos;
                            }
                        }
                    }
                }
            }
            searchPos = searchPos.up();
        }
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
        return state.isFoliage(world, pos) || mat.isReplaceable();
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
