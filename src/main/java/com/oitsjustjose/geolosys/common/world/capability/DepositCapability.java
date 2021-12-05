package com.oitsjustjose.geolosys.common.world.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DepositCapability implements IDepositCapability {
    private ConcurrentLinkedQueue<ChunkPos> oreGenMap;
    private Map<BlockPos, BlockState> pendingBlocks;
    private Map<UUID, Boolean> giveMap;

    public static final Capability<IDepositCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

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
    public Map<BlockPos, BlockState> getPendingBlocks() {
        return this.pendingBlocks;
    }

    @Override
    public void putPendingBlock(BlockPos pos, BlockState state) {
        this.pendingBlocks.put(pos, state);
    }

    @Override
    public BlockState getPendingBlock(BlockPos pos) {
        return this.pendingBlocks.get(pos);
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
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        compound.put("WorldOreDeposits", new CompoundTag());
        compound.put("PendingBlocks", new CompoundTag());
        compound.put("PlayersGifted", new CompoundTag());

        CompoundTag oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundTag pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundTag playersGifted = compound.getCompound("PlayersGifted");

        this.getGenMap().forEach(cp -> oreDeposits.putBoolean(serializeChunkPos(cp), true));
        this.getPendingBlocks()
                .forEach((pos, state) -> pendingBlocks.put(serializeBlockPos(pos), NbtUtils.writeBlockState(state)));
        this.getGivenMap().forEach((x, y) -> playersGifted.putBoolean(x.toString(), y));
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        CompoundTag oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundTag pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundTag playersGifted = compound.getCompound("PlayersGifted");

        oreDeposits.getAllKeys().forEach(key -> this.setPlutonGenerated(deSerializeChunkPos(key)));
        pendingBlocks.getAllKeys().forEach(key -> this.putPendingBlock(deSerializeBlockPos(key),
                NbtUtils.readBlockState((CompoundTag) Objects.requireNonNull(pendingBlocks.get(key)))));
        playersGifted.getAllKeys().forEach(key -> this.setPlayerReceivedManual(UUID.fromString(key)));
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
}
