package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
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

    public static class ProPickSurfaceHandler implements IMessageHandler<ProPickSurfacePacket, IMessage>
    {
        @Override
        public IMessage onMessage(ProPickSurfacePacket message, MessageContext ctx)
        {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

            String blockName = message.blockNameLocalized;

            sendProspectingMessage(serverPlayer, "geolosys.pro_pick.tooltip.found_surface", blockName);

            return null;
        }

        private void sendProspectingMessage(EntityPlayerMP player, String messageBase, Object... messageDecorators)
        {
            TextComponentTranslation msg = new TextComponentTranslation(messageBase, messageDecorators);
            player.sendStatusMessage(msg, true);
        }
    }
}
