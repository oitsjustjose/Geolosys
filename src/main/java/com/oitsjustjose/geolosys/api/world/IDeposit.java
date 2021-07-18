package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;

public interface IDeposit {
    public int generate(ISeedReader reader, BlockPos pos, IGeolosysCapability cap);

    public void afterGen(ISeedReader reader, BlockPos pos);

    public int getGenWt();

    public boolean canPlaceInBiome(Biome biome);

    /*
     * 
     * TODO: implement maybe ??
     * 
     * public boolean canPlaceInDimension(String dimkey);
     * 
     * 
     */
}