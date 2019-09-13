package com.oitsjustjose.geolosys;

import java.util.ArrayList;

import com.oitsjustjose.geolosys.blocks.Types;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
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

    private static ArrayList<Block> blocks = new ArrayList<>();

    public Geolosys()
    {
        for (Types.Vanilla vanillaType : Types.Vanilla.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(vanillaType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName("geolosys", "ore_" + vanillaType.getName());
            blocks.add(block);
        }

        for (Types.Modded moddedType : Types.Modded.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(moddedType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName("geolosys", "ore_" + moddedType.getName());
            blocks.add(block);
        }
        // Block testBlock = new Block(Properties.create(Material.WOOL, MaterialColor.WOOL)
        // .hardnessAndResistance(0.5F, 0.0F).sound(SoundType.SLIME)).setRegistryName("geolosys", "test_block");
        // blocks.add(testBlock);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register ourselves for server and other game events we are interested in
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
            for (Block b : blocks)
            {
                blockRegistryEvent.getRegistry().register(b);
            }
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            for (Block b : blocks)
            {
                Item iBlock = new BlockItem(b, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS))
                        .setRegistryName(b.getRegistryName());
                itemRegistryEvent.getRegistry().register(iBlock);
            }
        }
    }
}
