package com.oitsjustjose.geolosys.client;

import com.oitsjustjose.geolosys.client.network.PacketStackClientSurface;
import com.oitsjustjose.geolosys.client.network.PacketStackClientUnderground;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.network.PacketHelpers;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.HashSet;

public class ClientProxy extends CommonProxy {
    public void init() {
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class,
                PacketStackSurface::encode, PacketStackSurface::decode, PacketStackClientSurface::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++,
                PacketStackUnderground.class, PacketStackUnderground::encode, PacketStackUnderground::decode,
                PacketStackClientUnderground::handleClient);
    }

    @Override
    public void sendProspectingMessage(Player player, HashSet<BlockState> blocks, @Nullable Direction direction) {
        if (direction != null) {
            player.displayClientMessage(new TranslatableComponent("geolosys.pro_pick.tooltip.found",
                    PacketHelpers.messagify(blocks), direction), true);
        } else {
            player.displayClientMessage(new TranslatableComponent("geolosys.pro_pick.tooltip.found_surface",
                    PacketHelpers.messagify(blocks)), true);
        }
    }

    @Override
    public void registerClientSubscribeEvent(Object o) {
        MinecraftForge.EVENT_BUS.register(o);
    }
}
