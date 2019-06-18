package com.oitsjustjose.geolosys.common.config;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
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

        final File jsonFile = new File(configRoot.getAbsolutePath() + "/geolosys.json");
        try
        {
            InputStream jsonStream = new FileInputStream(jsonFile);
            this.read(jsonStream);
        }
        catch (IOException e)
        {
            // Download the file from GitHub if it can't be found
            try
            {
                Geolosys.getInstance().LOGGER.info("Could not find geolosys.json. Downloading it from GitHub...");
                BufferedInputStream in = new BufferedInputStream(
                        new URL("https://raw.githubusercontent.com/oitsjustjose/Geolosys/master/geolosys_ores.json")
                                .openStream());
                Files.copy(in, Paths.get((configRoot.getAbsolutePath() + "/geolosys.json")),
                        StandardCopyOption.REPLACE_EXISTING);
                Geolosys.getInstance().LOGGER.info("Done downloading geolosys.json from GitHub!");
                InputStream jsonStream = new FileInputStream(jsonFile);
                this.read(jsonStream);

            }
            catch (IOException f)
            {
                Geolosys.getInstance().LOGGER.error("File " + configRoot.getAbsolutePath()
                        + "/geolosys.json could neither be found nor downloaded. Unable to load any ores unless they are from CraftTweaker.");
            }
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
                        float density = 1.0F;
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
                            else if (subName.equalsIgnoreCase("density"))
                            {
                                density = (float) jReader.nextDouble();
                            }
                            else
                            {
                                Geolosys.getInstance().LOGGER
                                        .info("Unknown property found in geolosys_ores.json file. Skipping it.");
                                jReader.skipValue();

                            }
                        }
                        register(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist, blockStateMatchers,
                                biomes, isWhitelist, hasIsWhitelist, density);
                        jReader.endObject();
                    }
                    jReader.endArray();
                }
                else if (name.equalsIgnoreCase("stones"))
                {
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
            ArrayList<Biome> biomes, boolean isWhitelist, boolean hasIsWhitelist, float density)
    {
        Geolosys.getInstance().LOGGER.info("Registered " + oreBlocks + ", " + sampleBlocks);
        if (biomes.size() > 0)
        {
            if (hasIsWhitelist)
            {
                GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                        (blockStateMatchers.size() == 0 ? null : blockStateMatchers), biomes, isWhitelist, density);
            }
            else
            {
                Geolosys.getInstance().LOGGER.info(
                        "Received a biome list but no isWhitelist variable to define if the biome list is whitelist or blacklist.\n"
                                + "Registering it as a normal ore with no biome restrictions");
                GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                        (blockStateMatchers.size() == 0 ? null : blockStateMatchers), density);
            }
        }
        else
        {
            GeolosysAPI.registerMineralDeposit(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimBlacklist,
                    (blockStateMatchers.size() == 0 ? null : blockStateMatchers), density);
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
