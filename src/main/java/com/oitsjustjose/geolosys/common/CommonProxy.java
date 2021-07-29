package com.oitsjustjose.geolosys.common;

import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.PacketDistributor;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();
    public static int discriminator = 0;

    public void init() {
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++,
                PacketStackSurface.class, PacketStackSurface::encode, PacketStackSurface::decode,
                PacketStackSurface::handleServer);
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++,
                PacketStackUnderground.class, PacketStackUnderground::encode,
                PacketStackUnderground::decode, PacketStackUnderground::handleServer);
    }

    public void sendProspectingMessage(PlayerEntity player, ItemStack stack, Direction direction) {
        if (!(player instanceof ServerPlayerEntity)) {
            return;
        }
        if (direction != null) {
            PacketStackUnderground msg = new PacketStackUnderground(stack, direction.getName2());
            networkManager.networkWrapper
                    .send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
        } else {
            PacketStackSurface msg = new PacketStackSurface(stack);
            networkManager.networkWrapper
                    .send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), msg);
        }
    }

    public void registerClientSubscribeEvent(Object o) {}
}
