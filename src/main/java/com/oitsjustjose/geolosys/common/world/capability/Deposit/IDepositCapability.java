package com.oitsjustjose.geolosys.common.world.capability.Deposit;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IDepositCapability extends INBTSerializable<CompoundNBT> {
    boolean hasPlutonGenerated(ChunkPos pos);

    void setPlutonGenerated(ChunkPos pos);

    void putPendingBlock(BlockPos pos, BlockState state);

    void removePendingBlocksForChunk(ChunkPos cp);

    ConcurrentLinkedQueue<DepositCapability.PendingBlock> getPendingBlocks(ChunkPos cp);

    int getPendingBlockCount();

    ConcurrentLinkedQueue<ChunkPos> getGenMap();

    boolean hasPlayerReceivedManual(UUID uuid);

    void setPlayerReceivedManual(UUID uuid);

    Map<UUID, Boolean> getGivenMap();
}
