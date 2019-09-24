package com.oitsjustjose.geolosys.common.utils;

import com.oitsjustjose.geolosys.api.world.IDeposit;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;

import java.util.Random;

public class SampleUtils
{
    private static Random random = new Random();

    public static BlockPos getSamplePosition(IWorld world, ChunkPos chunkPos, int depositHeight)
    {
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, 0, blockPosZ);

        while (searchPos.getY() < world.getHeight())
        {
            world.getBlockState(searchPos.down()).getBlock();
            if (Block.func_220055_a(world, searchPos.down(), Direction.UP))
            {
                // If the current block is air
                if (canReplace(world, searchPos))
                {
                    // If the block above this state is air,
                    if (canReplace(world, searchPos.up()))
                    {
                        // If it's above sea level it's fine
                        if (searchPos.getY() > world.getSeaLevel())
                        {
                            return searchPos;
                        }
                        // If not, it's gotta be at least 12 blocks away from it (i.e. below it) but at least above the deposit
                        else if (isWithinRange(world.getSeaLevel(), searchPos.getY(), 12)
                                && searchPos.getY() < depositHeight)
                        {
                            return searchPos;
                        }
                    }
                }
            }
            searchPos = searchPos.up();
        }
        return world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, new BlockPos(blockPosX, 0, blockPosZ));
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
        return mat == Material.AIR || state.isFoliage(world, pos) || mat.isReplaceable();
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
     * @return true if the block above is liquid (since we can waterlog)
     */
    public static boolean isInWater(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos.up()).getMaterial().isLiquid() || world.getBlockState(pos).getMaterial().isLiquid();
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
