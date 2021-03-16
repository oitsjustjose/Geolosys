package com.oitsjustjose.geolosys.client;

import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public void init() {
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class,
                PacketStackSurface::encode, PacketStackSurface::decode, PacketStackSurface::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++,
                PacketStackUnderground.class, PacketStackUnderground::encode, PacketStackUnderground::decode,
                PacketStackUnderground::handleClient);
    }

    @Override
    public void sendProspectingMessage(PlayerEntity player, ItemStack stack, Direction direction) {
        if (direction != null) {
            player.sendStatusMessage(
                    new TranslationTextComponent("geolosys.pro_pick.tooltip.found", stack.getDisplayName(), direction),
                    true);
        } else {
            player.sendStatusMessage(
                    new TranslationTextComponent("geolosys.pro_pick.tooltip.found_surface", stack.getDisplayName()),
                    true);
        }
    }

    @Override
    public void registerClientSubscribeEvent(Object o) {
        MinecraftForge.EVENT_BUS.register(o);
    }
}