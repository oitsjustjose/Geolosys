package com.oitsjustjose.geolosys.common.world.utils;

import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;

public class SampleUtils
{
    private static Random random = new Random();

    @Nullable
    public static BlockPos getSamplePosition(IWorld world, ChunkPos chunkPos, int depositHeight)
    {
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        for (int y = world.getHeight(); y > world.getSeaLevel(); y--)
        {
            BlockPos scan = new BlockPos(blockPosX, y, blockPosZ);
            if (!world.getBlockState(scan).getMaterial().isReplaceable())
            {
                return scan.up();
            }
        }
        return null;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block at pos is replaceable
     */
    public static boolean canReplace(IWorld world, BlockPos pos)
    {
        BlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        return state.isFoliage(world, pos) || mat.isReplaceable();
    }

    /**
     * @param ore an instance of an IDeposit to determine the sample count of
     * @return an integer bound by {@link ModConfig}'s MAX_SAMPLES_PER_CHUNK
     */
    public static int getSampleCount(IDeposit ore)
    {
        int count = ore.getSize() / ModConfig.MAX_SAMPLES_PER_CHUNK.get();
        return Math.min(ModConfig.MAX_SAMPLES_PER_CHUNK.get(), count);
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is water (since we can waterlog)
     */
    public static boolean isInWater(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).getBlock() == Blocks.WATER;
    }

    /**
     * @param world an IWorld instance
     * @param pos   A BlockPos to check in and around
     * @return true if the block is in a non-water fluid
     */
    public static boolean inNonWaterFluid(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).getMaterial().isLiquid() && !isInWater(world, pos);
    }

    /**
     * @param posA
     * @param posB
     * @param range An integer representing how far is acceptable to be considered in range
     * @return true if within range
     */
    public static boolean isWithinRange(int posA, int posB, int range)
    {
        return (Math.abs(posA - posB) <= range);
    }

}
