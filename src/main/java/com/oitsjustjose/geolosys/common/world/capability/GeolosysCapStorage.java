package com.oitsjustjose.geolosys.common.world.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class GeolosysCapStorage implements Capability.IStorage<IGeolosysCapability>
{
    @Override
    public void readNBT(Capability<IGeolosysCapability> capability, IGeolosysCapability instance, Direction side,
            INBT nbt)
    {
        if (nbt instanceof CompoundNBT)
        {
            instance.deserializeNBT(((CompoundNBT) nbt));
        }
    }

    @Nullable
    @Override
    public INBT writeNBT(Capability<IGeolosysCapability> capability, IGeolosysCapability instance, Direction side)
    {
        // Initialize the Compound with WorldDeposits and RetroGen:
        return instance.serializeNBT();
    }
}
