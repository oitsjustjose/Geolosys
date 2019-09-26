package com.oitsjustjose.geolosys.common.world.capability;

import com.oitsjustjose.geolosys.common.world.utils.ChunkPosDim;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlutonCapability implements IPlutonCapability
{
    private HashMap<ChunkPosDim, Boolean> oreGenMap;
    private HashMap<ChunkPosDim, Boolean> stoneGenMap;
    private HashMap<BlockPos, BlockState> pendingBlocks;
    private HashMap<ChunkPosDim, Boolean> retroMap;

    public PlutonCapability()
    {
        this.oreGenMap = new HashMap<>();
        this.stoneGenMap = new HashMap<>();
        this.pendingBlocks = new HashMap<>();
        this.retroMap = new HashMap<>();
    }

    @Override
    public Map<ChunkPosDim, Boolean> getOreGenMap()
    {
        return this.oreGenMap;
    }

    @Override
    public Map<ChunkPosDim, Boolean> getStoneGenMap()
    {
        return this.stoneGenMap;
    }

    @Override
    public Map<BlockPos, BlockState> getPendingBlocks()
    {
        return this.pendingBlocks;
    }

    @Override
    public Map<ChunkPosDim, Boolean> getRetroMap()
    {
        return this.retroMap;
    }

    @Override
    public void setRetroGenned(ChunkPosDim pos)
    {
        this.retroMap.put(pos, true);
    }

    @Override
    public void putPendingBlock(BlockPos pos, BlockState state)
    {
        this.pendingBlocks.put(pos, state);
    }

    @Override
    public BlockState getPendingBlock(BlockPos pos)
    {
        BlockState ret = this.pendingBlocks.get(pos);
        this.pendingBlocks.remove(pos);
        return ret;
    }

    @Override
    public boolean hasRetroGenned(ChunkPosDim pos)
    {
        return this.retroMap.containsKey(pos) && this.retroMap.get(pos);
    }

    @Override
    public void setOrePlutonGenerated(ChunkPosDim dim)
    {
        this.oreGenMap.put(dim, true);
    }

    @Override
    public boolean hasOrePlutonGenerated(ChunkPosDim pos)
    {
        return this.oreGenMap.containsKey(pos) && this.oreGenMap.get(pos);
    }

    @Override
    public void setStonePlutonGenerated(ChunkPosDim dim)
    {
        this.stoneGenMap.put(dim, true);
    }

    @Override
    public boolean hasStonePlutonGenerated(ChunkPosDim pos)
    {
        return this.stoneGenMap.containsKey(pos) && this.stoneGenMap.get(pos);
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT compound = new CompoundNBT();
        compound.put("WorldOreDeposits", new CompoundNBT());
        compound.put("WorldStoneDeposits", new CompoundNBT());
        compound.put("PendingBlocks", new CompoundNBT());
        compound.put("RetroGen", new CompoundNBT());

        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT stoneDeposits = compound.getCompound("WorldStoneDeposits");
        CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundNBT retroGen = compound.getCompound("RetroGen");

        this.getOreGenMap().forEach((x, y) -> oreDeposits.putBoolean(x.toString(), y));
        this.getStoneGenMap().forEach((x, y) -> stoneDeposits.putBoolean(x.toString(), y));
        this.getPendingBlocks().forEach((x, y) -> pendingBlocks.put(CapUtils.toString(x), NBTUtil.writeBlockState(y)));
        this.getRetroMap().forEach((x, y) -> retroGen.putBoolean(x.toString(), y));
        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT compound)
    {
        CompoundNBT oreDeposits = compound.getCompound("WorldOreDeposits");
        CompoundNBT stoneDeposits = compound.getCompound("WorldStoneDeposits");
        CompoundNBT pendingBlocks = compound.getCompound("PendingBlocks");
        CompoundNBT retroGen = compound.getCompound("RetroGen");

        oreDeposits.keySet().forEach(key -> this.setOrePlutonGenerated(new ChunkPosDim(key)));
        stoneDeposits.keySet().forEach(key -> this.setStonePlutonGenerated(new ChunkPosDim(key)));
        pendingBlocks.keySet().forEach(key -> this.putPendingBlock(CapUtils.fromString(key),
                NBTUtil.readBlockState((CompoundNBT) Objects.requireNonNull(pendingBlocks.get(key)))));
        retroGen.keySet().forEach(key -> this.setRetroGenned(new ChunkPosDim(key)));
    }
}
