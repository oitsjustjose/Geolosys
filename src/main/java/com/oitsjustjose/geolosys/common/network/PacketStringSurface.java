package com.oitsjustjose.geolosys.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketStringSurface
{
    public String depositName;

    public PacketStringSurface(PacketBuffer buf)
    {
        this.depositName = buf.readString();
    }

    public PacketStringSurface(String data)
    {
        this.depositName = data;
    }

    public static PacketStringSurface decode(PacketBuffer buf)
    {
        return new PacketStringSurface(buf);
    }

    public static void encode(PacketStringSurface msg, PacketBuffer buf)
    {
        buf.writeString(msg.depositName);
    }


    public void handleServer(Supplier<NetworkEvent.Context> context)
    {
        context.get().setPacketHandled(true);
    }

    public static void handleClient(PacketStringSurface msg, Supplier<NetworkEvent.Context> context)
    {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT)
        {
            context.get().enqueueWork(() -> {
                sendProspectingMessage(Minecraft.getInstance().player, msg.depositName);
            });
        }
        context.get().setPacketHandled(true);
    }

    private static void sendProspectingMessage(PlayerEntity player, Object... messageDecorators)
    {
        TranslationTextComponent msg = new TranslationTextComponent("geolosys.pro_pick.tooltip.found_surface", messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}