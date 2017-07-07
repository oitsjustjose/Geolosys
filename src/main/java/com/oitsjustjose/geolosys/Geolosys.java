package com.oitsjustjose.geolosys;

import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.items.ItemCluster;
import com.oitsjustjose.geolosys.util.ClientRegistry;
import com.oitsjustjose.geolosys.util.Config;
import com.oitsjustjose.geolosys.world.WorldGenIronOverride;
import com.oitsjustjose.geolosys.world.WorldGenPluton;
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
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.ORE_GEN_BUS.register(new WorldGenIronOverride());
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(0), config.weightIron), 0);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(1), config.weightIron), 1);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(2), config.weightCopper), 2);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(3), config.weightCopper), 3);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(4), config.weightTin), 4);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(5), config.weightTin), 5);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(6), config.weightSilverLead), 6);
//        GameRegistry.registerWorldGenerator(new WorldGenPluton(ore.getStateFromMeta(7), config.weightAluminum), 7);

    }
}