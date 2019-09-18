package com.oitsjustjose.geolosys.network;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager
{
    private static NetworkManager instance;
    private SimpleNetworkWrapper networkWrapper;
    private int nextID = 0;

    public NetworkManager()
    {
        this.networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Geolosys.MODID);
        this.networkWrapper.registerMessage(ProPickSurfaceHandler.class, ProPickSurfacePacket.class, nextID++,
                Side.SERVER);
    }

    public static NetworkManager getInstance()
    {
        if (instance == null)
        {
            instance = new NetworkManager();
        }
        return instance;
    }

    public SimpleNetworkWrapper getNetworkWrapper()
    {
        return this.networkWrapper;
    }

    public void sendToServer(IMessage message)
    {
        this.networkWrapper.sendToServer(message);
    }
}