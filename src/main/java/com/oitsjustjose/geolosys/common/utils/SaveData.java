package com.oitsjustjose.geolosys.common.utils;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Map;

public class SaveData extends WorldSavedData
{
    private ServerWorld serverWorld;

    public SaveData(ServerWorld world)
    {
        super(Constants.MODID);
        this.markDirty();
        this.serverWorld = world;
    }


    @Override
    public void read(CompoundNBT compound)
    {
        if (compound.contains("currentWorldDeposits"))
        {
            CompoundNBT compDeposits = compound.getCompound("currentWorldDeposits");
            for (String s : compDeposits.keySet())
            {
                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s));
            }
        }
        if (compound.contains("regennedChunks"))
        {
            CompoundNBT compRegenned = compound.getCompound("regennedChunks");
            for (String s : compRegenned.keySet())
            {
                if (compRegenned.getBoolean(s))
                {
                    GeolosysAPI.markChunkRegenned(s);
                }
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound)
    {
        if (!compound.contains("currentWorldDeposits"))
        {
            compound.put("currentWorldDeposits", new CompoundNBT());
        }
        CompoundNBT compDeposits = compound.getCompound("currentWorldDeposits");
        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, String> e : GeolosysAPI.getCurrentWorldDeposits().entrySet())
        {
            compDeposits.putString(e.getKey().toString(), e.getValue());
        }

        if (!compound.contains("regennedChunks"))
        {
            compound.put("regennedChunks", new CompoundNBT());
        }
        CompoundNBT regenDeposits = compound.getCompound("regennedChunks");
        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, Boolean> e : GeolosysAPI.getRegennedChunks().entrySet())
        {
            regenDeposits.putBoolean(e.getKey().toString(), e.getValue());
        }

        return compound;
    }

    public static SaveData get()
    {

        SaveData instance = (SaveData) .getOrLoadData(SaveData.class, Constants.MODID);

        if (instance == null)
        {
            instance = new SaveData();
            storage.setData(Constants.MODID, instance);
        }
        return instance;
    }
}