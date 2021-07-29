package com.oitsjustjose.geolosys.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStackSurface {

    public ItemStack stack;

    public PacketStackSurface(PacketBuffer buf) {
        this.stack = buf.readItemStack();
    }

    public PacketStackSurface(ItemStack data) {
        this.stack = data;
    }

    public static PacketStackSurface decode(PacketBuffer buf) {
        return new PacketStackSurface(buf);
    }

    public static void encode(PacketStackSurface msg, PacketBuffer buf) {
        buf.writeItemStack(msg.stack);
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(PacketStackSurface msg,
            Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                sendProspectingMessage(mc.player, msg.stack.getDisplayName());
            });
        }
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void sendProspectingMessage(PlayerEntity player, Object... messageDecorators) {
        TranslationTextComponent msg = new TranslationTextComponent(
                "geolosys.pro_pick.tooltip.found_surface", messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}
