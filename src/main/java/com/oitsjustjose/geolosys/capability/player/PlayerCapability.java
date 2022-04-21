
package com.oitsjustjose.geolosys.capability.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlayerCapability implements IPlayerCapability {
    private final ConcurrentLinkedQueue<String> playersReceived;
    public static final Capability<IPlayerCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public PlayerCapability() {
        this.playersReceived = new ConcurrentLinkedQueue<>();
    }

    public void setManualReceived(UUID id) {
        this.playersReceived.add(id.toString());
    }

    public boolean hasManualBeenReceived(UUID id) {
        return this.playersReceived.contains(id.toString());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag playersReceived = new ListTag();
        this.playersReceived.forEach(id -> {
            CompoundTag t = new CompoundTag();
            t.putString("uuid", id.toString());
            playersReceived.add(t);
        });
        compound.put("playersReceivedManual", playersReceived);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        ListTag pending = compound.getList("playersReceivedManual", 10);
        pending.forEach((tmp) -> {
            CompoundTag comp = (CompoundTag) tmp;
            this.playersReceived.add(comp.getString("uuid"));
        });
    }
}
