package com.oitsjustjose.geolosys.common.api;

import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GeolosysSaveData extends WorldSavedData
{
    private boolean hasOldFiles;

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
                GeolosysAPI.putWorldDeposit(e.getKey(), e.getValue());
            }

            for (Map.Entry<GeolosysAPI.ChunkPosSerializable, Boolean> e : regennedChunksDeprecated.entrySet())
            {
                if (e.getValue())
                {
                    GeolosysAPI.markChunkRegenned(e.getKey());
                }
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
                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s));
            }
        }
        if (compound.hasKey("regennedChunks"))
        {
            NBTTagCompound compRegenned = compound.getCompoundTag("regennedChunks");
            for (String s : compRegenned.getKeySet())
            {
                if (compRegenned.getBoolean(s))
                {
                    GeolosysAPI.markChunkRegenned(s);
                }
            }
        }
        if (compound.hasKey("mineralMap"))
        {
            NBTTagCompound compMineralMap = compound.getCompoundTag("mineralMap");
            for (String chunkPosAsString : compMineralMap.getKeySet())
            {
                ArrayList<BlockPos> posList = new ArrayList<>();

                for (String s : compMineralMap.getCompoundTag(chunkPosAsString).getKeySet())
                {
                    int[] posArr = compMineralMap.getCompoundTag(chunkPosAsString).getIntArray(s);
                    posList.add(new BlockPos(posArr[0], posArr[1], posArr[2]));
                }
                GeolosysAPI.mineralMap.put(fromString(chunkPosAsString), posList);
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
        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, String> e : GeolosysAPI.getCurrentWorldDeposits().entrySet())
        {
            compDeposits.setString(e.getKey().toString(), e.getValue());
        }

        if (!compound.hasKey("regennedChunks"))
        {
            compound.setTag("regennedChunks", new NBTTagCompound());
        }
        NBTTagCompound regenDeposits = compound.getCompoundTag("regennedChunks");
        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, Boolean> e : GeolosysAPI.getRegennedChunks().entrySet())
        {
            regenDeposits.setBoolean(e.getKey().toString(), e.getValue());
        }

        if (!compound.hasKey("mineralMap"))
        {
            compound.setTag("mineralMap", new NBTTagCompound());
        }
        NBTTagCompound minMap = compound.getCompoundTag("mineralMap");
        for (Map.Entry<ChunkPos, ArrayList<BlockPos>> e : GeolosysAPI.mineralMap.entrySet())
        {
            minMap.setTag(e.getKey().toString(), new NBTTagCompound());
            for (int i = 0; i < e.getValue().size(); i++)
            {
                minMap.getCompoundTag(e.getKey().toString()).setIntArray("" + i, new int[]{e.getValue().get(i).getX(), e.getValue().get(i).getY(), e.getValue().get(i).getZ()});
            }
        }

        return compound;
    }

    private ChunkPos fromString(String s)
    {
        String[] parts = s.replace("[", "").replace("]", "").replace(" ", "").split(",");
        return new ChunkPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
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
