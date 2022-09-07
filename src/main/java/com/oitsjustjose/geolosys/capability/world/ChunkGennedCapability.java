package com.oitsjustjose.geolosys.capability.world;

import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ChunkGennedCapability implements IChunkGennedCapability {
    private final ConcurrentLinkedQueue<ChunkPos> generatedChunks;

    public static final Capability<IChunkGennedCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

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
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag chunks = new ListTag();
        this.generatedChunks.forEach(chunkPos -> {
            CompoundTag t = new CompoundTag();
            t.putInt("x", chunkPos.x);
            t.putInt("z", chunkPos.z);
            chunks.add(t);
        });
        compound.put("chunks", chunks);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        ListTag chunks = compound.getList("chunks", 10);
        chunks.forEach(x -> {
            CompoundTag comp = (CompoundTag) x;
            ChunkPos chunkPos = new ChunkPos(comp.getInt("x"), comp.getInt("z"));
            this.generatedChunks.add(chunkPos);
        });
    }

}
