package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.client.ClientRegistry;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.blocks.BlockOre;
import com.oitsjustjose.geolosys.common.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.common.blocks.BlockSample;
import com.oitsjustjose.geolosys.common.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ConfigParser;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemCluster;
import com.oitsjustjose.geolosys.common.items.ItemCoal;
import com.oitsjustjose.geolosys.common.items.ItemCoalCoke;
import com.oitsjustjose.geolosys.common.items.ItemFieldManual;
import com.oitsjustjose.geolosys.common.items.ItemIngot;
import com.oitsjustjose.geolosys.common.items.ItemProPick;
import com.oitsjustjose.geolosys.common.util.Recipes;
import com.oitsjustjose.geolosys.common.world.ChunkData;
import com.oitsjustjose.geolosys.common.world.OreGenerator;
import com.oitsjustjose.geolosys.common.world.StoneGenerator;
import com.oitsjustjose.geolosys.common.world.VanillaWorldGenOverride;
import com.oitsjustjose.geolosys.compat.CompatLoader;

import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Geolosys.MODID, name = "Geolosys", version = Geolosys.VERSION, acceptedMinecraftVersions = "1.12", dependencies = "after:immersiveengineering@[0.12,);after:contenttweaker;")
public class Geolosys
{
    public static final String MODID = "geolosys";
    public static final String VERSION = "@VERSION@";
    @SidedProxy(clientSide = "com.oitsjustjose.geolosys.client.ClientProxy", serverSide = "com.oitsjustjose.geolosys.common.CommonProxy")
    public static CommonProxy proxy;
    @Instance(Geolosys.MODID)
    private static Geolosys instance;
    // Logger & Configs, statically accessible.
    public Logger LOGGER;
    public ConfigOres configOres;
    public ClientRegistry clientRegistry;
    public ChunkData chunkOreGen;

    public BlockOre ORE;
    public BlockOreVanilla ORE_VANILLA;
    public BlockSample ORE_SAMPLE;
    public BlockSampleVanilla ORE_SAMPLE_VANILLA;

    public Item CLUSTER;
    public Item INGOT;
    public Item COAL;
    public Item COAL_COKE;
    public ItemProPick PRO_PICK;
    public Item ALMANAC;

    public static Geolosys getInstance()
    {
        return instance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        clientRegistry = new ClientRegistry();
        MinecraftForge.EVENT_BUS.register(clientRegistry);
        MinecraftForge.EVENT_BUS.register(new ModConfig.EventHandler());
        chunkOreGen = new ChunkData();

        ORE = new BlockOre();
        ORE_SAMPLE = new BlockSample();
        ORE_VANILLA = new BlockOreVanilla();
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
        if (ModConfig.prospecting.enableProPick)
        {
            PRO_PICK = new ItemProPick();
        }
        if (Loader.isModLoaded("immersiveengineering") && ModConfig.compat.enableIECompat
                && ModConfig.featureControl.enableCoals)
        {
            COAL_COKE = new ItemCoalCoke();
        }
        configOres = new ConfigOres(event.getModConfigurationDirectory());
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        MinecraftForge.ORE_GEN_BUS.register(new VanillaWorldGenOverride());
        configOres.init();
        ConfigParser.init();
        CompatLoader.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        configOres.postInit();
        if (ModConfig.featureControl.enableSmelting)
        {
            Recipes.init(configOres, CLUSTER);
        }
        proxy.postInit(event);
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
        GameRegistry.registerWorldGenerator(new StoneGenerator(), 100);
    }
}
