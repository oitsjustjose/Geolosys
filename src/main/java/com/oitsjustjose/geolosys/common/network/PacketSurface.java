package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSurface implements IMessage {
    public ItemStack stack;

    // A default constructor is always required
    public PacketSurface() {
    }

    public PacketSurface(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, this.stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.stack = ByteBufUtils.readItemStack(buf);
    }

}