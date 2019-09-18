package com.oitsjustjose.geolosys.common.network;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager
{
    private static NetworkManager instance;
    private SimpleNetworkWrapper networkWrapper;

    private NetworkManager()
    {
        int nextID = 0;
        this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Geolosys.MODID);
        this.networkWrapper.registerMessage(ProPickUndergroundPacket.ProPickUndergroundHandler.class,
                ProPickUndergroundPacket.class, nextID++, Side.CLIENT);
        this.networkWrapper.registerMessage(ProPickSurfacePacket.ProPickSurfaceHandler.class,
                ProPickSurfacePacket.class, nextID++, Side.CLIENT);
    }

    public static void init()
    {
        instance = new NetworkManager();
    }

    public static NetworkManager getInstance()
    {
        if (instance == null)
        {
            init();
        }
        return instance;
    }

    public SimpleNetworkWrapper getNetworkWrapper()
    {
        return this.networkWrapper;
    }

    public void sendToClient(IMessage message, EntityPlayer player)
    {
        this.networkWrapper.sendTo(message, (EntityPlayerMP) player);
    }
}