package com.oitsjustjose.geolosys.common.config;

import java.util.Arrays;
import java.util.HashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigParser
{
    private static ConfigParser instance;

    public ConfigParser()
    {
        parsePredicates();
        parseConverterBlacklist();
        parseDimensions();
        parseProPickExtras();
    }

    public static void init()
    {
        instance = new ConfigParser();
    }

    /**
     * A special case where we don't want to re-add custom ores ore stones, but want to reset the dimensions and ore converter
     * blacklist.
     */
    public static void reinit()
    {
        instance.parseDimensions();
        instance.parseConverterBlacklist();
        instance.parseProPickExtras();
    }

    private void parseProPickExtras()
    {
        for (String str : ModConfig.prospecting.extraProPickEntries)
        {
            String[] parts = str.split(":");
            if (parts.length == 2 || parts.length == 3)
            {
                Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (b != null)
                {
                    if (parts.length == 3)
                    {
                        GeolosysAPI.proPickExtras.add(Utils.getStateFromMeta(b, Integer.parseInt(parts[2])));
                    }
                    else
                    {
                        GeolosysAPI.proPickExtras.add(b.getDefaultState());
                    }
                }
                else
                {
                    Geolosys.getInstance().LOGGER
                            .info("ProPick extra entry " + str + " does not exist or hasn't been registered yet");
                }
            }
            else
            {
                Geolosys.getInstance().LOGGER.info("ProPick extra entry " + str
                        + " is not valid. Please ensure it's in form modid:block or modid:block:metadata");
            }
        }
    }

    private void parseDimensions()
    {
        if (Geolosys.getInstance().PRO_PICK == null)
        {
            return;
        }
        HashMap<Integer, Integer> dimensionSeaLevels = new HashMap<>();
        for (String s : ModConfig.prospecting.proPickDimensionSeaLevels)
        {
            String[] parts = s.trim().replace(" ", "").split(":");
            if (parts.length != 2)
            {
                Geolosys.getInstance().LOGGER.info("Entry " + s
                        + " is not a valid entry for proPickDimensionSeaLevels. Reason: Wrong number of args.");
                continue;
            }
            dimensionSeaLevels.put(toInt(parts[0]), toInt(parts[1]));
        }
    }

    private void parsePredicates()
    {
        for (String s : ModConfig.userEntries.replacementMatsRaw)
        {
            String[] parts = s.trim().replaceAll(" ", "").replaceAll("<", "").replaceAll(">", "").split(":");
            if (parts.length != 2 && parts.length != 3)
            {
                Geolosys.getInstance().LOGGER
                        .error("Entry " + s + " is not valid. Reason: wrong number of arguments given");
                continue;
            }
            try
            {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (block == null || block == Blocks.AIR)
                {
                    Geolosys.getInstance().LOGGER
                            .error("Entry " + s + " is not valid. Reason: predicate block does not exist");
                    continue;
                }
                if (parts.length == 2)
                {
                    GeolosysAPI.replacementMats.add(block.getDefaultState());
                }
                else
                {
                    if (parts[2].equals("*"))
                    {
                        for (int i = 0; i < 16; i++)
                        {
                            GeolosysAPI.replacementMats.add(Utils.getStateFromMeta(block, i));
                        }
                    }
                    else
                    {
                        GeolosysAPI.replacementMats.add(Utils.getStateFromMeta(block, toInt(parts[2])));
                    }
                }
            }
            catch (NumberFormatException e)
            {
                Geolosys.getInstance().LOGGER.error("Entry " + s
                        + " is not valid. Reason: this entry doesn't have a number where there's supposed to be");
                Geolosys.getInstance().LOGGER.error("Additional Info: " + e.getMessage());
            }
        }
    }

    private void parseConverterBlacklist()
    {
        for (String s : ModConfig.userEntries.convertBlacklistRaw)
        {
            String[] parts = s.trim().replaceAll(" ", "").split("[\\W]");
            if (parts.length != 2 && parts.length != 3)
            {
                Geolosys.getInstance().LOGGER
                        .error("Entry " + s + " is not valid. Reason: wrong number of arguments given");
                continue;
            }
            try
            {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (block == null || block == Blocks.AIR)
                {
                    Geolosys.getInstance().LOGGER
                            .error("Entry " + s + " is not valid. Reason: ore swap blacklist block does not exist");
                    continue;
                }
                if (parts.length == 2)
                {
                    GeolosysAPI.oreConverterBlacklist.add(block.getDefaultState());
                }
                else
                {
                    if (parts[2].equals("*"))
                    {
                        for (int i = 0; i < 16; i++)
                        {
                            GeolosysAPI.oreConverterBlacklist.add(Utils.getStateFromMeta(block, i));
                        }
                    }
                    else
                    {
                        GeolosysAPI.oreConverterBlacklist.add(Utils.getStateFromMeta(block, toInt(parts[2])));
                    }
                }
            }
            catch (NumberFormatException e)
            {
                Geolosys.getInstance().LOGGER.error("Entry " + s
                        + " is not valid. Reason: this entry doesn't have a number where there's supposed to be");
                Geolosys.getInstance().LOGGER.error("Additional Info: " + e.getMessage());
            }
        }
    }

    private int toInt(String s)
    {
        return Integer.parseInt(s);
    }

}
