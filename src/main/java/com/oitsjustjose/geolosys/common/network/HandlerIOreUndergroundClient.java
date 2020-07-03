package com.oitsjustjose.geolosys.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This MessageHandler does nothing; it is only used because the dedicated
 * server must register at least one message handler in order for Forge to know
 * what ID to use for this message. See more explanation in StartupCommon. User:
 * The Grey Ghost Date: 15/01/2015
 */
public class HandlerIOreUndergroundClient implements IMessageHandler<PacketIOreUnderground, IMessage> {
    @Override
    public IMessage onMessage(PacketIOreUnderground message, MessageContext ctx) {
        if (ctx.side == Side.CLIENT) {
            IThreadListener clientThread = Minecraft.getMinecraft();
            clientThread.addScheduledTask(() -> {
                String depositName = message.depositName;
                String direction = message.direction;
                sendProspectingMessage(Minecraft.getMinecraft().player, "geolosys.pro_pick.tooltip.found", depositName,
                        direction);
            });
        }

        return null;
    }

    private void sendProspectingMessage(EntityPlayer player, String messageBase, Object... messageDecorators) {
        TextComponentTranslation msg = new TextComponentTranslation(messageBase, messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}