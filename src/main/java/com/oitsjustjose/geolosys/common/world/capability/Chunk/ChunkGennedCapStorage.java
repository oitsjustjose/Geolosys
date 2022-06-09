package com.oitsjustjose.geolosys.common.world.capability.Chunk;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class ChunkGennedCapStorage implements Capability.IStorage<IChunkGennedCapability> {

    @Override
    public void readNBT(Capability<IChunkGennedCapability> capability, IChunkGennedCapability instance, Direction side,
                        INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            instance.deserializeNBT(((CompoundNBT) nbt));
        }
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IChunkGennedCapability> capability, IChunkGennedCapability instance, Direction side) {
        return instance.serializeNBT();
    }
}
