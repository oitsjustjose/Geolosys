package com.oitsjustjose.geolosys.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ProPickUndergroundPacket implements IMessage
{
    // A default constructor is always required
    public ProPickUndergroundPacket()
    {
    }

    public String blockNameLocalized;
    public String direction;

    public ProPickUndergroundPacket(String blockNameLocalized, EnumFacing direction)
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

    public static class ProPickUndergroundHandler implements IMessageHandler<ProPickUndergroundPacket, IMessage>
    {
        @Override
        public IMessage onMessage(ProPickUndergroundPacket message, MessageContext ctx)
        {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

            String blockName = message.blockNameLocalized;
            String direction = message.direction;

            sendProspectingMessage(serverPlayer, "geolosys.pro_pick.tooltip.found", blockName, direction);

            return null;
        }

        private void sendProspectingMessage(EntityPlayerMP player, String messageBase, Object... messageDecorators)
        {
            TextComponentTranslation msg = new TextComponentTranslation(messageBase, messageDecorators);
            player.sendStatusMessage(msg, true);
        }
    }
}
