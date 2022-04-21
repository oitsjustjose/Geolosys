package com.oitsjustjose.geolosys.capability.player;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public interface IPlayerCapability {
    void setManualReceived(UUID id);

    boolean hasManualBeenReceived(UUID id);

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag compound);
}
