package com.oitsjustjose.geolosys.common.world.capability.Chunk;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IChunkGennedCapability extends INBTSerializable<CompoundNBT> {
    boolean hasChunkGenerated(ChunkPos pos);

    void setChunkGenerated(ChunkPos pos);
}
