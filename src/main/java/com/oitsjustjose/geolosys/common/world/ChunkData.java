package com.oitsjustjose.geolosys.common.world;

import java.util.Random;

import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.blocks.BlockSample;
import com.oitsjustjose.geolosys.common.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

public class ChunkData
{
    private Random random = new Random();

    public void addChunk(ChunkPos pos, World world, IBlockState state, int depositHeight)
    {
        if (world.getWorldType() == WorldType.FLAT)
        {
            return;
        }

        int cap = getSampleCount(state);
        for (int i = 0; i < cap; i++)
        {
            BlockPos p = getSamplePos(world, pos, depositHeight);

            if (world.getBlockState(p.down()).getBlock() instanceof BlockSample
                    || world.getBlockState(p.down()).getBlock() instanceof BlockSampleVanilla)
            {
                continue;
            }
            if (ModConfig.prospecting.generateInWater || !isMoist(world, p))
            {
                world.setBlockState(p.up(), Blocks.AIR.getDefaultState(), 2 | 16);
                world.setBlockState(p, state, 2 | 16);
            }
        }
    }

    public boolean canGenerateInChunk(World world, ChunkPos pos, int dimension)
    {
        // Return true if the dimension is -9999; the default ExU Mining Dim
        return dimension == -9999 || !GeolosysAPI.getCurrentWorldDeposits().keySet()
                .contains(new GeolosysAPI.ChunkPosSerializable(pos, dimension));
    }

    /**
     * A bottom-up search for the next "surface-like" block.
     */
    private BlockPos getSamplePos(World world, ChunkPos chunkPos, int depositHeight)
    {
        int blockPosX = (chunkPos.x << 4) + random.nextInt(16);
        int blockPosZ = (chunkPos.z << 4) + random.nextInt(16);
        BlockPos searchPos = new BlockPos(blockPosX, 0, blockPosZ);
        while (searchPos.getY() < world.getHeight())
        {
            if (world.getBlockState(searchPos.down()).isSideSolid(world, searchPos.down(), EnumFacing.UP))
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
        return world.getTopSolidOrLiquidBlock(searchPos);
    }

    private boolean canReplace(World world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        Material mat = state.getMaterial();
        return mat == Material.AIR || mat == Material.PLANTS || state.getBlock().isLeaves(state, world, pos)
                || state.getBlock().isFoliage(world, pos) || mat.isReplaceable();
    }

    /**
     * 
     * @param posA
     * @param posB
     * @param range An integer representing how far is acceptable to be considered in range
     * @return
     */
    private boolean isWithinRange(int posA, int posB, int range)
    {
        return (Math.abs(posA - posB) <= range);
    }

    private boolean isMoist(World world, BlockPos pos)
    {
        return world.getBlockState(pos.up()).getMaterial().isLiquid()
                || world.getBlockState(pos.east()).getMaterial().isLiquid()
                || world.getBlockState(pos.west()).getMaterial().isLiquid()
                || world.getBlockState(pos.north()).getMaterial().isLiquid()
                || world.getBlockState(pos.south()).getMaterial().isLiquid();
    }

    private int getSampleCount(IBlockState state)
    {
        int count = GeolosysAPI.sampleCounts.get(state) / ModConfig.prospecting.maxSamples;

        // Normalize maximum sample counts
        if (count > ModConfig.prospecting.maxSamples)
        {
            count = ModConfig.prospecting.maxSamples;
        }

        return count;
    }
}