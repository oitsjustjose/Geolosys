package com.oitsjustjose.geolosys.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

import java.util.HashSet;
import java.util.Random;

public class ChunkData
{
    Random random = new Random();
    private HashSet<ChunkPos> populatedChunks = new HashSet<>();

    public void addChunk(ChunkPos pos, World world, IBlockState state)
    {
        populatedChunks.add(pos);
        if (world.getWorldType() != WorldType.FLAT)
            return;

        // Generate up to 4 clusters per chunk.
        int cap = random.nextInt(4) + 1;
        for (int i = 0; i < cap; i++)
        {
            BlockPos p = getPosForNugPlacement(world, pos);
            if (world.getBlockState(p).isSideSolid(world, p, EnumFacing.UP))
                world.setBlockState(p.up(), state);
        }
    }

    public boolean canGenerateInChunk(ChunkPos pos)
    {
        return !populatedChunks.contains(pos);
    }

    private BlockPos getPosForNugPlacement(World world, ChunkPos chunkPos)
    {
        // Start from the skybox and work down
        BlockPos posToAnalyze = new BlockPos((chunkPos.x << 4) + random.nextInt(15), 255, (chunkPos.z << 4) + random.nextInt(15));
        while (world.getBlockState(posToAnalyze).getBlock().isReplaceable(world, posToAnalyze))
            posToAnalyze = posToAnalyze.down();
        return posToAnalyze;
    }
}