package com.oitsjustjose.geolosys.common.world.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IDepositCapability {
    boolean hasPlutonGenerated(ChunkPos pos);

    void setPlutonGenerated(ChunkPos pos);

    void putPendingBlock(BlockPos pos, BlockState state);

    BlockState getPendingBlock(BlockPos pos);

    ConcurrentLinkedQueue<ChunkPos> getGenMap();

    Map<BlockPos, BlockState> getPendingBlocks();

    boolean hasPlayerReceivedManual(UUID uuid);

    void setPlayerReceivedManual(UUID uuid);

    Map<UUID, Boolean> getGivenMap();

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);
}
