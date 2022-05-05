package com.oitsjustjose.geolosys.common;

import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.HashSet;

public class CommonProxy {
    public static NetworkManager networkManager = new NetworkManager();
    public static int discriminator = 0;

    public void init() {
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class,
                PacketStackSurface::encode, PacketStackSurface::decode, PacketStackSurface::handleServer);
        networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackUnderground.class,
                PacketStackUnderground::encode, PacketStackUnderground::decode, PacketStackUnderground::handleServer);
    }

    public void sendProspectingMessage(Player player, HashSet<BlockState> blocks, @Nullable Direction direction) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        if (direction != null) {
            PacketStackUnderground msg = new PacketStackUnderground(blocks, direction.getName());
            networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
        } else {
            PacketStackSurface msg = new PacketStackSurface(blocks);
            networkManager.networkWrapper.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), msg);
        }
    }

    public void registerClientSubscribeEvent(Object o) {
    }
}
