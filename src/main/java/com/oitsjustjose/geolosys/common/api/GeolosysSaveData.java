package com.oitsjustjose.geolosys.common.api;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.world.util.Deposit;
import com.oitsjustjose.geolosys.common.world.util.DepositBiomeRestricted;
import com.oitsjustjose.geolosys.common.world.util.DepositMultiOre;
import com.oitsjustjose.geolosys.common.world.util.DepositMultiOreBiomeRestricted;
import com.oitsjustjose.geolosys.common.api.IOre;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.DimensionManager;

public class GeolosysSaveData extends WorldSavedData
{
    private boolean hasOldFiles;

    public GeolosysSaveData()
    {
        super(Geolosys.MODID);
        this.markDirty();
        this.hasOldFiles = (new File(
                DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat")).exists()
                || (new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysRegen.dat"))
                        .exists();
    }

    public GeolosysSaveData(String s)
    {
        super(s);
        this.markDirty();
        this.hasOldFiles = (new File(
                DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat")).exists()
                || (new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysRegen.dat"))
                        .exists();
    }

    private void convertFromOld(NBTTagCompound nbt)
    {
        File depositFileLocation = new File(
                DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat");
        File regenFileLocation = new File(
                DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysRegen.dat");
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
                boolean matchedToIore = false;
                for (IOre ore : GeolosysAPI.oreBlocks)
                {
                    if (e.getValue().contains(ore.getOre().getBlock().getRegistryName().toString()))
                    {
                        if (e.getValue().contains("" + ore.getOre().getBlock().getMetaFromState(ore.getOre())))
                        {
                            GeolosysAPI.putWorldDeposit(e.getKey(), e.getValue(), ore);
                            matchedToIore = true;
                        }
                    }
                }
                if (!matchedToIore)
                {
                    Geolosys.getInstance().LOGGER.info(
                            "Couldn't match up any IOre (A new Geolosys construct as of v3.0.x) with a a VERY old entry."
                                    + "Using the old deprecated method instead."
                                    + "This data may not work with the prospector's pick as expected.");
                    GeolosysAPI.putWorldDeposit(e.getKey(), e.getValue());
                }
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
            NBTTagCompound compIOres = compound.getCompoundTag("currentIOres");
            for (String s : compDeposits.getKeySet())
            {

                NBTTagCompound iore = (NBTTagCompound) compIOres.getTag(s);

                // Search for the IOre using what we know:
                for (IOre ore : GeolosysAPI.oreBlocks)
                {
                    if (iore.getString("type") == "DepositMultiOreBiomeRestricted")
                    {
                        if (ore instanceof DepositMultiOreBiomeRestricted)
                        {
                            if (ore.getFriendlyName() == iore.getString("name"))
                            {
                                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s), ore);
                            }
                        }
                    }
                    else if (iore.getString("type") == "DepositBiomeRestricted")
                    {
                        if (ore instanceof DepositBiomeRestricted)
                        {
                            if (ore.getFriendlyName() == iore.getString("name"))
                            {
                                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s), ore);
                            }
                        }
                    }
                    else if (iore.getString("type") == "DepositMultiOre")
                    {
                        if (ore instanceof DepositMultiOre)
                        {
                            if (ore.getFriendlyName() == iore.getString("name"))
                            {
                                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s), ore);
                            }
                        }
                    }
                    else
                    {
                        if (ore instanceof Deposit)
                        {
                            if (ore.getFriendlyName() == iore.getString("name"))
                            {
                                GeolosysAPI.putWorldDeposit(s, compDeposits.getString(s), ore);
                            }
                        }
                    }
                }

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

        if (!compound.hasKey("currentIOres"))
        {
            compound.setTag("currentIOres", new NBTTagCompound());
        }
        NBTTagCompound compIOres = compound.getCompoundTag("currentIOres");
        for (Map.Entry<GeolosysAPI.ChunkPosSerializable, IOre> e : GeolosysAPI.getCurrentIOres().entrySet())
        {
            NBTTagCompound iOreTag = new NBTTagCompound();
            if (e.getValue() instanceof DepositMultiOreBiomeRestricted)
            {
                iOreTag.setString("type", "DepositMultiOreBiomeRestricted");

            }
            else if (e.getValue() instanceof DepositBiomeRestricted)
            {
                iOreTag.setString("type", "DepositBiomeRestricted");
            }
            else if (e.getValue() instanceof DepositMultiOre)
            {
                iOreTag.setString("type", "DepositMultiOre");
            }
            else
            {
                iOreTag.setString("type", "Deposit");
            }
            // We just need these two to determine what kind of IOre we have
            iOreTag.setString("name", e.getValue().getFriendlyName());

            compIOres.setTag(e.getKey().toString(), iOreTag);
        }

        return compound;
    }

    public static GeolosysSaveData get(World world)
    {
        // The IS_GLOBAL constant is there for clarity, and should be simplified into
        // the right branch.
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
