package com.oitsjustjose.geolosys;

import org.apache.logging.log4j.Logger;

import com.oitsjustjose.geolosys.blocks.BlockOre;
import com.oitsjustjose.geolosys.proxy.CommonProxy;
import com.oitsjustjose.geolosys.util.Config;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Lib.MODID, name = Lib.NAME, version = Lib.VERSION, guiFactory = Lib.GUIFACTORY, acceptedMinecraftVersions = "1.12")
public class Geolosys
{
	@Instance(Lib.MODID)
	public static Geolosys instance;
	@SidedProxy(clientSide = Lib.CLIENT_PROXY, serverSide = Lib.COMMON_PROXY, modId = Lib.MODID)
	public static CommonProxy proxy;

	// Logger & Configs, statically accessible.
	public static Logger LOGGER;
	public static Config config;
	public static Block ore;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		instance = this;
		LOGGER = event.getModLog();
		config = new Config(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(config);

		ore = new BlockOre();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.register(Item.getItemFromBlock(ore));
	}
}