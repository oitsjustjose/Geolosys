package com.oitsjustjose.geolosys;

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

import java.util.ArrayList;

@Mod(modid = Lib.MODID, name = Lib.NAME, version = Lib.VERSION, guiFactory = Lib.GUIFACTORY, acceptedMinecraftVersions = "1.12")
public class Geolosys
{
    @Instance(Lib.MODID)
    private static Geolosys instance;

    // Logger & Configs, statically accessible.
    public Logger LOGGER;
    public Config config;
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
        config = new Config(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(config);
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
        if (config.enableIngots)
            INGOT = new ItemIngot();
        if (config.enableProPick)
            PRO_PICK = new ItemProPick();

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
        if (config.enableSmelting)
        {
            GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 0), new ItemStack(Items.IRON_INGOT, 1, 0), 0.7F);
            GameRegistry.addSmelting(new ItemStack(CLUSTER, 1, 1), new ItemStack(Items.GOLD_INGOT, 1, 0), 1.0F);

            if (config.modIron)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_IRON), "ingotIron");
            }
            if (config.modGold)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_GOLD), "ingotGold");
            }
            if (config.enableMalachite || config.enableAzurite)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_COPPER), "ingotCopper");
            }
            if (config.enableCassiterite || config.enableTeallite)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_TIN), "ingotTin");
            }
            if (config.enableGalena)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_SILVER), "ingotSilver");
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_LEAD), "ingotLead");
            }
            if (config.enableBauxite)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_ALUMINUM), "ingotAluminum");
            }
            if (config.enableLimonite)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_NICKEL), "ingotNickel");
            }
            if (config.enablePlatinum)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_PLATINUM), "ingotPlatinum");
            }
            if (config.enableSphalerite)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_ZINC), "ingotZinc");
            }
            if (config.enableYellorium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_YELLORIUM), "ingotYellorium");
            }
            if (config.enableOsmium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, ItemCluster.META_OSMIUM), "ingotOsmium");
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
            System.out.println(oreDictName + " has not been added already. Smelting has been skipped.");
        }
    }

    private void registerVanillaOreGen()
    {
        if (config.modCoal)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 0), config.clusterSizeCoal, 48, 70, config.chanceCoal);
        if (config.modRedstone)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 1), config.clusterSizeCinnabar, 5, 12, config.chanceCinnabar);
        if (config.modGold)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 2), config.clusterSizeGold, 5, 30, config.chanceGold);
        if (config.modLapis)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 3), config.clusterSizeLapis, 10, 24, config.chanceLapis);
        if (config.modQuartz)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 4), config.clusterSizeQuartz, 6, 29, config.chanceQuartz);
        if (config.modDiamond)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE_VANILLA, 5), config.clusterSizeKimberlite, 2, 15, config.chanceKimberlite);

        if (config.modStones)
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
        if (config.enableHematite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 0), config.clusterSizeHematite, 32, 60, config.chanceHematite);
        if (config.enableLimonite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 1), config.clusterSizeLimonite, 6, 32, config.chanceLimonite);
        if (config.enableMalachite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 2), config.clusterSizeMalachite, 39, 44, config.chanceMalachite);
        if (config.enableAzurite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 3), config.clusterSizeAzurite, 12, 44, config.chanceAzurite);
        if (config.enableCassiterite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 4), config.clusterSizeCassiterite, 44, 68, config.chanceCassiterite);
        if (config.enableTeallite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 5), config.clusterSizeTeallite, 8, 43, config.chanceTeallite);
        if (config.enableGalena)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 6), config.clusterSizeGalena, 16, 50, config.chanceGalena);
        if (config.enableBauxite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 7), config.clusterSizeBauxite, 45, 70, config.chanceBauxite);
        if (config.enablePlatinum)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 8), config.clusterSizePlatinum, 3, 25, config.chancePlatinum);
        if (config.enableAutunite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 9), config.clusterSizeAutunite, 8, 33, config.chanceAutunite);
        if (config.enableSphalerite)
            OreGenerator.addOreGen(HelperFunctions.getStateFromMeta(ORE, 10), config.clusterSizeSphalerite, 35, 55, config.chanceSphalerite);
    }

    private void registerUserOreGen()
    {
        for (ConfigParser.Entry e : configParser.getUserOreEntries().keySet())
        {
            OreGenerator.addOreGen(e.getState(), e.getSize(), e.getMinY(), e.getMaxY(), e.getChancePerChunk());
            userStates.add(e.getState());
        }
    }

    private void registerUserStoneGen()
    {
        for (ConfigParser.Entry e : configParser.getUserStoneEntries())
            StoneGenerator.addStoneGen(e.getState(), e.getMinY(), e.getMaxY(), e.getChancePerChunk());
    }
}
