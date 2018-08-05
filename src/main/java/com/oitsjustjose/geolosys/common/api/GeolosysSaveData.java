package com.oitsjustjose.geolosys.common.api;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeolosysSaveData extends WorldSavedData
{
    private HashMap<GeolosysAPI.ChunkPosSerializable, String> currentWorldDeposits = new HashMap<>();
    private LinkedHashMap<GeolosysAPI.ChunkPosSerializable, Boolean> regennedChunks = new LinkedHashMap<>();
    private boolean hasOldFiles;

    /**
     * @param pos   The Mojang ChunkPos to act as a key
     * @param state The String to act as a value
     */
    public void putWorldDeposit(ChunkPos pos, int dimension, String state)
    {
        currentWorldDeposits.put(new GeolosysAPI.ChunkPosSerializable(pos, dimension), state);
        if (ModConfig.featureControl.debugGeneration)
        {
            int total = 0;
            for (GeolosysAPI.ChunkPosSerializable chunk : currentWorldDeposits.keySet())
            {
                if (currentWorldDeposits.get(chunk).equals(state))
                {
                    total++;
                }
            }
            Geolosys.getInstance().LOGGER.info(state + ": " + total + "/" + currentWorldDeposits.keySet().size());
            Geolosys.getInstance().LOGGER.info(state + ": " + (100 * (total / (1f * currentWorldDeposits.keySet().size()))) + "%");
        }
    }

    /**
     * @param pos   The ChunkPosSerializable to act as a key
     * @param state The String to act as a value
     */
    public void putWorldDeposit(GeolosysAPI.ChunkPosSerializable pos, String state)
    {
        currentWorldDeposits.put(pos, state);
    }

    /**
     * @return The world's current deposits throughout the world. The string is formatted as modid:block:meta
     */
    @SuppressWarnings("unchecked")
    public HashMap<GeolosysAPI.ChunkPosSerializable, String> getCurrentWorldDeposits()
    {
        return (HashMap<GeolosysAPI.ChunkPosSerializable, String>) currentWorldDeposits.clone();
    }

    /**
     * @return The world's current deposits throughout the world. The string is formatted as modid:block:meta
     */
    @SuppressWarnings("unchecked")
    public HashMap<GeolosysAPI.ChunkPosSerializable, Boolean> getRegennedChunks()
    {
        return (HashMap<GeolosysAPI.ChunkPosSerializable, Boolean>) regennedChunks.clone();
    }

    public GeolosysSaveData()
    {
        super(Geolosys.MODID);
        this.markDirty();
        this.hasOldFiles = (new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat")).exists() || (new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysRegen.dat")).exists();
    }

    public GeolosysSaveData(String s)
    {
        super(s);
        this.markDirty();
        this.hasOldFiles = (new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat")).exists() || (new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysRegen.dat")).exists();
    }

    /**
     * Marks a chunk as having been regenerated
     * - this is for the "Retroactively replace existing ores in world" option
     *
     * @param pos The ChunkPos to add to
     */
    public void markChunkRegenned(ChunkPos pos, int dimension)
    {
        markChunkRegenned(new GeolosysAPI.ChunkPosSerializable(pos, dimension));
    }

    /**
     * Marks a chunk as having been regenerated
     * - this is for the "Retroactively replace existing ores in world" option
     *
     * @param pos The ChunkPosSerializeable to add to
     */
    public void markChunkRegenned(GeolosysAPI.ChunkPosSerializable pos)
    {
        regennedChunks.put(pos, true);
    }

    /**
     * Checks if a chunk has been retroactively replaced with Geolosys ores
     *
     * @param pos The ChunkPos to check
     * @return True if the chunk is in the map and has been marked as regenned
     */
    public boolean hasChunkRegenned(ChunkPos pos, int dimension)
    {
        return hasChunkRegenned(new GeolosysAPI.ChunkPosSerializable(pos, dimension));
    }

    /**
     * Checks if a chunk has been retroactively replaced with Geolosys ores
     *
     * @param pos The ChunkPos to check
     * @return True if the chunk is in the map and has been marked as regenned
     */
    public boolean hasChunkRegenned(GeolosysAPI.ChunkPosSerializable pos)
    {
        for (GeolosysAPI.ChunkPosSerializable c : regennedChunks.keySet())
        {
            if (c.getX() == pos.getX() && c.getZ() == pos.getZ() && c.getDimension() == pos.getDimension())
            {
                return regennedChunks.get(c);
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void convertFromOld(NBTTagCompound nbt)
    {
        File depositFileLocation = new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat");
        File regenFileLocation = new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysRegen.dat");
        HashMap<GeolosysAPI.ChunkPosSerializable, String> currentWorldDepositsDeprecated = new HashMap<>();
        LinkedHashMap<GeolosysAPI.ChunkPosSerializable, Boolean> regennedChunksDeprecated = new LinkedHashMap<>();
        if (DimensionManager.getCurrentSaveRootDirectory() != null)
        {
            if (depositFileLocation.exists())
            {
                currentWorldDepositsDeprecated = GeolosysAPI.getDeposits(depositFileLocation);
                if (depositFileLocation.delete())
                {
                    Geolosys.getInstance().LOGGER.info("Deleted old depositFile");
                }
            }
            if (regenFileLocation.exists())
            {
                regennedChunksDeprecated = GeolosysAPI.getRegennedChunks(regenFileLocation);
                if (regenFileLocation.delete())
                {
                    Geolosys.getInstance().LOGGER.info("Deleted old regenFile");
                }
            }

            this.hasOldFiles = false;

            for (Map.Entry<GeolosysAPI.ChunkPosSerializable, String> e : currentWorldDepositsDeprecated.entrySet())
            {
                currentWorldDeposits.put(e.getKey(), e.getValue());
            }

            for (Map.Entry<GeolosysAPI.ChunkPosSerializable, Boolean> e : regennedChunksDeprecated.entrySet())
            {
                regennedChunks.put(e.getKey(), e.getValue());
            }

            this.writeToNBT(nbt);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        if (hasOldFiles)
        {
            Geolosys.getInstance().LOGGER.info("Geolosys is converting old persistent files into Forge World Data.");
            this.convertFromOld(compound);
            Geolosys.getInstance().LOGGER.info("Geolosys World Data conversion complete!");

        }
        if (compound.hasKey("currentWorldDeposits"))
        {
            NBTTagCompound compDeposits = compound.getCompoundTag("currentWorldDeposits");
            for (String s : compDeposits.getKeySet())
            {
                currentWorldDeposits.put(GeolosysAPI.chunkPosSerializableFromString(s), compDeposits.getString(s));
            }
        }

        if (compound.hasKey("regennedChunks"))
        {
            NBTTagCompound compDeposits = compound.getCompoundTag("regennedChunks");
            for (String s : compDeposits.getKeySet())
            {
                regennedChunks.put(GeolosysAPI.chunkPosSerializableFromString(s), compDeposits.getBoolean(s));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        if (!compound.hasKey("currentWorldDeposits"))
        {
            compound.setTag("currentWorldDeposits", new NBTTagCompound());
        }
        NBTTagCompound compDeposits = compound.getCompoundTag("currentWorldDeposits");

        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, String> e : currentWorldDeposits.entrySet())
        {
            compDeposits.setString(e.getKey().toString(), e.getValue());
        }

        if (!compound.hasKey("regennedChunks"))
        {
            compound.setTag("regennedChunks", new NBTTagCompound());
        }
        NBTTagCompound regenDeposits = compound.getCompoundTag("regennedChunks");

        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, Boolean> e : regennedChunks.entrySet())
        {
            regenDeposits.setBoolean(e.getKey().toString(), e.getValue());
        }
        return compound;
    }

    public static GeolosysSaveData get(World world)
    {
        // The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
        MapStorage storage = world.getPerWorldStorage();
        GeolosysSaveData instance = (GeolosysSaveData) storage.getOrLoadData(GeolosysSaveData.class, Geolosys.MODID);

        if (instance == null)
        {
            instance = new GeolosysSaveData();
            storage.setData(Geolosys.MODID, instance);
        }
        return instance;
    }
}
