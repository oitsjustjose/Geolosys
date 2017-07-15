package com.oitsjustjose.geolosys.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Random;

public class ChunkData
{
    Random random = new Random();
    private HashSet<ChunkPos> populatedChunks = new HashSet<>();

    public void addChunk(ChunkPos pos, World world, IBlockState state)
    {
        populatedChunks.add(pos);
        BlockPos p = getPosForNugPlacement(world, pos);
        if (world.getBlockState(p).isSideSolid(world, p, EnumFacing.UP))
            world.setBlockState(p.up(), state);
    }

    public boolean canGenerateInChunk(ChunkPos pos)
    {
        return !populatedChunks.contains(pos);
    }

    private BlockPos getPosForNugPlacement(World world, ChunkPos chunkPos)
    {
        // Start from the skybox and work down
        int i = 255;
        while (world.isAirBlock(new BlockPos(chunkPos.x << 4, i, chunkPos.z << 4)))
            i--;

        BlockPos posToAnalyze = new BlockPos((chunkPos.x << 4) + random.nextInt(15), i + 1, (chunkPos.z << 4) + random.nextInt(15));

        if (world.getBlockState(posToAnalyze).getBlock().isReplaceable(world, posToAnalyze))
            posToAnalyze = posToAnalyze.down();
        return posToAnalyze;
    }
}