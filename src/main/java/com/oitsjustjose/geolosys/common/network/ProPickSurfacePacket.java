package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProPickSurfacePacket implements IMessage
{
    // A default constructor is always required
    public ProPickSurfacePacket()
    {
    }

    public String blockNameLocalized;

    public ProPickSurfacePacket(String blockNameLocalized)
    {
        this.blockNameLocalized = blockNameLocalized;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.blockNameLocalized);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.blockNameLocalized = ByteBufUtils.readUTF8String(buf);
    }
}
