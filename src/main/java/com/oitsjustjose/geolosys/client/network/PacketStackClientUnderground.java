package com.oitsjustjose.geolosys.client.network;

import com.oitsjustjose.geolosys.common.network.PacketHelpers;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;
import com.oitsjustjose.geolosys.common.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStackClientUnderground {
    public static void handleClient(PacketStackUnderground msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null) {
                    sendProspectingMessage(mc.player, PacketHelpers.messagify(msg.blocks), msg.direction);
                }
            });
        }
        context.get().setPacketHandled(true);
    }

    private static void sendProspectingMessage(LocalPlayer player, Object... messageDecorators) {
        MutableComponent msg = Utils.tryTranslate("geolosys.pro_pick.tooltip.found", messageDecorators);
        player.displayClientMessage(msg, true);
    }
}
