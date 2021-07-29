package com.oitsjustjose.geolosys.common.world.capability;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DepositCapProvider implements ICapabilitySerializable<CompoundNBT> {
    private final IDepositCapability impl = new DepositCapability();
    private final LazyOptional<IDepositCapability> cap = LazyOptional.of(() -> impl);

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> capIn,
            final @Nullable Direction side) {
        if (capIn == GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY) {
            return cap.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return impl.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        impl.deserializeNBT(nbt);
    }

}
