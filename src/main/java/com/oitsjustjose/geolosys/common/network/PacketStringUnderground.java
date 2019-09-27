package com.oitsjustjose.geolosys.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStringUnderground
{
    public String depositName;
    public String direction;

    public PacketStringUnderground(PacketBuffer buf)
    {
        this.depositName = buf.readString();
        this.direction = buf.readString();
    }

    public PacketStringUnderground(String d1, String d2)
    {
        this.depositName = d1;
        this.direction = d2;
    }

    public static PacketStringUnderground decode(PacketBuffer buf)
    {
        return new PacketStringUnderground(buf);
    }

    public static void encode(PacketStringUnderground msg, PacketBuffer buf)
    {
        buf.writeString(msg.depositName);
        buf.writeString(msg.direction);
    }


    public void handleServer(Supplier<NetworkEvent.Context> context)
    {
        context.get().setPacketHandled(true);
    }

    public static void handleClient(PacketStringUnderground msg, Supplier<NetworkEvent.Context> context)
    {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            context.get().enqueueWork(() -> {
                sendProspectingMessage(Minecraft.getInstance().player, msg.depositName, msg.direction);
            });
        }
        context.get().setPacketHandled(true);
    }

    private static void sendProspectingMessage(PlayerEntity player, Object... messageDecorators)
    {
        TranslationTextComponent msg = new TranslationTextComponent("geolosys.pro_pick.tooltip.found", messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}