package com.oitsjustjose.geolosys.capability.deposit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

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

public class DepositCapability implements IDepositCapability {
    // Essentially indexed by ChunkPos for ease of use
    private ConcurrentHashMap<ChunkPos, ConcurrentLinkedQueue<PendingBlock>> pendingBlocks;

    public static final Capability<IDepositCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public DepositCapability() {
        this.pendingBlocks = new ConcurrentHashMap<>();
    }

    public int getPendingBlockCount() {
        return (int) this.pendingBlocks.values().stream().collect(Collectors.summarizingInt(x -> x.size())).getSum();
        // return this.pendingBlocks.size();
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
        this.pendingBlocks.entrySet().forEach(e -> {
            ListTag p = new ListTag();
            String key = e.getKey().x + "_" + e.getKey().z;
            e.getValue().forEach(pb -> p.add(pb.serialize()));
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

    public static class PendingBlock {
        private BlockPos pos;
        private BlockState state;

        public PendingBlock(BlockPos pos, BlockState state) {
            this.pos = pos;
            this.state = state;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public BlockState getState() {
            return this.state;
        }

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
            if (t instanceof CompoundTag) {
                CompoundTag tag = (CompoundTag) t;
                BlockPos pos = NbtUtils.readBlockPos(tag.getCompound("pos"));
                BlockState state = NbtUtils.readBlockState(tag.getCompound("state"));
                return new PendingBlock(pos, state);
            }
            return null;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.pos.getX());
            sb.append(" ");
            sb.append(this.pos.getY());
            sb.append(" ");
            sb.append(this.pos.getZ());
            sb.append("]: ");
            sb.append(this.state.getBlock().getRegistryName().toString());
            return sb.toString();
        }
    }
}
