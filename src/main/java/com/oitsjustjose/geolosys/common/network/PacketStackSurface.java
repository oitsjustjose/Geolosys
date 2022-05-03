package com.oitsjustjose.geolosys.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.function.Supplier;

public class PacketStackSurface {

    public HashSet<BlockState> blocks;

    public PacketStackSurface(FriendlyByteBuf buf) {
        CompoundTag comp = buf.readNbt();
        this.blocks = PacketHelpers.decodeBlocks(comp);
    }

    public PacketStackSurface(HashSet<BlockState> d1) {
        this.blocks = d1;
    }

    public static PacketStackSurface decode(FriendlyByteBuf buf) {
        return new PacketStackSurface(buf);
    }

    public static void encode(PacketStackSurface msg, FriendlyByteBuf buf) {
        buf.writeNbt(PacketHelpers.encodeBlocks(msg.blocks));
    }

    public void handleServer(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }
}
