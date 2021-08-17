package com.oitsjustjose.geolosys.common.network;

import java.util.HashSet;
import java.util.function.Supplier;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketHighlightPos {
    public HashSet<BlockPos> posns;

    public PacketHighlightPos(PacketBuffer buf) {
        CompoundNBT comp = buf.readCompoundTag();
        this.posns = PacketHelpers.decodeBlockPosns(comp);
    }

    public PacketHighlightPos(HashSet<BlockPos> d1) {
        this.posns = d1;
    }

    public static PacketHighlightPos decode(PacketBuffer buf) {
        return new PacketHighlightPos(buf);
    }

    public static void encode(PacketHighlightPos msg, PacketBuffer buf) {
        buf.writeCompoundTag(PacketHelpers.encodeBlockPosns(msg.posns));
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleClient(PacketHighlightPos msg, Supplier<NetworkEvent.Context> context) {
        if (context.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                Geolosys.proxy.highlightBlocks(msg.posns, mc.player);
            });
        }
        context.get().setPacketHandled(true);
    }
}
