package com.oitsjustjose.geolosys.common.world.capability;

import com.oitsjustjose.geolosys.common.world.utils.ChunkPosDim;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IPlutonCapability extends INBTSerializable<CompoundNBT>
{
    public boolean hasGenerated(ChunkPosDim pos);

    public void setGenerated(ChunkPosDim pos);

    public boolean hasRetroGenned(ChunkPosDim pos);

    public void setRetroGenned(ChunkPosDim pos);

    public Map<ChunkPosDim, Boolean> getGenerationMap();

    public Map<ChunkPosDim, Boolean> getRetroMap();
}
