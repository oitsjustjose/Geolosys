package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProPickSurfacePacket implements IMessage
{
    // A default constructor is always required
    public ProPickSurfacePacket()
    {
    }

    public ItemStack stack;

    public ProPickSurfacePacket(ItemStack stack)
    {
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeItemStack(buf, this.stack);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.stack = ByteBufUtils.readItemStack(buf);
    }

    public static class ProPickSurfaceHandler implements IMessageHandler<ProPickSurfacePacket, IMessage>
    {
        @Override
        public IMessage onMessage(ProPickSurfacePacket message, MessageContext ctx)
        {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            ItemStack stack = message.stack;
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                sendProspectingMessage(serverPlayer, "geolosys.pro_pick.tooltip.found_surface", stack.getDisplayName());
            });

            return null;
        }

        private void sendProspectingMessage(EntityPlayerMP player, String messageBase, Object... messageDecorators)
        {
            TextComponentTranslation msg = new TextComponentTranslation(messageBase, messageDecorators);
            player.sendStatusMessage(msg, true);
        }
    }
}