package com.oitsjustjose.geolosys.common.world.capability;

import java.util.Map;
import java.util.UUID;

import com.oitsjustjose.geolosys.api.BlockPosDim;
import com.oitsjustjose.geolosys.api.ChunkPosDim;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IGeolosysCapability extends INBTSerializable<CompoundNBT>
{
    boolean hasOrePlutonGenerated(ChunkPosDim pos);

    void setOrePlutonGenerated(ChunkPosDim pos);

    boolean hasStonePlutonGenerated(ChunkPosDim pos);

    void setStonePlutonGenerated(ChunkPosDim pos);

    void putPendingBlock(BlockPosDim pos, BlockState state);

    BlockState getPendingBlock(BlockPosDim pos);

    Map<ChunkPosDim, Boolean> getOreGenMap();

    Map<ChunkPosDim, Boolean> getStoneGenMap();

    Map<BlockPosDim, BlockState> getPendingBlocks();

    boolean hasPlayerReceivedManual(UUID uuid);

    void setPlayerReceivedManual(UUID uuid);

    Map<UUID, Boolean> getGivenMap();
}
