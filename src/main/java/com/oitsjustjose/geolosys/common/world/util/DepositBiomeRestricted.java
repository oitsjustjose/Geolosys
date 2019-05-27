package com.oitsjustjose.geolosys.common.world.util;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.Biome;

public class DepositBiomeRestricted extends Deposit implements IBiomeRestricted
{
    private IBlockState oreBlock;
    private IBlockState sampleBlock;
    private List<Biome> biomes;
    private boolean useWhitelist;

    public DepositBiomeRestricted(IBlockState oreBlock, IBlockState sampleBlock, List<Biome> biomes,
            boolean useWhitelist)
    {
        this.oreBlock = oreBlock;
        this.sampleBlock = sampleBlock;
        this.biomes = biomes;
        this.useWhitelist = useWhitelist;
    }

    public boolean canPlaceInBiome(Biome biome)
    {
        // Manually check this since it's always a fucking pain
        for (Biome b : this.biomes)
        {
            if (b == biome)
            {
                return true;
            }
        }
        return false;
    }

    public boolean useWhitelist()
    {
        return this.useWhitelist;
    }

    public boolean useBlacklist()
    {
        return !this.useWhitelist;
    }

    public List<Biome> getBiomeList()
    {
        return this.biomes;
    }

}