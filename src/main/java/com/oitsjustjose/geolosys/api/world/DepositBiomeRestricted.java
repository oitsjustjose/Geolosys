package com.oitsjustjose.geolosys.api.world;

import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.feature.PlutonType;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.List;
import java.util.Set;

public class DepositBiomeRestricted extends Deposit
{
    private List<Biome> biomes;
    private List<BiomeDictionary.Type> biomeTypes;
    private boolean useWhitelist;

    public DepositBiomeRestricted(BlockState oreBlock, BlockState sampleBlock, int yMin, int yMax, int size, int chance,
            String[] dimensionBlacklist, List<BlockState> blockStateMatchers, List<Biome> biomes,
            List<BiomeDictionary.Type> biomeTypes, boolean useWhitelist, PlutonType type, float density)
    {
        super(oreBlock, sampleBlock, yMin, yMax, size, chance, dimensionBlacklist, blockStateMatchers, type, density);
        this.biomes = biomes;
        this.biomeTypes = biomeTypes;
        this.useWhitelist = useWhitelist;
    }

    public boolean canPlaceInBiome(Biome biome)
    {
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
                if (type.getName().equalsIgnoreCase(otherType.getName()))
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

    public String getFriendlyName()
    {
        return Utils.blockStateToName(this.getOre());
    }
}