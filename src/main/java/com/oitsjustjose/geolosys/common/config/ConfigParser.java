package com.oitsjustjose.geolosys.common.config;

import java.util.HashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigParser
{
    private static ConfigParser instance;

    public ConfigParser()
    {
        parseOres();
        parseStones();
        parsePredicates();
        parseConverterBlacklist();
        parseDimensions();
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

    private void parseOres()
    {
        for (String s : ModConfig.userEntries.userOreEntriesRaw)
        {
            String[] parts = s.trim().replace(" ", "").split("[\\W]");
            if (parts.length < 10)
            {
                Geolosys.getInstance().LOGGER
                        .error("Entry " + s + " is not valid. Reason: wrong number of arguments given");
                continue;
            }
            try
            {
                Block oreBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (oreBlock == null || oreBlock == Blocks.AIR)
                {
                    Geolosys.getInstance().LOGGER
                            .error("Entry " + s + " is not valid. Reason: ore block does not exist");
                    continue;
                }
                IBlockState oreState = Utils.getStateFromMeta(oreBlock, toInt(parts[2]));
                Block sampleBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[7], parts[8]));
                if (sampleBlock == null || sampleBlock == Blocks.AIR)
                {
                    Geolosys.getInstance().LOGGER
                            .error("Entry " + s + " is not valid. Reason: sample block does not exist");
                    continue;
                }
                IBlockState sampleState = Utils.getStateFromMeta(sampleBlock, toInt(parts[9]));
                String blacklistString = s.substring(s.indexOf("["), s.indexOf("]") + 1).replace("[", "")
                        .replace("]", "").replace(" ", "").trim();
                int[] blacklist;
                // Empty Blacklist
                if (blacklistString.length() == 0)
                {
                    blacklist = new int[]
                    {};
                }
                // Blacklist of only 1 dim
                else if (blacklistString.length() == 1)
                {
                    blacklist = new int[]
                    { toInt(blacklistString) };
                }
                // Anything else
                else
                {
                    String[] blacklistParts = blacklistString.split(",");
                    blacklist = new int[blacklistParts.length];
                    for (int i = 0; i < blacklistParts.length; i++)
                    {
                        blacklist[i] = toInt(blacklistParts[i]);
                    }
                }
                GeolosysAPI.registerMineralDeposit(oreState, sampleState, toInt(parts[4]), toInt(parts[5]),
                        toInt(parts[3]), toInt(parts[6]), blacklist);
            }
            catch (NumberFormatException e)
            {
                Geolosys.getInstance().LOGGER.error("Entry " + s
                        + " is not valid. Reason: this entry doesn't have a number where there's supposed to be");
                Geolosys.getInstance().LOGGER.error("Additional Info: " + e.getMessage());
            }
            catch (StringIndexOutOfBoundsException e)
            {
                Geolosys.getInstance().LOGGER.error("Entry " + s
                        + " is not valid. Reason: this entry doesn't have '[]' for the dimension blacklist. This is REQUIRED, even if empty.");
                Geolosys.getInstance().LOGGER.error("Additional Info: " + e.getMessage());
            }
        }
    }

    private void parseStones()
    {
        for (String s : ModConfig.userEntries.userStoneEntriesRaw)
        {
            String[] parts = s.trim().replaceAll(" ", "").split("[\\W]");
            if (parts.length != 6)
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
                            .error("Entry " + s + " is not valid. Reason: stone block does not exist");
                    continue;
                }
                GeolosysAPI.registerStoneDeposit(Utils.getStateFromMeta(block, toInt(parts[2])), toInt(parts[3]),
                        toInt(parts[4]), toInt(parts[5]));
            }
            catch (NumberFormatException e)
            {
                Geolosys.getInstance().LOGGER.error("Entry " + s
                        + " is not valid. Reason: this entry doesn't have a number where there's supposed to be");
                Geolosys.getInstance().LOGGER.error("Additional Info: " + e.getMessage());
            }
        }
    }

    private void parsePredicates()
    {
        for (String s : ModConfig.userEntries.replacementMatsRaw)
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
                            .error("Entry " + s + " is not valid. Reason: predicate block does not exist");
                    continue;
                }
                if (parts.length == 2)
                {
                    GeolosysAPI.replacementMats.add(block.getDefaultState());
                }
                else
                {
                    GeolosysAPI.replacementMats.add(Utils.getStateFromMeta(block, toInt(parts[2])));
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
                    GeolosysAPI.oreConverterBlacklist.add(Utils.getStateFromMeta(block, toInt(parts[2])));
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
