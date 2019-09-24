package com.oitsjustjose.geolosys.api.world;

import java.util.List;
import java.util.Set;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DepositBiomeRestricted extends Deposit
{
    private List<Biome> biomes;
    private List<BiomeDictionary.Type> biomeTypes;
    private boolean useWhitelist;

    public DepositBiomeRestricted(BlockState oreBlock, BlockState sampleBlock, int yMin, int yMax, int size, int chance,
            int[] dimensionBlacklist, List<BlockState> blockStateMatchers, List<Biome> biomes,
            List<BiomeDictionary.Type> biomeTypes, boolean useWhitelist, float density)
    {
        super(oreBlock, sampleBlock, yMin, yMax, size, chance, dimensionBlacklist, blockStateMatchers, density);
        this.biomes = biomes;
        this.biomeTypes = biomeTypes;
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
        for (BiomeDictionary.Type type : this.biomeTypes)
        {
            Set<BiomeDictionary.Type> dictTypes = BiomeDictionary.getTypes(biome);
            for (BiomeDictionary.Type otherType : dictTypes)
            {
                if (type.equals(otherType))
                {
                    return true;
                }
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

    public List<BiomeDictionary.Type> getBiomeTypes()
    {
        return this.biomeTypes;
    }
}