package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.blocks.BlockSample;
import com.oitsjustjose.geolosys.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.config.ModConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

import java.util.Random;

public class ChunkData
{
    private Random random = new Random();

    public void addChunk(ChunkPos pos, World world, IBlockState state)
    {
        if (world.getWorldType() == WorldType.FLAT)
        {
            return;
        }

        int cap = getSampleCount(state);
        for (int i = 0; i < cap; i++)
        {
            BlockPos p = getSamplePos(world, pos);

            if (world.getBlockState(p.down()).getBlock() instanceof BlockSample || world.getBlockState(p.down()).getBlock() instanceof BlockSampleVanilla)
            {
                continue;
            }
            if (ModConfig.samples.generateInWater || !isMoist(world, p))
            {
                world.setBlockState(p, state, 2|16);
            }
        }
    }

    public boolean canGenerateInChunk(ChunkPos pos, int dimension)
    {
        return !GeolosysAPI.getCurrentWorldDeposits().keySet().contains(new GeolosysAPI.ChunkPosSerializable(pos, dimension));
    }

    private BlockPos getSamplePos(World world, ChunkPos chunkPos)
    {
        return world.getTopSolidOrLiquidBlock(new BlockPos((chunkPos.x << 4) + random.nextInt(16), 0, (chunkPos.z << 4) + random.nextInt(16)));
    }

    private boolean isMoist(World world, BlockPos pos)
    {
        return world.getBlockState(pos.up()).getMaterial().isLiquid() || world.getBlockState(pos.east()).getMaterial().isLiquid() || world.getBlockState(pos.west()).getMaterial().isLiquid() || world.getBlockState(pos.north()).getMaterial().isLiquid() || world.getBlockState(pos.south()).getMaterial().isLiquid();
    }

    private int getSampleCount(IBlockState state)
    {
        int count = GeolosysAPI.sampleCounts.get(state) / ModConfig.samples.maxSamples;

        // Normalize maximum sample counts
        if (count > ModConfig.samples.maxSamples)
        {
            count = ModConfig.samples.maxSamples;
        }

        return count;
    }
}