package com.oitsjustjose.geolosys.common.network;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class NetworkManager {
    public SimpleNetworkWrapper networkWrapper;

    public NetworkManager() {
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Geolosys.MODID);
    }

    public void sendToClient(IMessage message, EntityPlayer player) {
        this.networkWrapper.sendTo(message, (EntityPlayerMP) player);
    }
}