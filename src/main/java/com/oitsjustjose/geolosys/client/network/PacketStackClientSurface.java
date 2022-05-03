package com.oitsjustjose.geolosys.client.network;

import com.oitsjustjose.geolosys.common.network.PacketHelpers;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStackClientSurface {
    public static void handleClient(PacketStackSurface msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                sendProspectingMessage(mc.player, PacketHelpers.messagify(msg.blocks));
            });
        }
        context.get().setPacketHandled(true);
    }

    private static void sendProspectingMessage(Player player, Object... messageDecorators) {
        TranslatableComponent msg = new TranslatableComponent("geolosys.pro_pick.tooltip.found_surface",
                messageDecorators);
        player.displayClientMessage(msg, true);
    }
}
