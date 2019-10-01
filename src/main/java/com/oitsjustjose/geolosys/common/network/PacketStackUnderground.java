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

public class PacketStackUnderground
{
    public ItemStack stack;
    public String direction;

    public PacketStackUnderground(PacketBuffer buf)
    {
        this.stack = buf.readItemStack();
        this.direction = buf.readString();
    }

    public PacketStackUnderground(ItemStack d1, String d2)
    {
        this.stack = d1;
        this.direction = d2;
    }

    public static PacketStackUnderground decode(PacketBuffer buf)
    {
        return new PacketStackUnderground(buf);
    }

    public static void encode(PacketStackUnderground msg, PacketBuffer buf)
    {
        buf.writeItemStack(msg.stack);
        buf.writeString(msg.direction);
    }

    public void handleServer(Supplier<NetworkEvent.Context> context)
    {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(PacketStackUnderground msg, Supplier<NetworkEvent.Context> context)
    {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            context.get().enqueueWork(() -> {
                sendProspectingMessage(Minecraft.getInstance().player, msg.stack.getDisplayName(), msg.direction);
            });
        }
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void sendProspectingMessage(PlayerEntity player, Object... messageDecorators)
    {
        TranslationTextComponent msg = new TranslationTextComponent("geolosys.pro_pick.tooltip.found",
                messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}