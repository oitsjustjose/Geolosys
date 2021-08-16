package com.oitsjustjose.geolosys.common.network;

import java.util.HashSet;
import java.util.function.Supplier;

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

public class PacketStackUnderground {
    public HashSet<BlockState> blocks;
    public String direction;

    public PacketStackUnderground(PacketBuffer buf) {
        CompoundNBT comp = buf.readCompoundTag();
        this.blocks = PacketHelpers.decodeBlocks(comp);
        this.direction = buf.readString();
    }

    public PacketStackUnderground(HashSet<BlockState> d1, String d2) {
        this.blocks = d1;
        this.direction = d2;
    }

    public static PacketStackUnderground decode(PacketBuffer buf) {
        return new PacketStackUnderground(buf);
    }

    public static void encode(PacketStackUnderground msg, PacketBuffer buf) {
        buf.writeCompoundTag(PacketHelpers.encodeBlocks(msg.blocks));
        buf.writeString(msg.direction);
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(PacketStackUnderground msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                sendProspectingMessage(mc.player, PacketHelpers.messagify(msg.blocks), msg.direction);
            });
        }
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void sendProspectingMessage(PlayerEntity player, Object... messageDecorators) {
        TranslationTextComponent msg = new TranslationTextComponent("geolosys.pro_pick.tooltip.found",
                messageDecorators);
        player.sendStatusMessage(msg, true);
    }
}
