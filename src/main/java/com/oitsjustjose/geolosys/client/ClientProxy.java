package com.oitsjustjose.geolosys.client;

import java.util.HashSet;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.client.render.BlockHighlighter;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.config.ClientConfig;
import com.oitsjustjose.geolosys.common.network.PacketHelpers;
import com.oitsjustjose.geolosys.common.network.PacketHighlightPos;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    private BlockHighlighter bh = new BlockHighlighter();

    public void init() {
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class,
                PacketStackSurface::encode, PacketStackSurface::decode, PacketStackSurface::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++,
                PacketStackUnderground.class, PacketStackUnderground::encode, PacketStackUnderground::decode,
                PacketStackUnderground::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketHighlightPos.class,
                PacketHighlightPos::encode, PacketHighlightPos::decode, PacketHighlightPos::handleClient);
        registerClientSubscribeEvent(bh);
    }

    @Override
    public void sendProspectingMessage(PlayerEntity player, HashSet<BlockState> blocks, @Nullable Direction direction) {
        if (direction != null) {
            player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.found",
                    PacketHelpers.messagify(blocks), direction), true);
        } else {
            player.sendStatusMessage(new TranslationTextComponent("geolosys.pro_pick.tooltip.found_surface",
                    PacketHelpers.messagify(blocks)), true);
        }
    }

    @Override
    public void highlightBlocks(HashSet<BlockPos> posns, PlayerEntity player) {
        if (ClientConfig.ENABLE_DEPOSIT_HIGHLIGHT.get()) {
            posns.forEach((p) -> bh.selectBlock(p,
                    System.currentTimeMillis() + (1000 * ClientConfig.DEPOSIT_HIGHLIGHT_DURATION.get())));
        }
    }

    @Override
    public void registerClientSubscribeEvent(Object o) {
        MinecraftForge.EVENT_BUS.register(o);
    }
}
