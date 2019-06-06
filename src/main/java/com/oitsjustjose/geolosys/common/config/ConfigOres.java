package com.oitsjustjose.geolosys.common.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.google.gson.stream.JsonReader;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ConfigOres
{
    public ConfigOres(File configRoot)
    {

        final File jsonFile = new File(configRoot.getAbsolutePath() + "/geolosys_ores.json");
        try
        {
            InputStream jsonStream = new FileInputStream(jsonFile);
            this.read(jsonStream);
        }
        catch (IOException e)
        {
            System.out.println("File " + configRoot.getAbsolutePath() + "/geolosys_ores.json could not be found.");
        }
    }

    public void read(InputStream in) throws IOException
    {
        JsonReader jReader = new JsonReader(new InputStreamReader(in));
        try
        {
            jReader.beginObject();
            while (jReader.hasNext())
            {
                String name = jReader.nextName();
                if (name.equalsIgnoreCase("ores"))
                {
                    jReader.beginArray();
                    while (jReader.hasNext())
                    {
                        HashMap<IBlockState, Integer> oreBlocks = new HashMap<>();
                        HashMap<IBlockState, Integer> sampleBlocks = new HashMap<>();
                        int yMin = -1;
                        int yMax = -1;
                        int size = -1;
                        int chance = -1;
                        int[] dimBlacklist = new int[]
                        {};
                        ArrayList<IBlockState> blockStateMatchers = new ArrayList<>();
                        ArrayList<Biome> biomes = new ArrayList<>();
                        boolean isWhitelist = false;
                        boolean hasIsWhitelist = false;
                        jReader.beginObject();
                        while (jReader.hasNext())
                        {
                            String subName = jReader.nextName();
                            if (subName.equalsIgnoreCase("blocks"))
                            {
                                jReader.beginArray();
                                while (jReader.hasNext())
                                {
                                    oreBlocks.put(fromString(jReader.nextString()), jReader.nextInt());
                                }
                                jReader.endArray();
                            }
                            else if (subName.equalsIgnoreCase("samples"))
                            {
                                jReader.beginArray();
                                while (jReader.hasNext())
                                {
                                    sampleBlocks.put(fromString(jReader.nextString()), jReader.nextInt());
                                }
                                jReader.endArray();
                            }
                            else if (subName.equalsIgnoreCase("yMin"))
                            {
                                yMin = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("yMax"))
                            {
                                yMax = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("size"))
                            {
                                size = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("chance"))
                            {
                                chance = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("dimBlacklist"))
                            {
                                ArrayList<Integer> tmp = new ArrayList<>();
                                jReader.beginArray();
                                while (jReader.hasNext())
                                {
                                    tmp.add(jReader.nextInt());
                                }
                                jReader.endArray();
                                dimBlacklist = fromArrayList(tmp);
                            }
                            else if (subName.equalsIgnoreCase("blockStateMatchers"))
                            {
                                jReader.beginArray();
                                while (jReader.hasNext())
                                {
                                    String toString = jReader.nextString();
                                    if (fromString(toString) != null)
                                    {
                                        blockStateMatchers.add(fromString(toString));
                                    }
                                }
                                jReader.endArray();
                            }
                            else if (subName.equalsIgnoreCase("biomes"))
                            {
                                jReader.beginArray();
                                while (jReader.hasNext())
                                {
                                    Biome b = ForgeRegistries.BIOMES
                                            .getValue(new ResourceLocation(jReader.nextString()));
                                    if (b != null)
                                    {
                                        biomes.add(b);
                                    }
                                }
                                jReader.endArray();
                            }
                            else if (subName.equalsIgnoreCase("isWhitelist"))
                            {
                                isWhitelist = jReader.nextBoolean();
                                hasIsWhitelist = true;
                            }
                            else
                            {
                                Geolosys.getInstance().LOGGER
                                        .info("Unknown property found in geolosys_ores.json file. Skipping it.");
                                jReader.skipValue();

                            }
                        }
                        register(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist, blockStateMatchers,
                                biomes, isWhitelist, hasIsWhitelist);
                        jReader.endObject();
                    }
                    jReader.endArray();
                }
                else if (name.equalsIgnoreCase("stones"))
                {
                    System.out.println("In stones section");

                    jReader.beginArray();
                    while (jReader.hasNext())
                    {
                        jReader.beginObject();
                        IBlockState stone = null;
                        int yMin = -1;
                        int yMax = -1;
                        int chance = -1;
                        int size = -1;
                        int[] dimBlacklist = new int[]
                        {};
                        while (jReader.hasNext())
                        {
                            String subName = jReader.nextName();
                            if (subName.equalsIgnoreCase("block"))
                            {
                                stone = fromString(jReader.nextString());
                            }
                            else if (subName.equalsIgnoreCase("yMin"))
                            {
                                yMin = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("yMax"))
                            {
                                yMax = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("size"))
                            {
                                size = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("chance"))
                            {
                                chance = jReader.nextInt();
                            }
                            else if (subName.equalsIgnoreCase("dimBlacklist"))
                            {
                                ArrayList<Integer> tmp = new ArrayList<>();
                                jReader.beginArray();
                                while (jReader.hasNext())
                                {
                                    tmp.add(jReader.nextInt());
                                }
                                jReader.endArray();
                                dimBlacklist = fromArrayList(tmp);
                            }
                        }
                        register(stone, yMin, yMax, chance, size, dimBlacklist);
                        jReader.endObject();
                    }
                    jReader.endArray();
                }
                else
                {
                    jReader.skipValue();
                }
            }
            jReader.endObject();
        }
        catch (Exception e)
        {
            Geolosys.getInstance().LOGGER.error(
                    "There was a parsing error with the geolosys_ores.json file. Please check for drastic syntax errors and check it at https://jsonlint.com/");
            e.printStackTrace();
        }
        finally
        {
            jReader.close();
        }
    }

    private void register(IBlockState stone, int yMin, int yMax, int chance, int size, int[] dimBlacklist)
    {
        GeolosysAPI.registerStoneDeposit(stone, yMin, yMax, chance, size, dimBlacklist);
    }

    private void register(HashMap<IBlockState, Integer> oreBlocks, HashMap<IBlockState, Integer> sampleBlocks, int yMin,
            int yMax, int size, int chance, int[] dimBlacklist, ArrayList<IBlockState> blockStateMatchers,
            ArrayList<Biome> biomes, boolean isWhitelist, boolean hasIsWhitelist)
    {
        if (biomes.size() > 0)
        {
            if (hasIsWhitelist)
            {
                GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                        (blockStateMatchers.size() == 0 ? null : blockStateMatchers), biomes, isWhitelist);
            }
            else
            {
                Geolosys.getInstance().LOGGER.info(
                        "Received a biome list but no isWhitelist variable to define if the biome list is whitelist or blacklist.\n"
                                + "Registering it as a normal ore with no biome restrictions");
                GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                        (blockStateMatchers.size() == 0 ? null : blockStateMatchers));
            }
        }
        else
        {
            GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                    (blockStateMatchers.size() == 0 ? null : blockStateMatchers));
        }
    }

    private IBlockState fromString(String iBlockState)
    {
        String[] parts = iBlockState.split(":");
        if (parts.length == 2)
        {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
            return b.getDefaultState();
        }
        else if (parts.length == 3)
        {
            Block b = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
            return Utils.getStateFromMeta(b, Integer.parseInt(parts[2]));
        }
        else
        {
            Geolosys.getInstance().LOGGER.info(
                    "String " + iBlockState + " is not a valid block with or without metadata. It has been skipped");
            return null;
        }
    }

    private int[] fromArrayList(ArrayList<Integer> arrList)
    {
        int[] retVal = new int[arrList.size()];
        for (int i = 0; i < arrList.size(); i++)
        {
            retVal[i] = arrList.get(i);
        }
        return retVal;
    }
}
