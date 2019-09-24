package com.oitsjustjose.geolosys;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.oitsjustjose.geolosys.api.world.Deposit;
import com.oitsjustjose.geolosys.client.ConfigClient;
import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.PlutonRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Constants.MODID)
public class Geolosys
{
    private static Geolosys instance;
    public Logger LOGGER = LogManager.getLogger();

    public Geolosys()
    {
        instance = this;

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        this.initConfig();
    }

    private void initConfig()
    {
        ModLoadingContext.get().registerConfig(Type.COMMON, ModConfig.COMMON_CONFIG);
        ModConfig.loadConfig(ModConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("geolosys-common.toml"));
    }

    public static Geolosys getInstance()
    {
        return instance;
    }

    public void setup(final FMLCommonSetupEvent event)
    {
        if (ModConfig.DISABLE_VANILLA_ORE_GEN.get())
        {
            for (Biome biome : ForgeRegistries.BIOMES.getValues())
            {
                biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES)
                        .removeAll(biome.getFeatures(GenerationStage.Decoration.UNDERGROUND_ORES));
            }
        }

        PlutonRegistry.getInstance().register();
    }

    @SubscribeEvent
    public void onHover(ItemTooltipEvent event)
    {
        if (!ConfigClient.ENABLE_TAG_DEBUG.get())
        {
            return;
        }
        if (Minecraft.getInstance().gameSettings.advancedItemTooltips)
        {
            Collection<ResourceLocation> tags = ItemTags.getCollection().getOwningTags(event.getItemStack().getItem());
            if (tags.size() > 0)
            {
                for (ResourceLocation tag : tags)
                {
                    event.getToolTip().add(new StringTextComponent("\u00A78#" + tag.toString() + "\u00A7r"));
                }
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
            BlockInit.getInstance().registerBlocks(blockRegistryEvent);
            PlutonRegistry.getInstance().addOrePluton(new Deposit(
                    BlockInit.getInstance().getModBlocks().get("geolosys:limonite_ore").getDefaultState(),
                    BlockInit.getInstance().getModBlocks().get("geolosys:limonite_ore_sample").getDefaultState(), 60,
                    128, 80, 100, new int[]
                    { -1, 1 }, ImmutableList.of(Blocks.STONE.getDefaultState()), 1.0F));
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent)
        {
            // Register BlockItems (formerly known as ItemBlocks) for each block initialized
            BlockInit.getInstance().registerBlockItems(itemRegistryEvent);
            ItemInit.getInstance().register(itemRegistryEvent);
        }
    }
}
