package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.blocks.BlockOreSample;
import com.oitsjustjose.geolosys.blocks.BlockOreSampleVanilla;
import com.oitsjustjose.geolosys.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.items.ItemIngot;
import com.oitsjustjose.geolosys.util.ClientRegistry;
import com.oitsjustjose.geolosys.util.Config;
import com.oitsjustjose.geolosys.util.ConfigParser;
import com.oitsjustjose.geolosys.util.Lib;
import com.oitsjustjose.geolosys.world.ChunkData;
import com.oitsjustjose.geolosys.world.OreGenerator;
import com.oitsjustjose.geolosys.world.StoneGenerator;
import com.oitsjustjose.geolosys.world.VanillaWorldGenOverride;
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
    public static ChunkData chunkOreGen;
    private ConfigParser configParser;


    public static BlockOre ORE;
    public static BlockOreVanilla ORE_VANILLA;
    public static BlockOreSample ORE_SAMPLE;
    public static BlockOreSampleVanilla ORE_SAMPLE_VANILLA;

    public static Item CLUSTER;
    public static Item INGOT;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        config = new Config(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(config);
        clientRegistry = new ClientRegistry();
        MinecraftForge.EVENT_BUS.register(clientRegistry);
        chunkOreGen = new ChunkData();

        ORE = new BlockOre();
        ORE_VANILLA = new BlockOreVanilla();
        ORE_SAMPLE = new BlockOreSample();
        ORE_SAMPLE_VANILLA = new BlockOreSampleVanilla();
        CLUSTER = new ItemCluster();
        if (config.enableIngots)
            INGOT = new ItemIngot();

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
        if (!config.enableIngots)
        {
            String mat = "";
            try
            {
                mat = "ingotIron";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 0), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotGold";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 1), OreDictionary.getOres(mat).get(0), 1.0F);
                mat = "ingotCopper";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 2), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotTin";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 3), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotSilver";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 4), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotLead";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 5), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotAluminum";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 6), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotNickel";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 7), OreDictionary.getOres(mat).get(0), 0.7F);
                mat = "ingotPlatinum";
                GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 8), OreDictionary.getOres(mat).get(0), 0.7F);
            }
            catch (NullPointerException | IndexOutOfBoundsException e)
            {
                LOGGER.fatal("Could not find " + mat + " in the OreDictionary. Please enable Geolosys' Ingots in the config or add a mod which adds it.");
            }
        }
        configParser = new ConfigParser();
        registerUserOreGen();
        registerUserStoneGen();
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
        GameRegistry.registerWorldGenerator(new StoneGenerator(), 100);
    }

    private void registerVanillaOreGen()
    {
        if (config.modCoal)
            OreGenerator.addOreGen(ORE_VANILLA.getStateFromMeta(0), config.clusterSizeCoal, 48, 70, config.chanceCoal);
        if (config.modRedstone)
            OreGenerator.addOreGen(ORE_VANILLA.getStateFromMeta(1), config.clusterSizeCinnabar, 5, 12, config.chanceCinnabar);
        if (config.modGold)
            OreGenerator.addOreGen(ORE_VANILLA.getStateFromMeta(2), config.clusterSizeGold, 5, 30, config.chanceGold);
        if (config.modLapis)
            OreGenerator.addOreGen(ORE_VANILLA.getStateFromMeta(3), config.clusterSizeLapis, 10, 24, config.chanceLapis);
        if (config.modQuartz)
            OreGenerator.addOreGen(ORE_VANILLA.getStateFromMeta(4), config.clusterSizeQuartz, 6, 29, config.chanceQuartz);
        if (config.modDiamond)
            OreGenerator.addOreGen(ORE_VANILLA.getStateFromMeta(5), config.clusterSizeKimberlite, 2, 15, config.chanceKimberlite);

        if (config.modStones)
        {

            IBlockState diorite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);IBlockState andesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
            IBlockState granite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);

            StoneGenerator.addStoneGen(andesite, 2, 70, 40);
            StoneGenerator.addStoneGen(diorite, 2, 70, 40);
            StoneGenerator.addStoneGen(granite, 2, 70, 40);
        }
    }

    private void registerGeolosysOreGen()
    {
        if (config.enableHematite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(0), config.clusterSizeHematite, 32, 60, config.chanceHematite);
        if (config.enableLimonite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(1), config.clusterSizeLimonite, 6, 32, config.chanceLimonite);
        if (config.enableMalachite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(2), config.clusterSizeMalachite, 39, 44, config.chanceMalachite);
        if (config.enableAzurite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(3), config.clusterSizeAzurite, 12, 44, config.chanceAzurite);
        if (config.enableCassiterite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(4), config.clusterSizeCassiterite, 44, 68, config.chanceCassiterite);
        if (config.enableTeallite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(5), config.clusterSizeTeallite, 8, 43, config.chanceTeallite);
        if (config.enableGalena)
            OreGenerator.addOreGen(ORE.getStateFromMeta(6), config.clusterSizeGalena, 16, 50, config.chanceGalena);
        if (config.enableBauxite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(7), config.clusterSizeBauxite, 45, 70, config.chanceBauxite);
        if (config.enablePlatinum)
            OreGenerator.addOreGen(ORE.getStateFromMeta(8), config.clusterSizePlatinum, 3, 25, config.chancePlatinum);
        if (config.enableAutunite)
            OreGenerator.addOreGen(ORE.getStateFromMeta(9), config.clusterSizeUranium, 8, 33, config.chanceUranium);
    }

    private void registerUserOreGen()
    {
        for(ConfigParser.Entry e : configParser.getUserOreEntries())
            OreGenerator.addOreGen(e.getState(), e.getSize(), e.getMinY(), e.getMaxY(), e.getChancePerChunk());
    }


    private void registerUserStoneGen()
    {
        for (ConfigParser.Entry e : configParser.getUserStoneEntries())
            StoneGenerator.addStoneGen(e.getState(), e.getMinY(), e.getMaxY(), e.getChancePerChunk());
    }
}
