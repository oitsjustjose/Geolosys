package com.oitsjustjose.geolosys;

import java.util.Map.Entry;

import com.oitsjustjose.geolosys.blocks.BlockInit;
import com.oitsjustjose.geolosys.items.ItemInit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
    private static final Logger LOGGER = LogManager.getLogger();

    public Geolosys()
    {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
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
            for (Block b : BlockInit.getInstance().getModBlocks())
            {
                blockRegistryEvent.getRegistry().register(b);
            }
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            // Register BlockItems (formerly known as ItemBlocks) for each block initialized
            for (Block b : BlockInit.getInstance().getModBlocks())
            {
                Item iBlock = new BlockItem(b, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                        .setRegistryName(b.getRegistryName());
                itemRegistryEvent.getRegistry().register(iBlock);
            }

            for (Item i : ItemInit.getInstance().getModItems())
            {
                itemRegistryEvent.getRegistry().register(i);
            }
        }

        @SubscribeEvent
        public static void onFuelRegistry(final FurnaceFuelBurnTimeEvent fuelBurnoutEvent)
        {
            for (Entry<Item, Integer> fuelPair : ItemInit.getInstance().getModFuels().entrySet())
            {
                if (fuelBurnoutEvent.getItemStack().getItem() == fuelPair.getKey())
                {
                    fuelBurnoutEvent.setBurnTime(fuelPair.getValue());
                }
            }
        }
    }
}
