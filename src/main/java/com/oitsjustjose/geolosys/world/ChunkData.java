package com.oitsjustjose.geolosys.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.HashSet;

public class ChunkData
{
    private HashMap<ChunkPos, Integer> populatedChunks = new HashMap<>();

    public void addChunk(ChunkPos pos)
    {
        if (populatedChunks.containsKey(pos))
            populatedChunks.put(pos, populatedChunks.get(pos) + 1);
        populatedChunks.put(pos, 1);
    }

    public boolean canGenerateInChunk(ChunkPos pos)
    {
        if (populatedChunks.containsKey(pos))
            return populatedChunks.get(pos) < 2;
        return true;
    }

}
