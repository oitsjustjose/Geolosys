package com.oitsjustjose.geolosys.common.world.capability;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class DepositCapability implements IDepositCapability {
    private ConcurrentLinkedQueue<ChunkPos> oreGenMap;
    private Map<BlockPos, BlockState> pendingBlocks;
    private Map<UUID, Boolean> giveMap;

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
        // Geolosys.getInstance().LOGGER.info("Putting pending block {} at {}", state,
        // pos);
        this.pendingBlocks.put(pos, state);
    }

    @Override
    public BlockState getPendingBlock(BlockPos pos) {
        BlockState ret = this.pendingBlocks.get(pos);
        return ret;
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
        this.getPendingBlocks()
                .forEach((pos, state) -> pendingBlocks.put(serializeBlockPos(pos), NBTUtil.writeBlockState(state)));
        this.getGivenMap().forEach((x, y) -> playersGifted.putBoolean(x.toString(), y));
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundNBT playersGifted = compound.getCompound("PlayersGifted");

        oreDeposits.keySet().forEach(key -> this.setPlutonGenerated(deSerializeChunkPos(key)));
        pendingBlocks.keySet().forEach(key -> this.putPendingBlock(deSerializeBlockPos(key),
                NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(pendingBlocks.get(key)))));
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
}
