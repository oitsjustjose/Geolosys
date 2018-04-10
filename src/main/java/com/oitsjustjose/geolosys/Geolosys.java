package com.oitsjustjose.geolosys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.blocks.BlockSample;
import com.oitsjustjose.geolosys.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.compat.ModMaterials;
import com.oitsjustjose.geolosys.compat.OreConverter;
import com.oitsjustjose.geolosys.compat.ie.IECompat;
import com.oitsjustjose.geolosys.config.ConfigOres;
import com.oitsjustjose.geolosys.config.ConfigParser;
import com.oitsjustjose.geolosys.config.ModConfig;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.items.ItemCoal;
import com.oitsjustjose.geolosys.items.ItemFieldManual;
import com.oitsjustjose.geolosys.items.ItemIngot;
import com.oitsjustjose.geolosys.items.ItemProPick;
import com.oitsjustjose.geolosys.util.ClientRegistry;
import com.oitsjustjose.geolosys.util.Finder;
import com.oitsjustjose.geolosys.util.HelperFunctions;
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
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
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

@Mod(modid = Geolosys.MODID, name = "Geolosys", version = Geolosys.VERSION, acceptedMinecraftVersions = "1.12", dependencies = "after:immersiveengineering@[0.12,);")
public class Geolosys
{
    public static final String MODID = "geolosys";
    public static final String VERSION = "@VERSION@";

    @Instance(Geolosys.MODID)
    private static Geolosys instance;

    // Logger & Configs, statically accessible.
    public Logger LOGGER;
    public ConfigOres configOres;
    public ClientRegistry clientRegistry;
    public ChunkData chunkOreGen;

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
        configOres = getOresConfig(event.getModConfigurationDirectory());
        clientRegistry = new ClientRegistry();
        MinecraftForge.EVENT_BUS.register(clientRegistry);
        chunkOreGen = new ChunkData();

        ORE = new BlockOre();
        ORE_VANILLA = new BlockOreVanilla();
        ORE_SAMPLE = new BlockSample();
        ORE_SAMPLE_VANILLA = new BlockSampleVanilla();
        CLUSTER = new ItemCluster();
        ALMANAC = new ItemFieldManual();
        if (ModConfig.featureControl.enableIngots)
        {
            INGOT = new ItemIngot();
        }
        if (ModConfig.featureControl.enableCoals)
        {
            COAL = new ItemCoal();
        }
        if (ModConfig.featureControl.enableProPick)
        {
            PRO_PICK = new ItemProPick();
        }
        if (ModConfig.featureControl.enableEnhancedCompass)
        {
            MinecraftForge.EVENT_BUS.register(new Finder());
        }
        if (ModConfig.featureControl.modDrops)
        {
            MinecraftForge.EVENT_BUS.register(new OreConverter());
        }

        registerGeolosysOreGen();
        registerVanillaOreGen();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.ORE_GEN_BUS.register(new VanillaWorldGenOverride());

        if (Loader.isModLoaded("immersiveengineering") && ModConfig.featureControl.enableIECompat)
        {
            IECompat.init();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (ModConfig.featureControl.enableSmelting)
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
            if (ModConfig.featureControl.enableYellorium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 11), "ingotYellorium");
            }
            if (ModConfig.featureControl.enableOsmium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 12), "ingotOsmium");
            }
        }

        ConfigParser.init();
        ModMaterials.init();
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
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 0), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 0), configOres.coalMinY, configOres.coalMaxY, configOres.coalSize, configOres.coalChance, configOres.coalDimBlacklist);
        }
        if (configOres.cinnabarChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 1), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 1), configOres.cinnabarMinY, configOres.cinnabarMaxY, configOres.cinnabarSize, configOres.cinnabarChance, configOres.cinnabarDimBlacklist);
        }
        if (configOres.goldChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 2), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 2), configOres.goldMinY, configOres.goldMaxY, configOres.goldSize, configOres.goldChance, configOres.goldDimBlacklist);
        }
        if (configOres.lapisChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 3), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 3), configOres.lapisMinY, configOres.lapisMaxY, configOres.lapisSize, configOres.lapisChance, configOres.lapisDimBlacklist);
        }
        if (configOres.quartzChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 4), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 4), configOres.quartzMinY, configOres.quartzMaxY, configOres.quartzSize, configOres.quartzChance, configOres.quartzDimBlacklist);
        }
        if (configOres.kimberliteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 5), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 5), configOres.kimberliteMinY, configOres.kimberliteMaxY, configOres.kimberliteSize, configOres.kimberliteChance, configOres.kimberliteDimBlacklist);
        }
        if (configOres.berylChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE_VANILLA, 6), HelperFunctions.getStateFromMeta(ORE_SAMPLE_VANILLA, 6), configOres.berylMinY, configOres.berylMaxY, configOres.berylSize, configOres.berylChance, configOres.berylDimBlacklist);
        }
        if (ModConfig.featureControl.modStones)
        {
            IBlockState diorite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);
            IBlockState andesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
            IBlockState granite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);

            GeolosysAPI.registerStoneDeposit(andesite, 2, 70, 40);
            GeolosysAPI.registerStoneDeposit(diorite, 2, 70, 40);
            GeolosysAPI.registerStoneDeposit(granite, 2, 70, 40);
        }
    }

    private void registerGeolosysOreGen()
    {
        if (configOres.hematiteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 0), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 0), configOres.hematiteMinY, configOres.hematiteMaxY, configOres.hematiteSize, configOres.hematiteChance, configOres.hematiteDimBlacklist);
        }
        if (configOres.limoniteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 1), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 1), configOres.limoniteMinY, configOres.limoniteMaxY, configOres.limoniteSize, configOres.limoniteChance, configOres.limoniteDimBlacklist);
        }
        if (configOres.malachiteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 2), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 2), configOres.malachiteMinY, configOres.malachiteMaxY, configOres.malachiteSize, configOres.malachiteChance, configOres.malachiteDimBlacklist);
        }
        if (configOres.azuriteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 3), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 3), configOres.azuriteMinY, configOres.azuriteMaxY, configOres.azuriteSize, configOres.azuriteChance, configOres.azuriteDimBlacklist);
        }
        if (configOres.cassiteriteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 4), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 4), configOres.cassiteriteMinY, configOres.cassiteriteMaxY, configOres.cassiteriteSize, configOres.cassiteriteChance, configOres.cassiteriteDimBlacklist);
        }
        if (configOres.tealliteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 5), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 5), configOres.tealliteMinY, configOres.tealliteMaxY, configOres.tealliteSize, configOres.tealliteChance, configOres.tealliteDimBlacklist);
        }
        if (configOres.galenaChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 6), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 6), configOres.galenaMinY, configOres.galenaMaxY, configOres.galenaSize, configOres.galenaChance, configOres.galenaDimBlacklist);
        }
        if (configOres.bauxiteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 7), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 7), configOres.bauxiteMinY, configOres.bauxiteMaxY, configOres.bauxiteSize, configOres.bauxiteChance, configOres.bauxiteDimBlacklist);
        }
        if (configOres.platinumChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 8), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 8), configOres.platinumMinY, configOres.platinumMaxY, configOres.platinumSize, configOres.platinumChance, configOres.platinumDimBlacklist);
        }
        if (configOres.autuniteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 9), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 9), configOres.autuniteMinY, configOres.autuniteMaxY, configOres.autuniteSize, configOres.autuniteChance, configOres.autuniteDimBlacklist);
        }
        if (configOres.sphaleriteChance > 0)
        {
            GeolosysAPI.registerMineralDeposit(HelperFunctions.getStateFromMeta(ORE, 10), HelperFunctions.getStateFromMeta(ORE_SAMPLE, 10), configOres.sphaleriteMinY, configOres.sphaleriteMaxY, configOres.sphaleriteSize, configOres.sphaleriteChance, configOres.sphaleriteDimBlacklist);
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

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event)
    {
        GeolosysAPI.readFromFile();
    }
}
