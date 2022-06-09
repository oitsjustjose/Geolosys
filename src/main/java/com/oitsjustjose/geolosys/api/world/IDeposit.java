package com.oitsjustjose.geolosys.api.world;

import java.util.HashSet;

import com.oitsjustjose.geolosys.common.world.capability.Chunk.IChunkGennedCapability;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.IDepositCapability;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;

public interface IDeposit {
    public int generate(ISeedReader reader, BlockPos pos, IDepositCapability cap, IChunkGennedCapability cgCap);

    public void afterGen(ISeedReader reader, BlockPos pos, IDepositCapability cap, IChunkGennedCapability cgCap);

    public HashSet<BlockState> getAllOres();

    public int getGenWt();

    public boolean canPlaceInBiome(Biome biome);

    public boolean hasBiomeRestrictions();

    public String[] getDimensionFilter();

    public boolean isDimensionFilterBl();

    public HashSet<BlockState> getBlockStateMatchers();
}
