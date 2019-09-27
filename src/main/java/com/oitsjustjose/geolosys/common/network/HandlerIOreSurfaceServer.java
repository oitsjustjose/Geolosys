package com.oitsjustjose.geolosys.common.network;

import javax.xml.ws.handler.MessageContext;

/**
 * This MessageHandler does nothing; it is only used because the dedicated server must register at least one message handler in
 * order for Forge to know what ID to use for this message. See more explanation in StartupCommon. User: The Grey Ghost Date:
 * 15/01/2015
 */
public class HandlerIOreSurfaceServer implements MessageHand<PacketIOreSurface, IMessage>
{
    public IMessage onMessage(final PacketIOreSurface message, MessageContext ctx)
    {
        System.err.println("TargetEffectMessageToClient received on wrong side:" + ctx.side);
        return null;
    }
}