package com.oitsjustjose.geolosys.chunkdata;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashSet;

public class ChunkOreGen
{
    private HashSet<ChunkPos> populatedChunks = new HashSet<>();

    public void addChunk(ChunkPos pos)
    {
        this.populatedChunks.add(pos);
    }

    public void addChunk(BlockPos pos)
    {
        this.populatedChunks.add(new ChunkPos(pos.getX() << 4, pos.getZ() << 4));
    }

    public boolean hasGeneratedInChunk(ChunkPos pos)
    {
        return this.populatedChunks.contains(pos);
    }

    public boolean hasGeneratedInChunk(BlockPos pos)
    {
        return this.populatedChunks.contains(new ChunkPos(pos.getX() << 4, pos.getZ() << 4));
    }
}
