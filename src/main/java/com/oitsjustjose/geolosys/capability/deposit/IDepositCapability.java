package com.oitsjustjose.geolosys.capability.deposit;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.oitsjustjose.geolosys.capability.deposit.DepositCapability.PendingBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IDepositCapability {
    void putPendingBlock(BlockPos pos, BlockState state);

    void removePendingBlocksForChunk(ChunkPos p);

    int getPendingBlockCount();

    ConcurrentLinkedQueue<PendingBlock> getPendingBlocks(ChunkPos chunkPos);

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);
}
