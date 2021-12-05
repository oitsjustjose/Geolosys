package com.oitsjustjose.geolosys.common.world.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IDepositCapability extends INBTSerializable<CompoundTag> {
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
