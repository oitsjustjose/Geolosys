package com.oitsjustjose.geolosys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oitsjustjose.geolosys.client.ClientRegistry;
import com.oitsjustjose.geolosys.client.TranslationManager;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.blocks.BlockOre;
import com.oitsjustjose.geolosys.common.blocks.BlockOreVanilla;
import com.oitsjustjose.geolosys.common.blocks.BlockSample;
import com.oitsjustjose.geolosys.common.blocks.BlockSampleVanilla;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ConfigParser;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.*;
import com.oitsjustjose.geolosys.common.util.MineralTracker;
import com.oitsjustjose.geolosys.common.util.Recipes;
import com.oitsjustjose.geolosys.common.util.Utils;
import com.oitsjustjose.geolosys.common.world.ChunkData;
import com.oitsjustjose.geolosys.common.world.OreGenerator;
import com.oitsjustjose.geolosys.common.world.StoneGenerator;
import com.oitsjustjose.geolosys.common.world.VanillaWorldGenOverride;
import com.oitsjustjose.geolosys.compat.ModMaterials;
import com.oitsjustjose.geolosys.compat.OreConverter;
import com.oitsjustjose.geolosys.compat.ie.IECompat;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.io.*;

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
        MinecraftForge.EVENT_BUS.register(new ModConfig.EventHandler());
        MinecraftForge.EVENT_BUS.register(new MineralTracker());
        chunkOreGen = new ChunkData();
        if (event.getSide() == Side.CLIENT)
        {
            TranslationManager.init();
        }

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
        if (ModConfig.featureControl.retroReplace)
        {
            MinecraftForge.EVENT_BUS.register(new OreConverter());
        }

        registerGeolosysOreGen();
        registerVanillaOreGen();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        MinecraftForge.ORE_GEN_BUS.register(new VanillaWorldGenOverride());

        if (Loader.isModLoaded("immersiveengineering") && ModConfig.featureControl.enableIECompat)
        {
            IECompat.init();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        if (ModConfig.featureControl.enableSmelting)
        {
            Recipes.init(configOres, CLUSTER);
        }
        ConfigParser.init();
        ModMaterials.init();
        GameRegistry.registerWorldGenerator(new OreGenerator(), 0);
        GameRegistry.registerWorldGenerator(new StoneGenerator(), 100);
    }

    private void registerVanillaOreGen()
    {

        if (configOres.coal.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 0), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 0), configOres.coal);
        }
        if (configOres.cinnabar.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 1), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 1), configOres.cinnabar);
        }
        if (configOres.gold.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 2), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 2), configOres.gold);
        }
        if (configOres.lapis.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 3), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 3), configOres.lapis);
        }
        if (configOres.quartz.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 4), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 4), configOres.quartz);
        }
        if (configOres.kimberlite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 5), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 5), configOres.kimberlite);
        }
        if (configOres.beryl.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE_VANILLA, 6), Utils.getStateFromMeta(ORE_SAMPLE_VANILLA, 6), configOres.beryl);
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
        if (configOres.hematite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 0), Utils.getStateFromMeta(ORE_SAMPLE, 0), configOres.hematite);
        }
        if (configOres.lapis.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 1), Utils.getStateFromMeta(ORE_SAMPLE, 1), configOres.limonite);
        }
        if (configOres.malachite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 2), Utils.getStateFromMeta(ORE_SAMPLE, 2), configOres.malachite);
        }
        if (configOres.azurite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 3), Utils.getStateFromMeta(ORE_SAMPLE, 3), configOres.azurite);
        }
        if (configOres.cassiterite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 4), Utils.getStateFromMeta(ORE_SAMPLE, 4), configOres.cassiterite);
        }
        if (configOres.teallite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 5), Utils.getStateFromMeta(ORE_SAMPLE, 5), configOres.teallite);
        }
        if (configOres.galena.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 6), Utils.getStateFromMeta(ORE_SAMPLE, 6), configOres.galena);
        }
        if (configOres.bauxite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 7), Utils.getStateFromMeta(ORE_SAMPLE, 7), configOres.bauxite);
        }
        if (configOres.platinum.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 8), Utils.getStateFromMeta(ORE_SAMPLE, 8), configOres.platinum);
        }
        if (configOres.autunite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 9), Utils.getStateFromMeta(ORE_SAMPLE, 9), configOres.autunite);
        }
        if (configOres.sphalerite.getChance() > 0)
        {
            GeolosysAPI.registerMineralDeposit(Utils.getStateFromMeta(ORE, 10), Utils.getStateFromMeta(ORE_SAMPLE, 10), configOres.sphalerite);
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
                br.close();
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
}
