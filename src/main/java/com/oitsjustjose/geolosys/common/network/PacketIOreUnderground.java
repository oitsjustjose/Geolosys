package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketIOreUnderground implements IMessage {
    // A default constructor is always required
    public PacketIOreUnderground() {
    }

    String depositName;
    String direction;

    public PacketIOreUnderground(String depositName, EnumFacing direction) {
        this.depositName = depositName;
        this.direction = direction.getName();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.depositName);
        ByteBufUtils.writeUTF8String(buf, this.direction);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.depositName = ByteBufUtils.readUTF8String(buf);
        this.direction = ByteBufUtils.readUTF8String(buf);
    }

}