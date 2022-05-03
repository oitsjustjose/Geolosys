package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.common.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GeolosysClient {
    public static void setup() {
        MinecraftForge.EVENT_BUS.register(GeolosysClient.class);
        MinecraftForge.EVENT_BUS.register(ProPickOverlay.class);
    }

    @SubscribeEvent
    public void onHover(ItemTooltipEvent event) {
        if (!ClientConfig.ENABLE_TAG_DEBUG.get()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        if (mc.options.advancedItemTooltips) {
            event.getItemStack().getTags().forEach(x -> {
                event.getToolTip().add(new TextComponent("\u00A78#" + x.location() + "\u00A7r"));
            });
        }
    }
}
