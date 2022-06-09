package com.oitsjustjose.geolosys.common.world.capability.Deposit;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import javax.annotation.Nullable;

public class DepositCapability implements IDepositCapability {
    private final ConcurrentLinkedQueue<ChunkPos> oreGenMap;
    private final ConcurrentHashMap<ChunkPos, ConcurrentLinkedQueue<PendingBlock>> pendingBlocks;
    private final ConcurrentHashMap<UUID, Boolean> giveMap;

    public DepositCapability() {
        this.oreGenMap = new ConcurrentLinkedQueue<>();
        this.pendingBlocks = new ConcurrentHashMap<>();
        this.giveMap = new ConcurrentHashMap<>();
    }


    @Override
    public ConcurrentLinkedQueue<ChunkPos> getGenMap() {
        return this.oreGenMap;
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
    public int getPendingBlockCount() {
        return (int) this.pendingBlocks.values().stream().collect(Collectors.summarizingInt(x -> x.size())).getSum();
    }

    @Override
    public void setPlutonGenerated(ChunkPos pos) {
        this.oreGenMap.add(pos);
    }

    @Override
    public boolean hasPlutonGenerated(ChunkPos pos) {
        return this.oreGenMap.contains(pos);
    }

    @Override
    public boolean hasPlayerReceivedManual(UUID uuid) {
        return this.giveMap.containsKey(uuid);
    }

    @Override
    public void setPlayerReceivedManual(UUID uuid) {
        this.giveMap.put(uuid, true);
    }

    @Override
    public Map<UUID, Boolean> getGivenMap() {
        return this.giveMap;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.put("WorldOreDeposits", new CompoundNBT());
        compound.put("PendingBlocks", new CompoundNBT());
        compound.put("PlayersGifted", new CompoundNBT());

        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundNBT playersGifted = compound.getCompound("PlayersGifted");

        this.getGenMap().forEach(cp -> oreDeposits.putBoolean(serializeChunkPos(cp), true));
        this.getGivenMap().forEach((x, y) -> playersGifted.putBoolean(x.toString(), y));
        this.pendingBlocks.entrySet().forEach(e -> {
           ListNBT p = new ListNBT();
           String key = e.getKey().x + "_" + e.getKey().z;
           e.getValue().forEach(pb -> p.add(pb.serialize()));
           pendingBlocks.put(key, p);
        });

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        // Check to see if the old storage is still there
        if (compound.contains("PendingBlocks")) {
            CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
            pendingBlocks.keySet().forEach(key -> {
                BlockPos pos = deSerializeBlockPos(key);
                BlockState state = NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(pendingBlocks.get(key)));
                this.putPendingBlock(pos, state);
            });
        }

        if (compound.contains("PendingBlocksNew")) {
            CompoundNBT pendingBlocks = compound.getCompound("PendingBlocksNew");
            compound.keySet().forEach(chunkPosAsString -> {
                String[] parts = chunkPosAsString.split("_");
                ChunkPos cp = new ChunkPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));

                ListNBT pending = compound.getList(chunkPosAsString, 10);
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

        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT playersGifted = compound.getCompound("PlayersGifted");
        oreDeposits.keySet().forEach(key -> this.setPlutonGenerated(deSerializeChunkPos(key)));
        playersGifted.keySet().forEach(key -> this.setPlayerReceivedManual(UUID.fromString(key)));
    }

    private String serializeChunkPos(ChunkPos pos) {
        return pos.x + "," + pos.z;
    }

    private String serializeBlockPos(BlockPos pos) {
        return pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    private ChunkPos deSerializeChunkPos(String asStr) {
        String[] parts = asStr.split(",");
        return new ChunkPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private BlockPos deSerializeBlockPos(String asStr) {
        String[] parts = asStr.split(",");
        return new BlockPos(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
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

        public CompoundNBT serialize() {
            CompoundNBT tag = new CompoundNBT();
            CompoundNBT posTag = NBTUtil.writeBlockPos(this.pos);
            CompoundNBT stateTag = NBTUtil.writeBlockState(this.state);
            tag.put("pos", posTag);
            tag.put("state", stateTag);
            return tag;
        }

        @Nullable
        public static PendingBlock deserialize(INBT nbt) {
            if (nbt instanceof CompoundNBT) {
                CompoundNBT tag = (CompoundNBT) nbt;
                BlockPos pos = NBTUtil.readBlockPos(tag.getCompound("pos"));
                BlockState state = NBTUtil.readBlockState(tag.getCompound("state"));
                return new PendingBlock(pos, state);
            }

            return null;
        }
    }
}
