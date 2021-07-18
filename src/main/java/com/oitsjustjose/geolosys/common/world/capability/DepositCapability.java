package com.oitsjustjose.geolosys.common.world.capability;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.oitsjustjose.geolosys.api.BlockPosDim;
import com.oitsjustjose.geolosys.api.ChunkPosDim;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;

public class DepositCapability implements IDepositCapability {
    private ConcurrentLinkedQueue<ChunkPosDim> oreGenMap;
    private ConcurrentLinkedQueue<ChunkPosDim> stoneGenMap;
    private Map<BlockPosDim, BlockState> pendingBlocks;
    private Map<UUID, Boolean> giveMap;

    public DepositCapability() {
        this.oreGenMap = new ConcurrentLinkedQueue<>();
        this.stoneGenMap = new ConcurrentLinkedQueue<>();
        this.pendingBlocks = new ConcurrentHashMap<>();
        this.giveMap = new ConcurrentHashMap<>();
    }

    @Override
    public ConcurrentLinkedQueue<ChunkPosDim> getOreGenMap() {
        return this.oreGenMap;
    }

    @Override
    public ConcurrentLinkedQueue<ChunkPosDim> getStoneGenMap() {
        return this.stoneGenMap;
    }

    @Override
    public Map<BlockPosDim, BlockState> getPendingBlocks() {
        return this.pendingBlocks;
    }

    @Override
    public void putPendingBlock(BlockPosDim pos, BlockState state) {
        this.pendingBlocks.put(pos, state);
    }

    @Override
    public BlockState getPendingBlock(BlockPosDim pos) {
        BlockState ret = this.pendingBlocks.get(pos);
        return ret;
    }

    @Override
    public void setOrePlutonGenerated(ChunkPosDim dim) {
        this.oreGenMap.add(dim);
    }

    @Override
    public boolean hasOrePlutonGenerated(ChunkPosDim pos) {
        return this.oreGenMap.contains(pos);
    }

    @Override
    public void setStonePlutonGenerated(ChunkPosDim pos) {
        this.stoneGenMap.add(pos);
    }

    @Override
    public boolean hasStonePlutonGenerated(ChunkPosDim pos) {
        return this.stoneGenMap.contains(pos);
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
        compound.put("WorldStoneDeposits", new CompoundNBT());
        compound.put("PendingBlocks", new CompoundNBT());
        compound.put("PlayersGifted", new CompoundNBT());

        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT stoneDeposits = compound.getCompound("WorldStoneDeposits");
        CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundNBT playersGifted = compound.getCompound("PlayersGifted");

        this.getOreGenMap().forEach(x -> oreDeposits.putBoolean(x.toString(), true));
        this.getStoneGenMap().forEach(x -> stoneDeposits.putBoolean(x.toString(), true));
        this.getPendingBlocks().forEach((x, y) -> pendingBlocks.put(x.toString(), NBTUtil.writeBlockState(y)));
        this.getGivenMap().forEach((x, y) -> playersGifted.putBoolean(x.toString(), y));

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound) {
        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT stoneDeposits = compound.getCompound("WorldStoneDeposits");
        CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundNBT playersGifted = compound.getCompound("PlayersGifted");

        oreDeposits.keySet().forEach(key -> this.setOrePlutonGenerated(new ChunkPosDim(key)));
        stoneDeposits.keySet().forEach(key -> this.setStonePlutonGenerated(new ChunkPosDim(key)));
        pendingBlocks.keySet().forEach(key -> this.putPendingBlock(new BlockPosDim(key),
                NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(pendingBlocks.get(key)))));
        playersGifted.keySet().forEach(key -> this.setPlayerReceivedManual(UUID.fromString(key)));
    }
}
