package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.blocks.BlockSample;
import com.oitsjustjose.geolosys.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.util.Config;
import com.oitsjustjose.geolosys.util.ConfigParser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.HashMap;
import java.util.Random;

public class ChunkData
{
    private Random random = new Random();
    private File fileLocation = new File(DimensionManager.getCurrentSaveRootDirectory(), "GeolosysDeposits.dat");

    public void addChunk(ChunkPos pos, World world, IBlockState state)
    {
        GeolosysAPI.currentWorldDeposits.put(pos, state.toString());
        this.serialize();
        if (world.getWorldType() == WorldType.FLAT)
        {
            return;
        }

        int cap = getSampleCount(state);
        for (int i = 0; i < cap; i++)
        {
            BlockPos p = getSamplePos(world, pos);

            if (world.getBlockState(p.down()).getBlock() instanceof BlockSample || world.getBlockState(p.down()).getBlock() instanceof BlockSampleVanilla)
            {
                continue;
            }
            if (Config.getInstance().generateSamplesInWater || !isMoist(world, p))
            {
                world.setBlockState(p, state);
            }
        }
    }

    public boolean canGenerateInChunk(ChunkPos pos)
    {
        return !GeolosysAPI.currentWorldDeposits.keySet().contains(pos);
    }

    private BlockPos getSamplePos(World world, ChunkPos chunkPos)
    {
        return world.getTopSolidOrLiquidBlock(new BlockPos((chunkPos.x << 4) + random.nextInt(16), 0, (chunkPos.z << 4) + random.nextInt(16)));
    }

    private boolean isMoist(World world, BlockPos pos)
    {
        return world.getBlockState(pos.up()).getMaterial().isLiquid() || world.getBlockState(pos.east()).getMaterial().isLiquid() || world.getBlockState(pos.west()).getMaterial().isLiquid() || world.getBlockState(pos.north()).getMaterial().isLiquid() || world.getBlockState(pos.south()).getMaterial().isLiquid();
    }

    private int getSampleCount(IBlockState state)
    {
        int count = 0;
        if (state.getBlock() == Geolosys.getInstance().ORE_SAMPLE)
        {
            switch (state.getBlock().getMetaFromState(state))
            {
                case 0:
                    count = Math.round(Geolosys.getInstance().configOres.hematiteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 1:
                    count = Math.round(Geolosys.getInstance().configOres.limoniteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 2:
                    count = Math.round(Geolosys.getInstance().configOres.malachiteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 3:
                    count = Math.round(Geolosys.getInstance().configOres.azuriteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 4:
                    count = Math.round(Geolosys.getInstance().configOres.cassiteriteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 5:
                    count = Math.round(Geolosys.getInstance().configOres.tealliteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 6:
                    count = Math.round(Geolosys.getInstance().configOres.galenaSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 7:
                    count = Math.round(Geolosys.getInstance().configOres.bauxiteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 8:
                    count = Math.round(Geolosys.getInstance().configOres.platinumSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 9:
                    count = Math.round(Geolosys.getInstance().configOres.autuniteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 10:
                    count = Math.round(Geolosys.getInstance().configOres.sphaleriteSize / (float) (Config.getInstance().maxSamples));
                    break;
            }
        }
        else if (state.getBlock() == Geolosys.getInstance().ORE_SAMPLE_VANILLA)
        {
            switch (state.getBlock().getMetaFromState(state))
            {
                case 0:
                    count = Math.round(Geolosys.getInstance().configOres.coalSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 1:
                    count = Math.round(Geolosys.getInstance().configOres.cinnabarSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 2:
                    count = Math.round(Geolosys.getInstance().configOres.goldSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 3:
                    count = Math.round(Geolosys.getInstance().configOres.lapisSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 4:
                    count = Math.round(Geolosys.getInstance().configOres.quartzSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 5:
                    count = Math.round(Geolosys.getInstance().configOres.kimberliteSize / (float) (Config.getInstance().maxSamples));
                    break;
                case 6:
                    count = Math.round(Geolosys.getInstance().configOres.berylSize / (float) (Config.getInstance().maxSamples));
                    break;
            }
        }
        else if (Geolosys.getInstance().configParser.getUserOreEntries().containsValue(state))
        {
            for (ConfigParser.Entry e : Geolosys.getInstance().configParser.getUserOreEntries().keySet())
            {
                if (Geolosys.getInstance().configParser.getUserOreEntries().get(e) == state)
                {
                    count = e.getSize();
                    break;
                }
            }
        }

        // Normalize maximum sample counts
        if (count > Config.getInstance().maxSamples)
        {
            count = Config.getInstance().maxSamples;
        }

        return count;
    }

    public void serialize()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream(fileLocation);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(GeolosysAPI.currentWorldDeposits);
            out.close();
            fileOut.close();
        }
        catch (IOException i)
        {
            i.printStackTrace();
            Geolosys.getInstance().LOGGER.error("There was an error saving GeolosysDeposits.dat");
            return;
        }
    }

    @SuppressWarnings("unchecked")
    public void deserialize()
    {
        try
        {
            if (fileLocation.exists())
            {
                FileInputStream fileIn = new FileInputStream(fileLocation);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                GeolosysAPI.currentWorldDeposits = (HashMap<ChunkPos, String>) in.readObject();
                in.close();
                fileIn.close();
            }
        }
        catch (IOException i)
        {
            Geolosys.getInstance().LOGGER.error("There was an error loading GeolosysDeposits.dat");
            i.printStackTrace();
            return;
        }
        catch (ClassNotFoundException c)
        {
            Geolosys.getInstance().LOGGER.error("There was an error in the code for deserialization. Please contact oitsjustjose on GitHub with a log");
            Geolosys.getInstance().LOGGER.error(c.getMessage());
            return;
        }
    }
}