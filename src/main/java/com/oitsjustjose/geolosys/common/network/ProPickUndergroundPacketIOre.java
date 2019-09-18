package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProPickUndergroundPacketIOre implements IMessage
{
    // A default constructor is always required
    public ProPickUndergroundPacketIOre()
    {
    }

    String depositName;
    String direction;

    public ProPickUndergroundPacketIOre(String depositName, EnumFacing direction)
    {
        this.depositName = depositName;
        this.direction = direction.getName();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.depositName);
        ByteBufUtils.writeUTF8String(buf, this.direction);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.depositName = ByteBufUtils.readUTF8String(buf);
        this.direction = ByteBufUtils.readUTF8String(buf);
    }

    public static class ProPickUndergroundHandlerIOre implements IMessageHandler<ProPickUndergroundPacketIOre, IMessage>
    {
        @Override
        public IMessage onMessage(ProPickUndergroundPacketIOre message, MessageContext ctx)
        {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            String depositName = message.depositName;
            String direction = message.direction;
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                sendProspectingMessage(serverPlayer, "geolosys.pro_pick.tooltip.found", depositName, direction);
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