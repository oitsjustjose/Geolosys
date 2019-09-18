package com.oitsjustjose.geolosys.common.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// The params of the IMessageHandler are <REQ, REPLY>
// This means that the first param is the packet you are receiving, and the second is the packet you are returning.
// The returned packet can be used as a "response" from a sent packet.
public class ProPickSurfaceHandler implements IMessageHandler<ProPickSurfacePacket, IMessage>
{
    @Override
    public IMessage onMessage(ProPickSurfacePacket message, MessageContext ctx)
    {
        EntityPlayerMP serverPlayer = ctx.getServerHandler().player;

        String blockName = message.blockNameLocalized;

        sendProspectingMessage(serverPlayer, "geolosys.pro_pick.tooltip.found_surface", blockName);

        return null;
    }

    private void sendProspectingMessage(EntityPlayer player, String messageBase, Object... messageDecorators)
    {
        TextComponentTranslation msg = new TextComponentTranslation(messageBase, messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}