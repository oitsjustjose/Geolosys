package com.oitsjustjose.geolosys.common.world.capability;

import com.oitsjustjose.geolosys.common.world.utils.ChunkPosDim;
import net.minecraft.nbt.CompoundNBT;

import java.util.HashMap;
import java.util.Map;

public class PlutonCapability implements IPlutonCapability
{
    private HashMap<ChunkPosDim, Boolean> generationMap;
    private HashMap<ChunkPosDim, Boolean> retroMap;

    public PlutonCapability()
    {
        this.generationMap = new HashMap<>();
        this.retroMap = new HashMap<>();
    }

    @Override
    public Map<ChunkPosDim, Boolean> getGenerationMap()
    {
        return this.generationMap;
    }

    @Override
    public Map<ChunkPosDim, Boolean> getRetroMap()
    {
        return this.retroMap;
    }

    @Override
    public void setRetroGenned(ChunkPosDim pos)
    {
        this.retroMap.put(pos, true);
    }

    @Override
    public boolean hasRetroGenned(ChunkPosDim pos)
    {
        return this.retroMap.containsKey(pos) && this.retroMap.get(pos);
    }

    @Override
    public void setGenerated(ChunkPosDim dim)
    {
        this.generationMap.put(dim, true);
    }

    @Override
    public boolean hasGenerated(ChunkPosDim pos)
    {
        return this.generationMap.containsKey(pos) && this.generationMap.get(pos);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.put("WorldDeposits", new CompoundNBT());
        compound.put("RetroGen", new CompoundNBT());

        CompoundNBT worldDeposits = compound.getCompound("WorldDeposits");
        CompoundNBT retroGen = compound.getCompound("RetroGen");

        this.getGenerationMap().forEach((x, y) -> worldDeposits.putBoolean(x.toString(), y));
        this.getRetroMap().forEach((x, y) -> retroGen.putBoolean(x.toString(), y));
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound)
    {
        CompoundNBT worldDeposits = compound.getCompound("WorldDeposits");
        CompoundNBT retroGen = compound.getCompound("RetroGen");

        worldDeposits.keySet().forEach(key -> this.setGenerated(new ChunkPosDim(key)));
        retroGen.keySet().forEach(key -> this.setRetroGenned(new ChunkPosDim(key)));
    }
}
