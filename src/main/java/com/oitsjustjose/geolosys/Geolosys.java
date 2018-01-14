package com.oitsjustjose.geolosys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.blocks.BlockSample;
import com.oitsjustjose.geolosys.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.items.ItemFieldManual;
import com.oitsjustjose.geolosys.items.ItemIngot;
import com.oitsjustjose.geolosys.items.ItemProPick;
import com.oitsjustjose.geolosys.util.*;
import com.oitsjustjose.geolosys.world.ChunkData;
import com.oitsjustjose.geolosys.world.OreGenerator;
import com.oitsjustjose.geolosys.world.StoneGenerator;
import com.oitsjustjose.geolosys.world.VanillaWorldGenOverride;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.ArrayList;

@Mod(modid = Lib.MODID, name = Lib.NAME, version = Lib.VERSION, guiFactory = Lib.GUIFACTORY, acceptedMinecraftVersions = "1.12")
public class Geolosys
{
    @Instance(Lib.MODID)
    private static Geolosys instance;

    // Logger & Configs, statically accessible.
    public Logger LOGGER;
    public ConfigOres configOres;
    public ClientRegistry clientRegistry;
    public ChunkData chunkOreGen;
    public ArrayList<IBlockState> userStates;
    public ConfigParser configParser;

    public Block ORE;
    public Block ORE_VANILLA;
    public Block ORE_SAMPLE;
    public Block ORE_SAMPLE_VANILLA;

    public Item CLUSTER;
    public Item INGOT;
    public Item PRO_PICK;
    public Item ALMANAC;

    public static Geolosys getInstance()
    {
        return instance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        MinecraftForge.EVENT_BUS.register(new Config(event.getSuggestedConfigurationFile()));
        configOres = getOresConfig(event.getModConfigurationDirectory());
        clientRegistry = new ClientRegistry();
        MinecraftForge.EVENT_BUS.register(clientRegistry);
        chunkOreGen = new ChunkData();
        userStates = new ArrayList<>();

        ORE = new BlockOre();
        ORE_VANILLA = new BlockOreVanilla();
        ORE_SAMPLE = new BlockSample();
        ORE_SAMPLE_VANILLA = new BlockSampleVanilla();
        CLUSTER = new ItemCluster();
        ALMANAC = new ItemFieldManual();
        if (Config.getInstance().enableIngots)
        {
            INGOT = new ItemIngot();
        }
        if (Config.getInstance().enableProPick)
        {
            PRO_PICK = new ItemProPick();
        }

        registerGeolosysOreGen();
        registerVanillaOreGen();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.ORE_GEN_BUS.register(new VanillaWorldGenOverride());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (Config.getInstance().enableSmelting)
        {
            if (configOres.hematiteChance > 0 || configOres.limoniteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 0), "ingotIron");
            }
            if (configOres.goldChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 1), "ingotGold");
            }
            if (configOres.malachiteChance > 0 || configOres.azuriteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 2), "ingotCopper");
            }
            if (configOres.cassiteriteChance > 0 || configOres.tealliteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 3), "ingotTin");
            }
            if (configOres.galenaChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 4), "ingotSilver");
                smeltSafely(new ItemStack(CLUSTER, 1, 5), "ingotLead");
            }
            if (configOres.bauxiteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 6), "ingotAluminum");
            }
            if (configOres.limoniteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 7), "ingotNickel");
            }
            if (configOres.platinumChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 8), "ingotPlatinum");
            }
            if (configOres.sphaleriteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 10), "ingotZinc");
            }
            if (Config.getInstance().enableYellorium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 11), "ingotYellorium");
            }
            if (Config.getInstance().enableOsmium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 12), "ingotOsmium");
            }
        }

        configParser = new ConfigParser();
        registerUserOreGen();
        registerUserStoneGen();
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
        GameRegistry.registerWorldGenerator(new StoneGenerator(), 100);
    }

    private void smeltSafely(ItemStack input, String oreDictName)
    {
        try
        {
            GameRegistry.addSmelting(input, OreDictionary.getOres(oreDictName).get(0), 0.7F);
        }
        catch (IndexOutOfBoundsException | NullPointerException ex)
        {
            LOGGER.info(oreDictName + " has not been added already. Smelting has been skipped.");
        }
    }

    private void registerVanillaOreGen()
    {
        if (configOres.coalChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 0), configOres.coalSize, 48, 70, configOres.coalChance, configOres.coalDimBlacklist);
        }
        if (configOres.cinnabarChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 1), configOres.cinnabarSize, 5, 12, configOres.cinnabarChance, configOres.cinnabarDimBlacklist);
        }
        if (configOres.goldChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 2), configOres.goldSize, 5, 30, configOres.goldChance, configOres.goldDimBlacklist);
        }
        if (configOres.lapisChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 3), configOres.lapisSize, 10, 24, configOres.lapisChance, configOres.lapisDimBlacklist);
        }
        if (configOres.quartzChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 4), configOres.quartzSize, 6, 29, configOres.quartzChance, configOres.quartzDimBlacklist);
        }
        if (configOres.kimberliteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 5), configOres.kimberliteSize, 2, 15, configOres.kimberliteChance, configOres.kimberliteDimBlacklist);
        }
        if (configOres.berylChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 6), configOres.berylSize, 4, 32, configOres.berylChance, configOres.berylDimBlacklist);
        }
        if (Config.getInstance().modStones)
        {
            IBlockState diorite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);
            IBlockState andesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
            IBlockState granite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);

            StoneGenerator.addStoneGen(andesite, 2, 70, 40);
            StoneGenerator.addStoneGen(diorite, 2, 70, 40);
            StoneGenerator.addStoneGen(granite, 2, 70, 40);
        }
    }

    private void registerGeolosysOreGen()
    {
        if (configOres.hematiteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 0), configOres.hematiteSize, 32, 60, configOres.hematiteChance, configOres.hematiteDimBlacklist);
        }
        if (configOres.limoniteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 1), configOres.limoniteSize, 6, 32, configOres.limoniteChance, configOres.limoniteDimBlacklist);
        }
        if (configOres.malachiteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 2), configOres.malachiteSize, 39, 44, configOres.malachiteChance, configOres.malachiteDimBlacklist);
        }
        if (configOres.azuriteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 3), configOres.azuriteSize, 12, 44, configOres.azuriteChance, configOres.azuriteDimBlacklist);
        }
        if (configOres.cassiteriteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 4), configOres.cassiteriteSize, 44, 68, configOres.cassiteriteChance, configOres.cassiteriteDimBlacklist);
        }
        if (configOres.tealliteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 5), configOres.tealliteSize, 8, 43, configOres.tealliteChance, configOres.tealliteDimBlacklist);
        }
        if (configOres.galenaChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 6), configOres.galenaSize, 16, 50, configOres.galenaChance, configOres.galentaDimBlacklist);
        }
        if (configOres.bauxiteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 7), configOres.bauxiteSize, 45, 70, configOres.bauxiteChance, configOres.bauxiteDimBlacklist);
        }
        if (configOres.platinumChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 8), configOres.platinumSize, 3, 25, configOres.platinumChance, configOres.platinumDimBlacklist);
        }
        if (configOres.autuniteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 9), configOres.autuniteSize, 8, 33, configOres.autuniteChance, configOres.autuniteDimBlacklist);
        }
        if (configOres.sphaleriteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 10), configOres.sphaleriteSize, 35, 55, configOres.sphaleriteChance, configOres.sphaleriteDimBlacklist);
        }
    }

    @Nonnull
    private ConfigOres getOresConfig(File configDir)
    {
        try
        {
            FileReader fr = new FileReader(configDir.getAbsolutePath() + "/geolosys_ores.json".replace("/", File.separator));
            BufferedReader br = new BufferedReader(fr);
            String line;
            String json = "";
            try
            {
                while ((line = br.readLine()) != null)
                {
                    json += line;
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                return gson.fromJson(json, ConfigOres.class);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e)
        {
            ConfigOres props = new ConfigOres();
            props.populateConfigs();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(props);
            try
            {
                FileWriter fw = new FileWriter(configDir.getAbsolutePath() + "/geolosys_ores.json".replace("/", File.separator));
                fw.write(json);
                fw.close();
                return props;
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
        return getOresConfig(configDir);
    }

    private void registerUserOreGen()
    {
        for (ConfigParser.Entry e : configParser.getUserOreEntries().keySet())
        {
            OreGenerator.addOreGen(e.getState(), e.getSize(), e.getMinY(), e.getMaxY(), e.getChancePerChunk(), new int[]{-1, 1});
            userStates.add(e.getState());
        }
    }

    private void registerUserStoneGen()
    {
        for (ConfigParser.Entry e : configParser.getUserStoneEntries())
        {
            StoneGenerator.addStoneGen(e.getState(), e.getMinY(), e.getMaxY(), e.getChancePerChunk());
        }
    }
}
