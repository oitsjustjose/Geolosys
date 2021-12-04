package com.oitsjustjose.geolosys.common.world.capability;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface IDepositCapability extends INBTSerializable<CompoundNBT> {
    boolean hasPlutonGenerated(ChunkPos pos);

    void setPlutonGenerated(ChunkPos pos);

    void putPendingBlock(BlockPos pos, BlockState state);

    BlockState getPendingBlock(BlockPos pos);

    ConcurrentLinkedQueue<ChunkPos> getGenMap();

    Map<BlockPos, BlockState> getPendingBlocks();

    boolean hasPlayerReceivedManual(UUID uuid);

    void setPlayerReceivedManual(UUID uuid);

    Map<UUID, Boolean> getGivenMap();
}
