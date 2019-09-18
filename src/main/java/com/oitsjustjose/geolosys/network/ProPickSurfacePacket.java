package com.oitsjustjose.geolosys.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ProPickSurfacePacket implements IMessage
{
    // A default constructor is always required
    public ProPickSurfacePacket()
    {
    }

    public String blockNameLocalized;
    public String direction;

    public ProPickSurfacePacket(String blockNameLocalized, EnumFacing direction)
    {
        this.blockNameLocalized = blockNameLocalized;
        this.direction = direction.getName();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.blockNameLocalized);
        ByteBufUtils.writeUTF8String(buf, this.direction);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.blockNameLocalized = ByteBufUtils.readUTF8String(buf);
        this.direction = ByteBufUtils.readUTF8String(buf);
    }
}
