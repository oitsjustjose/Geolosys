package com.oitsjustjose.geolosys;

import java.util.Map.Entry;

import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.items.ItemInit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("geolosys")
public class Geolosys
{
    // Directly reference a log4j logger.
    public Logger LOGGER = LogManager.getLogger();
    private static Geolosys instance;

    public Geolosys()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        instance = this;
    }

    public static Geolosys getInstance()
    {
        return instance;
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            BlockInit.getInstance().registerBlocks(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            // Register BlockItems (formerly known as ItemBlocks) for each block initialized
            BlockInit.getInstance().registerBlockItems(itemRegistryEvent);
            ItemInit.getInstance().register(itemRegistryEvent);
        }

        @SubscribeEvent
        public static void onFuelRegistry(final FurnaceFuelBurnTimeEvent fuelBurnoutEvent)
        {
            for (Entry<Item, Integer> fuelPair : ItemInit.getInstance().getModFuels().entrySet())
            {
                if (fuelBurnoutEvent.getItemStack().getItem().equals(fuelPair.getKey()))
                {
                    Geolosys.getInstance().LOGGER.info("Itemstack " + fuelBurnoutEvent.getItemStack().getDisplayName() + " should be burnable for " + fuelPair.getValue() + " ticks");
                    fuelBurnoutEvent.setBurnTime(fuelPair.getValue());
                }
            }
        }
    }
}
