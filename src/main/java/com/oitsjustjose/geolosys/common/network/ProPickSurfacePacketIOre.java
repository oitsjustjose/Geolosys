package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProPickSurfacePacketIOre implements IMessage
{
    // A default constructor is always required
    public ProPickSurfacePacketIOre()
    {
    }

    public String depositName;

    public ProPickSurfacePacketIOre(String depositName)
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

    public static class ProPickSurfaceHandlerIOre implements IMessageHandler<ProPickSurfacePacketIOre, IMessage>
    {
        @Override
        public IMessage onMessage(ProPickSurfacePacketIOre message, MessageContext ctx)
        {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            String depositName = message.depositName;
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                sendProspectingMessage(serverPlayer, "geolosys.pro_pick.tooltip.found_surface", depositName);
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