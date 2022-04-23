package com.oitsjustjose.geolosys.capability.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

public interface IChunkGennedCapability {
    boolean hasChunkGenerated(ChunkPos pos);

    void setChunkGenerated(ChunkPos pos);

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag compound);
}
