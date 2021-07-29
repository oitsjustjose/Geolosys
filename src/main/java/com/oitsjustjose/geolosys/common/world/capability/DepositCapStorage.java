package com.oitsjustjose.geolosys.common.world.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class DepositCapStorage implements Capability.IStorage<IDepositCapability> {
    @Override
    public void readNBT(Capability<IDepositCapability> capability, IDepositCapability instance,
            Direction side, INBT nbt) {
        if (nbt instanceof CompoundNBT) {
            instance.deserializeNBT(((CompoundNBT) nbt));
        }
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IDepositCapability> capability, IDepositCapability instance,
            Direction side) {
        // Initialize the Compound with WorldDeposits and RetroGen:
        return instance.serializeNBT();
    }
}
