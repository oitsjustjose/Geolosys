package com.oitsjustjose.geolosys.common.world.capability;

import com.oitsjustjose.geolosys.api.ChunkPosDim;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IPlutonCapability extends INBTSerializable<CompoundNBT>
{
    boolean hasOrePlutonGenerated(ChunkPosDim pos);

    void setOrePlutonGenerated(ChunkPosDim pos);

    boolean hasStonePlutonGenerated(ChunkPosDim pos);

    void setStonePlutonGenerated(ChunkPosDim pos);

    boolean hasRetroGenned(ChunkPosDim pos);

    void setRetroGenned(ChunkPosDim pos);

    void putPendingBlock(BlockPos pos, BlockState state);

    BlockState getPendingBlock(BlockPos pos);

    Map<ChunkPosDim, Boolean> getOreGenMap();

    Map<ChunkPosDim, Boolean> getStoneGenMap();

    Map<BlockPos, BlockState> getPendingBlocks();

    Map<ChunkPosDim, Boolean> getRetroMap();
}
