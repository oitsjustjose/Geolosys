package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.items.ItemIngot;
import com.oitsjustjose.geolosys.util.ClientRegistry;
import com.oitsjustjose.geolosys.util.Config;
import com.oitsjustjose.geolosys.util.ConfigParser;
import com.oitsjustjose.geolosys.util.Lib;
import com.oitsjustjose.geolosys.world.WorldGenOverride;
import com.oitsjustjose.geolosys.world.WorldGenPluton;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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

@Mod(modid = Lib.MODID, name = Lib.NAME, version = Lib.VERSION, guiFactory = Lib.GUIFACTORY, acceptedMinecraftVersions = "1.12")
public class Geolosys
{
    @Instance(Lib.MODID)
    public static Geolosys instance;

    // Logger & Configs, statically accessible.
    public static Logger LOGGER;
    public static Config config;
    public static ClientRegistry clientRegistry;
    private ConfigParser configParser;

    public static BlockOre ore;

    public static Item cluster;
    public static Item ingot;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        config = new Config(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(config);
        clientRegistry = new ClientRegistry();
        MinecraftForge.EVENT_BUS.register(clientRegistry);

        ore = new BlockOre();
        cluster = new ItemCluster();
        if (config.enableIngots)
            ingot = new ItemIngot();

        registerVanillaOreGen();
        registerGeolosysOreGen();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.ORE_GEN_BUS.register(new WorldGenOverride());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        if (!config.enableIngots)
        {
            String mat = "";
            try
            {
                mat = "ingotIron";
                GameRegistry.addSmelting(new ItemStack(cluster, 1, 0), OreDictionary.getOres("ingotIron").get(0), 0.7F);
                mat = "ingotCopper";
                GameRegistry.addSmelting(new ItemStack(cluster, 1, 1), OreDictionary.getOres("ingotCopper").get(0), 0.7F);
                mat = "ingotTin";
                GameRegistry.addSmelting(new ItemStack(cluster, 1, 2), OreDictionary.getOres("ingotTin").get(0), 0.7F);
                mat = "ingotSilver";
                GameRegistry.addSmelting(new ItemStack(cluster, 1, 3), OreDictionary.getOres("ingotSilver").get(0), 0.7F);
                mat = "ingotLead";
                GameRegistry.addSmelting(new ItemStack(cluster, 1, 4), OreDictionary.getOres("ingotLead").get(0), 0.7F);
                mat = "ingotAluminum";
                GameRegistry.addSmelting(new ItemStack(cluster, 1, 5), OreDictionary.getOres("ingotAluminum").get(0), 0.7F);
            }
            catch (NullPointerException | IndexOutOfBoundsException e)
            {
                LOGGER.fatal("Could not find " + mat + " in the OreDictionary. Please enable Geolosys' Ingots in the config or add a mod which adds it.");
            }
        }
        configParser = new ConfigParser();
        registerUserOreGen();
        GameRegistry.registerWorldGenerator(new WorldGenPluton(), 0);
    }

    private void registerVanillaOreGen()
    {
        if (config.modGold)
            WorldGenPluton.addOreGen(Blocks.GOLD_ORE.getDefaultState(), 20, 5, 30, 1, 12);
        if (config.modDiamond)
            WorldGenPluton.addOreGen(Blocks.DIAMOND_ORE.getDefaultState(), 12, 2, 15, 1, 10);
        if (config.modCoal)
            WorldGenPluton.addOreGen(Blocks.COAL_ORE.getDefaultState(), 64, 2, 70, 1, 12);
        if (config.modRedstone)
            WorldGenPluton.addOreGen(Blocks.REDSTONE_ORE.getDefaultState(), 32, 5, 12, 1, 14);
        if (config.modLapis)
            WorldGenPluton.addOreGen(Blocks.LAPIS_ORE.getDefaultState(), 20, 10, 24, 1, 8);
        if (config.modStones)
        {
            IBlockState andesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
            IBlockState diorite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);
            IBlockState granite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);

            WorldGenPluton.addOreGen(andesite, 72, 2, 70, 1, 40);
            WorldGenPluton.addOreGen(diorite, 72, 2, 70, 1, 40);
            WorldGenPluton.addOreGen(granite, 72, 2, 70, 1, 40);
        }
    }

    private void registerGeolosysOreGen()
    {
        if (config.enableHematite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(0), config.clusterSizeHematite, 35, 65, config.frequencyHematite, config.chanceHematite);
        if (config.enableLimonite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(1), config.clusterSizeLimonite, 0, 35, config.frequencyLimonite, config.chanceLimonite);
        if (config.enableMalachite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(2), config.clusterSizeMalachite, 30, 65, config.frequencyMalachite, config.chanceLimonite);
        if (config.enableAzurite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(3), config.clusterSizeAzurite, 0, 35, config.frequencyAzurite, config.chanceAzurite);
        if (config.enableCassiterite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(4), config.clusterSizeCassiterite, 44, 68, config.frequencyCassiterite, config.chanceCassiterite);
        if (config.enableTeallite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(5), config.clusterSizeTeallite, 8, 43, config.frequencyTeallite, config.chanceTeallite);
        if (config.enableGalena)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(6), config.clusterSizeGalena, 0, 50, config.frequencyGalena, config.chanceGalena);
        if (config.enableBauxite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(7), config.clusterSizeBauxite, 45, 70, config.frequencyBauxite, config.chanceBauxite);
        if (config.enablePlatinum)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(8), config.clusterSizePlatinum, 3, 25, config.frequencyPlatinum, config.chancePlatinum);
        if (config.enableAutunite)
            WorldGenPluton.addOreGen(ore.getStateFromMeta(9), config.clusterSizeUranium, 8, 33, config.frequencyUranium, config.chanceUranium);
    }

    private void registerUserOreGen()
    {
        for (ConfigParser.Entry e : configParser.getUserEntries())
            WorldGenPluton.addOreGen(e.getState(), e.getSize(), e.getMinY(), e.getMaxY(), e.getChunkOccurence(), e.getChancePerChunk());
    }
}