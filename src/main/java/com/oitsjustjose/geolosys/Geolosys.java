package com.oitsjustjose.geolosys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.blocks.BlockSample;
import com.oitsjustjose.geolosys.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.compat.ie.IECompat;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.items.ItemCoal;
import com.oitsjustjose.geolosys.items.ItemFieldManual;
import com.oitsjustjose.geolosys.items.ItemIngot;
import com.oitsjustjose.geolosys.items.ItemProPick;
import com.oitsjustjose.geolosys.util.ClientRegistry;
import com.oitsjustjose.geolosys.util.Config;
import com.oitsjustjose.geolosys.util.ConfigOres;
import com.oitsjustjose.geolosys.util.ConfigParser;
import com.oitsjustjose.geolosys.util.HelperFunctions;
import com.oitsjustjose.geolosys.util.Lib;
import com.oitsjustjose.geolosys.world.ChunkData;
import com.oitsjustjose.geolosys.world.OreGenerator;
import com.oitsjustjose.geolosys.world.StoneGenerator;
import com.oitsjustjose.geolosys.world.VanillaWorldGenOverride;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Mod(modid = Lib.MODID, name = Lib.NAME, version = Lib.VERSION, guiFactory = Lib.GUIFACTORY, acceptedMinecraftVersions = "1.12", dependencies = "after:immersiveengineering@[0.12,);")
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
    public Item COAL;
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
        if (Config.getInstance().enableCoals)
        {
            COAL = new ItemCoal();
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

        if (Loader.isModLoaded("immersiveengineering"))
        {
            IECompat.init();
        }
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
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 0), configOres.coalSize, configOres.coalMinY, configOres.coalMaxY, configOres.coalChance, configOres.coalDimBlacklist);
        }
        if (configOres.cinnabarChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 1), configOres.cinnabarSize, configOres.cinnabarMinY, configOres.cinnabarMaxY, configOres.cinnabarChance, configOres.cinnabarDimBlacklist);
        }
        if (configOres.goldChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 2), configOres.goldSize, configOres.goldMinY, configOres.goldMaxY, configOres.goldChance, configOres.goldDimBlacklist);
        }
        if (configOres.lapisChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 3), configOres.lapisSize, configOres.lapisMinY, configOres.lapisMaxY, configOres.lapisChance, configOres.lapisDimBlacklist);
        }
        if (configOres.quartzChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 4), configOres.quartzSize, configOres.quartzMinY, configOres.quartzMaxY, configOres.quartzChance, configOres.quartzDimBlacklist);
        }
        if (configOres.kimberliteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 5), configOres.kimberliteSize, configOres.kimberliteMinY, configOres.kimberliteMaxY, configOres.kimberliteChance, configOres.kimberliteDimBlacklist);
        }
        if (configOres.berylChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 6), configOres.berylSize, configOres.berylMinY, configOres.berylMaxY, configOres.berylChance, configOres.berylDimBlacklist);
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
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 0), configOres.hematiteSize, configOres.hematiteMinY, configOres.hematiteMaxY, configOres.hematiteChance, configOres.hematiteDimBlacklist);
        }
        if (configOres.limoniteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 1), configOres.limoniteSize, configOres.limoniteMinY, configOres.limoniteMaxY, configOres.limoniteChance, configOres.limoniteDimBlacklist);
        }
        if (configOres.malachiteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 2), configOres.malachiteSize, configOres.malachiteMinY, configOres.malachiteMaxY, configOres.malachiteChance, configOres.malachiteDimBlacklist);
        }
        if (configOres.azuriteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 3), configOres.azuriteSize, configOres.azuriteMinY, configOres.azuriteMaxY, configOres.azuriteChance, configOres.azuriteDimBlacklist);
        }
        if (configOres.cassiteriteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 4), configOres.cassiteriteSize, configOres.cassiteriteMinY, configOres.cassiteriteMaxY, configOres.cassiteriteChance, configOres.cassiteriteDimBlacklist);
        }
        if (configOres.tealliteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 5), configOres.tealliteSize, configOres.tealliteMinY, configOres.tealliteMaxY, configOres.tealliteChance, configOres.tealliteDimBlacklist);
        }
        if (configOres.galenaChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 6), configOres.galenaSize, configOres.galenaMinY, configOres.galenaMaxY, configOres.galenaChance, configOres.galenaDimBlacklist);
        }
        if (configOres.bauxiteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 7), configOres.bauxiteSize, configOres.bauxiteMinY, configOres.bauxiteMaxY, configOres.bauxiteChance, configOres.bauxiteDimBlacklist);
        }
        if (configOres.platinumChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 8), configOres.platinumSize, configOres.platinumMinY, configOres.platinumMaxY, configOres.platinumChance, configOres.platinumDimBlacklist);
        }
        if (configOres.autuniteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 9), configOres.autuniteSize, configOres.autuniteMinY, configOres.autuniteMaxY, configOres.autuniteChance, configOres.autuniteDimBlacklist);
        }
        if (configOres.sphaleriteChance > 0)
        {
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 10), configOres.sphaleriteSize, configOres.sphaleriteMinY, configOres.sphaleriteMaxY, configOres.sphaleriteChance, configOres.sphaleriteDimBlacklist);
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
            StringBuilder json = new StringBuilder();
            try
            {
                while ((line = br.readLine()) != null)
                {
                    json.append(line);
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                ConfigOres ret = gson.fromJson(json.toString(), ConfigOres.class);
                ret.validate(configDir);
                return ret;
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
