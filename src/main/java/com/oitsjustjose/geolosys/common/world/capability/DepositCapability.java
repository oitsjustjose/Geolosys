package com.oitsjustjose.geolosys.common.world.capability;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class DepositCapability implements IDepositCapability {
    private Map<BlockPos, BlockState> pendingBlocks;

    public static final Capability<IDepositCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public DepositCapability() {
        this.pendingBlocks = new ConcurrentHashMap<>();
    }

    @Override
    public void putPendingBlock(BlockPos pos, BlockState state) {
        this.pendingBlocks.put(pos, state);
    }

    @Override
    public void removePendingBlock(BlockPos pos, BlockState state) {
        this.pendingBlocks.remove(pos, state);
    }

    @Override
    public Map<BlockPos, BlockState> getPendingBlocks(ChunkPos chunkPos) {
        Map<BlockPos, BlockState> ret = new ConcurrentHashMap<>();
        this.pendingBlocks.forEach((pos, state) -> {
            ChunkPos tmp = new ChunkPos(pos);
            if (chunkPos == tmp) {
                ret.put(pos, state);
            }
        });
        return ret;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag pendingBlocks = new ListTag();
        this.pendingBlocks.forEach((pos, state) -> {
            CompoundTag tmp = new CompoundTag();
            CompoundTag posTag = NbtUtils.writeBlockPos(pos);
            CompoundTag stateTag = NbtUtils.writeBlockState(state);
            tmp.put("pos", posTag);
            tmp.put("state", stateTag);
            pendingBlocks.add(tmp);
        });

        compound.put("pending", pendingBlocks);
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        ListTag pending = compound.getList("pending", 10);
        pending.forEach((tmp) -> {
            CompoundTag comp = (CompoundTag) tmp;
            BlockPos pos = NbtUtils.readBlockPos(comp.getCompound("pos"));
            BlockState state = NbtUtils.readBlockState(comp.getCompound("state"));
            this.putPendingBlock(pos, state);
        });
    }
}
