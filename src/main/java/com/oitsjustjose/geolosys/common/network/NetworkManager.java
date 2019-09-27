package com.oitsjustjose.geolosys.common.network;

import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetworkManager
{
        public SimpleChannel networkWrapper;

        public NetworkManager()
        {
                networkWrapper = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(Constants.MODID, "pro_pick_messages")).simpleChannel();
        }

        public void sendToClient(IMessage message, PlayerEntity player)
        {
                this.networkWrapper.sendTo(message, (ServerPlayerEntity) player);
        }
}