package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketIOreSurface implements IMessage
{
    // A default constructor is always required
    public PacketIOreSurface()
    {
    }

    public String depositName;

    public PacketIOreSurface(String depositName)
    {
        this.depositName = depositName;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.depositName);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.depositName = ByteBufUtils.readUTF8String(buf);
    }
}