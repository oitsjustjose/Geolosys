package com.oitsjustjose.geolosys.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.items.ProPickItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ProPickOverlay {
    @SubscribeEvent
    public static void onDrawScreen(RenderGuiOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null || mc.options.renderDebug || mc.options.renderDebugCharts) {
            return;
        }

        if (mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ProPickItem || mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem() instanceof ProPickItem) {
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            int seaLvl = mc.player.level().getSeaLevel();
            int level = (int) (seaLvl - mc.player.getY());

            mc.font.drawInBatch(
                    getCompForLevel(mc.player, level),
                    (float) ClientConfig.PROPICK_HUD_X.get(),
                    (float) ClientConfig.PROPICK_HUD_Y.get(),
                    0xFFFFFFFF,
                    false,
                    event.getGuiGraphics().pose().last().pose(),
                    event.getGuiGraphics().bufferSource(),
                    Font.DisplayMode.NORMAL,
                    0,
                    15728880
            );
        }
    }

    private static MutableComponent getCompForLevel(LocalPlayer player, int level) {
        TranslatableContents contents;
        var levelArgs = new Object[]{Math.abs(level)};
        if (level < 0) {
            contents = new TranslatableContents("geolosys.pro_pick.depth.above", "", levelArgs);
        } else if (level == 0) {
            contents = new TranslatableContents("geolosys.pro_pick.depth.at", "", new Object[]{});
        } else {
            contents = new TranslatableContents("geolosys.pro_pick.depth.below", "", levelArgs);
        }

        try {
            return contents.resolve(null, player, 0);
        } catch (CommandSyntaxException e) {
            return Component.empty().append(e.getMessage());
        }
    }
}
