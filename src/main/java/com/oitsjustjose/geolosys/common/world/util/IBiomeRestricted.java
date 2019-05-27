package com.oitsjustjose.geolosys.common.world.util;

import java.util.List;

import net.minecraft.world.biome.Biome;

public interface IBiomeRestricted extends IOre
{
    public boolean canPlaceInBiome(Biome biome);

    public boolean useWhitelist();

    public boolean useBlacklist();

    public List<Biome> getBiomeList();
}