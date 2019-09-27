package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketUnderground implements IMessage
{
    // A default constructor is always required
    public PacketUnderground()
    {
    }

    ItemStack stack;
    String direction;

    public PacketUnderground(ItemStack stack, EnumFacing direction)
    {
        this.stack = stack;
        this.direction = direction.getName();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, this.stack);
        ByteBufUtils.writeUTF8String(buf, this.direction);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.stack = ByteBufUtils.readItemStack(buf);
        this.direction = ByteBufUtils.readUTF8String(buf);
    }

}