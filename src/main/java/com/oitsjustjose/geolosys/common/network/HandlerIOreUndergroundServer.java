package com.oitsjustjose.geolosys.common.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This MessageHandler does nothing; it is only used because the dedicated
 * server must register at least one message handler in order for Forge to know
 * what ID to use for this message. See more explanation in StartupCommon. User:
 * The Grey Ghost Date: 15/01/2015
 */
public class HandlerIOreUndergroundServer implements IMessageHandler<PacketIOreUnderground, IMessage> {
    public IMessage onMessage(final PacketIOreUnderground message, MessageContext ctx) {
        System.err.println("TargetEffectMessageToClient received on wrong side:" + ctx.side);
        return null;
    }
}