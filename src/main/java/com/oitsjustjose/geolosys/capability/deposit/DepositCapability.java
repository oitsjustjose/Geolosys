package com.oitsjustjose.geolosys.capability.deposit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class DepositCapability implements IDepositCapability {
    // Essentially indexed by ChunkPos for ease of use
    private final ConcurrentHashMap<ChunkPos, ConcurrentLinkedQueue<PendingBlock>> pendingBlocks;

    public static final Capability<IDepositCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public DepositCapability() {
        this.pendingBlocks = new ConcurrentHashMap<>();
    }

    public int getPendingBlockCount() {
        return (int) this.pendingBlocks.values().stream().collect(Collectors.summarizingInt(ConcurrentLinkedQueue::size)).getSum();
    }

    @Override
    public void putPendingBlock(BlockPos pos, BlockState state) {
        PendingBlock p = new PendingBlock(pos, state);
        ChunkPos cp = new ChunkPos(pos);
        this.pendingBlocks.putIfAbsent(cp, new ConcurrentLinkedQueue<>());
        this.pendingBlocks.get(cp).add(p);
    }

    @Override
    public void removePendingBlocksForChunk(ChunkPos cp) {
        this.pendingBlocks.remove(cp);
    }

    @Override
    public ConcurrentLinkedQueue<PendingBlock> getPendingBlocks(ChunkPos chunkPos) {
        return this.pendingBlocks.getOrDefault(chunkPos, new ConcurrentLinkedQueue<>());
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        this.pendingBlocks.forEach((pos, pending) -> {
            ListTag p = new ListTag();
            String key = pos.x + "_" + pos.z;
            pending.forEach(pb -> p.add(pb.serialize()));
            compound.put(key, p);
        });
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        compound.getAllKeys().forEach(chunkPosAsString -> {
            // Parse out the ChunkPos
            String[] parts = chunkPosAsString.split("_");
            ChunkPos cp = new ChunkPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
            // Parse out the pending block objects
            ListTag pending = compound.getList(chunkPosAsString, 10);
            ConcurrentLinkedQueue<PendingBlock> lq = new ConcurrentLinkedQueue<>();
            pending.forEach(x -> {
                PendingBlock pb = PendingBlock.deserialize(x);
                if (pb != null) {
                    lq.add(pb);
                }
            });
            this.pendingBlocks.put(cp, lq);
        });
    }

    public record PendingBlock(BlockPos pos, BlockState state) {

        public CompoundTag serialize() {
            CompoundTag tmp = new CompoundTag();
            CompoundTag posTag = NbtUtils.writeBlockPos(this.pos);
            CompoundTag stateTag = NbtUtils.writeBlockState(this.state);
            tmp.put("pos", posTag);
            tmp.put("state", stateTag);
            return tmp;
        }

        @Nullable
        public static PendingBlock deserialize(Tag t) {
            if (t instanceof CompoundTag tag) {
                BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("pos"));
                BlockState state = NbtUtils.readBlockState(tag.getCompound("state"));
                return new PendingBlock(pos, state);
            }
            return null;
        }

        @Override
        public String toString() {
            return "[" + this.pos.getX() + " " + this.pos.getY() + " " + this.pos.getZ() + "]: " + ForgeRegistries.BLOCKS.getKey(this.state.getBlock());
        }
    }
}
