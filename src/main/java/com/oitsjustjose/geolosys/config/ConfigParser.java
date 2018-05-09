package com.oitsjustjose.geolosys.config;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.util.HelperFunctions;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Arrays;

public class ConfigParser
{
    public ConfigParser()
    {
        parseOres();
        parseStones();
        parsePredicates();
    }

    public static void init()
    {
        new ConfigParser();
    }

    public void parseOres()
    {
        for (String s : ModConfig.userEntries.userOreEntriesRaw)
        {
            String[] parts = s.trim().replace("[", "").replace("[", "").replace(" ", "").split("[\\W]");
            if (parts.length < 10)
            {
                printFormattingError(s);
                continue;
            }
            try
            {
                Block oreBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (oreBlock == null || oreBlock == Blocks.AIR)
                {
                    printFormattingError(s);
                    continue;
                }
                IBlockState oreState = HelperFunctions.getStateFromMeta(oreBlock, toInt(parts[2]));
                Block sampleBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[7], parts[8]));
                if (sampleBlock == null || sampleBlock == Blocks.AIR)
                {
                    printFormattingError(s);
                    continue;
                }
                IBlockState sampleState = HelperFunctions.getStateFromMeta(sampleBlock, toInt(parts[9]));
                int[] blacklist = new int[parts.length - 10];
                for (int i = 0; i < parts.length - 10; i++)
                {
                    blacklist[i] = toInt(parts[10 + i]);
                }
                System.out.println("Blacklist for " + oreState + " is " + Arrays.toString(blacklist));
                GeolosysAPI.registerMineralDeposit(oreState, sampleState, toInt(parts[4]), toInt(parts[5]), toInt(parts[3]), toInt(parts[6]), blacklist);
            }
            catch (NumberFormatException e)
            {
                printFormattingError(s);
            }
        }
    }

    public void parseStones()
    {
        for (String s : ModConfig.userEntries.userStoneEntriesRaw)
        {
            String[] parts = s.trim().replaceAll(" ", "").split("[\\W]");
            if (parts.length != 6)
            {
                printFormattingError(s);
                continue;
            }
            try
            {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (block == null || block == Blocks.AIR)
                {
                    printFormattingError(s);
                    continue;
                }
                GeolosysAPI.registerStoneDeposit(HelperFunctions.getStateFromMeta(block, toInt(parts[2])), toInt(parts[3]), toInt(parts[4]), toInt(parts[5]));

            }
            catch (NumberFormatException e)
            {
                printFormattingError(s);
                continue;
            }
        }
    }

    public void parsePredicates()
    {
        for (String s : ModConfig.userEntries.replacementMatsRaw)
        {
            String[] parts = s.trim().replaceAll(" ", "").split("[\\W]");
            if (parts.length != 2 && parts.length != 3)
            {
                printFormattingError(s);
                continue;
            }
            try
            {
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
                if (block == null || block == Blocks.AIR)
                {
                    printFormattingError(s);
                    continue;
                }
                if (parts.length == 2)
                {
                    GeolosysAPI.replacementMats.add(block.getDefaultState());
                }
                else
                {
                    GeolosysAPI.replacementMats.add(HelperFunctions.getStateFromMeta(block, toInt(parts[2])));
                }
            }
            catch (NumberFormatException e)
            {
                printFormattingError(s);
                continue;
            }
        }
    }

    private int toInt(String s)
    {
        return Integer.parseInt(s);
    }

    private void printFormattingError(String s)
    {
        Geolosys.getInstance().LOGGER.info("Entry " + s + " is not valid and has been skipped. Please check your formatting.");
    }
}
