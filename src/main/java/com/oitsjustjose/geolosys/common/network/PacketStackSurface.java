package com.oitsjustjose.geolosys.common.network;

import java.util.HashSet;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketStackSurface {

    public HashSet<BlockState> blocks;

    public PacketStackSurface(PacketBuffer buf) {
        CompoundNBT comp = buf.readCompoundTag();
        this.blocks = PacketHelpers.decodeBlocks(comp);
    }

    public PacketStackSurface(HashSet<BlockState> d1) {
        this.blocks = d1;
    }

    public static PacketStackSurface decode(PacketBuffer buf) {
        return new PacketStackSurface(buf);
    }

    public static void encode(PacketStackSurface msg, PacketBuffer buf) {
        buf.writeCompoundTag(PacketHelpers.encodeBlocks(msg.blocks));
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(PacketStackSurface msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                sendProspectingMessage(mc.player, PacketHelpers.messagify(msg.blocks));
            });
        }
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void sendProspectingMessage(PlayerEntity player, Object... messageDecorators) {
        TranslationTextComponent msg = new TranslationTextComponent("geolosys.pro_pick.tooltip.found_surface",
                messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}
