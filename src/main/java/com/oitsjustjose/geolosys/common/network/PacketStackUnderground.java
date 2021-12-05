package com.oitsjustjose.geolosys.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.function.Supplier;

public class PacketStackUnderground {
    public HashSet<BlockState> blocks;
    public String direction;

    public PacketStackUnderground(FriendlyByteBuf buf) {
        CompoundTag comp = buf.readNbt();
        this.blocks = PacketHelpers.decodeBlocks(comp);
        this.direction = buf.readUtf();
    }

    public PacketStackUnderground(HashSet<BlockState> d1, String d2) {
        this.blocks = d1;
        this.direction = d2;
    }

    public static PacketStackUnderground decode(FriendlyByteBuf buf) {
        return new PacketStackUnderground(buf);
    }

    public static void encode(PacketStackUnderground msg, FriendlyByteBuf buf) {
        buf.writeNbt(PacketHelpers.encodeBlocks(msg.blocks));
        buf.writeUtf(msg.direction);
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
    private static void sendProspectingMessage(LocalPlayer player, Object... messageDecorators) {
        TranslatableComponent msg = new TranslatableComponent("geolosys.pro_pick.tooltip.found",
                messageDecorators);
        player.displayClientMessage(msg, true);
    }
}
