package com.oitsjustjose.geolosys.common.network;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class ProspectingPacket
{
    private final String data;

    ProspectingPacket(PacketBuffer buf)
    {
        this.data = buf.readString();
    }

    ProspectingPacket(String data)
    {
        this.data = data;
    }

    void encode(PacketBuffer buf)
    {
        buf.writeString(data);
    }

    void handle(Supplier<NetworkEvent.Context> context)
    {
    }

}