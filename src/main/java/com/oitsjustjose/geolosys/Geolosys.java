package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.util.ClientRegistry;
import com.oitsjustjose.geolosys.util.Config;
import com.oitsjustjose.geolosys.util.Lib;
import com.oitsjustjose.geolosys.world.WorldGenOverride;
import com.oitsjustjose.geolosys.world.WorldGenPluton;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
    public static BlockOre ore;
    public static Item cluster;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        instance = this;
        LOGGER = event.getModLog();
        config = new Config(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(config);
        clientRegistry = new ClientRegistry();
        MinecraftForge.EVENT_BUS.register(clientRegistry);

        ore = new BlockOre();
        cluster = new ItemCluster();

        IBlockState andesite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
        IBlockState diorite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE);
        IBlockState granite = Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE);

        WorldGenPluton.addOreGen(Blocks.DIAMOND_ORE.getDefaultState(), 18, 2, 16, 1, 12);
        WorldGenPluton.addOreGen(Blocks.GOLD_ORE.getDefaultState(), 32, 2, 36, 1, 16);
        WorldGenPluton.addOreGen(Blocks.LAPIS_ORE.getDefaultState(), 18, 2, 24, 1, 20);
        WorldGenPluton.addOreGen(Blocks.REDSTONE_ORE.getDefaultState(), 48, 2, 32, 1, 16);
        WorldGenPluton.addOreGen(Blocks.COAL_ORE.getDefaultState(), 64, 2, 70, 1, 16);
        WorldGenPluton.addOreGen(andesite, 72, 2, 70, 1, 20);
        WorldGenPluton.addOreGen(diorite, 72, 2, 70, 1, 20);
        WorldGenPluton.addOreGen(granite, 72, 2, 70, 1, 20);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(0), config.clusterSizeHematite, 35, 65, config.frequencyHematite, 20);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(1), config.clusterSizeLimonite, 0, 35, config.frequencyLimonite, 10);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(2), config.clusterSizeMalachite, 30, 65, config.frequencyMalachite, 20);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(3), config.clusterSizeAzurite, 0, 35, config.frequencyAzurite, 10);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(4), config.clusterSizeCassiterite, 44, 68, config.frequencyCassiterite, 20);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(5), config.clusterSizeSphalerite, 8, 43, config.frequencySphalerite, 10);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(6), config.clusterSizeGalena, 0, 50, config.frequencyGalena, 20);
        WorldGenPluton.addOreGen(ore.getStateFromMeta(7), config.clusterSizeBauxite, 45, 70, config.frequencyBauxite, 20);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.ORE_GEN_BUS.register(new WorldGenOverride());
        GameRegistry.registerWorldGenerator(new WorldGenPluton(), 0);
    }
}