package com.oitsjustjose.geolosys.common.world.capability.Chunk;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.ChunkPos;

import java.util.concurrent.ConcurrentLinkedQueue;

public class ChunkGennedCapability implements IChunkGennedCapability {
    private final ConcurrentLinkedQueue<ChunkPos> generatedChunks;

    public ChunkGennedCapability() {
        this.generatedChunks = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean hasChunkGenerated(ChunkPos pos) {
        return this.generatedChunks.contains(pos);
    }

    @Override
    public void setChunkGenerated(ChunkPos pos) {
        this.generatedChunks.add(pos);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        ListNBT chunks = new ListNBT();
        this.generatedChunks.forEach(chunkPos -> {
            CompoundNBT t = new CompoundNBT();
            t.putInt("x", chunkPos.x);
            t.putInt("z", chunkPos.z);
            chunks.add(t);
        });
        compound.put("chunks", chunks);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        ListNBT chunks = compound.getList("chunks", 10);
        chunks.forEach(x -> {
            CompoundNBT comp = (CompoundNBT) x;
            ChunkPos chunkPos = new ChunkPos(comp.getInt("x"), comp.getInt("z"));
            this.generatedChunks.add(chunkPos);
        });
    }
}
