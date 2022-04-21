package com.oitsjustjose.geolosys.capability.deposit;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IDepositCapability {
    void putPendingBlock(BlockPos pos, BlockState state);

    void removePendingBlock(BlockPos pos, BlockState state);

    Map<BlockPos, BlockState> getPendingBlocks(ChunkPos chunkPos);

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);
}
