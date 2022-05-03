package com.oitsjustjose.geolosys.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
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
}
