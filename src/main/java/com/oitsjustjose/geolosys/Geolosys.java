package com.oitsjustjose.geolosys;

import java.io.File;
import java.util.Collection;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.client.ClientProxy;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.config.OreConfig;
import com.oitsjustjose.geolosys.common.event.ManualGifting;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.FeatureStripper;
import com.oitsjustjose.geolosys.common.world.capability.GeolosysCapProvider;
import com.oitsjustjose.geolosys.common.world.capability.GeolosysCapStorage;
import com.oitsjustjose.geolosys.common.world.capability.GeolosysCapability;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MODID)
@SuppressWarnings("deprecation")
public class Geolosys {
    private static Geolosys instance;
    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public Logger LOGGER = LogManager.getLogger();

    public Geolosys() {
        instance = this;

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ManualGifting());

        this.configSetup();
    }

    public static Geolosys getInstance() {
        return instance;
    }

    private void configSetup() {
        ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.COMMON_CONFIG);
        CommonConfig.loadConfig(CommonConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("geolosys-common.toml"));
    }

    public void setup(final FMLCommonSetupEvent event) {

        CapabilityManager.INSTANCE.register(IGeolosysCapability.class, new GeolosysCapStorage(),
                GeolosysCapability::new);

        if (CommonConfig.DISABLE_VANILLA_ORE_GEN.get()) {
            MinecraftForge.EVENT_BUS.register(new FeatureStripper());
        }

        OreConfig.setup(new File("./config"));
        OreConfig.getInstance().init();
        GeolosysAPI.init();
        proxy.init();
    }

    @SubscribeEvent
    public void attachCap(AttachCapabilitiesEvent<World> event) {
        event.addCapability(new ResourceLocation(Constants.MODID, "pluton"), new GeolosysCapProvider());
        LOGGER.info("Geolosys capability attached for " + Utils.dimensionToString(event.getObject()));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onHover(ItemTooltipEvent event) {
        if (!ClientConfig.ENABLE_TAG_DEBUG.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.gameSettings.advancedItemTooltips) {
            Collection<ResourceLocation> tags = ItemTags.getCollection().getOwningTags(event.getItemStack().getItem());
            if (tags.size() > 0) {
                for (ResourceLocation tag : tags) {
                    event.getToolTip().add(new StringTextComponent("\u00A78#" + tag.toString() + "\u00A7r"));
                }
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            BlockInit.getInstance().registerBlocks(blockRegistryEvent);
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> itemRegistryEvent) {
            // Register BlockItems (formerly known as ItemBlocks) for each block initialized
            BlockInit.getInstance().registerBlockItems(itemRegistryEvent);
            ItemInit.getInstance().register(itemRegistryEvent);
        }
    }
}
