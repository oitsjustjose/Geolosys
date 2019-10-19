package com.oitsjustjose.geolosys.api.world;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.oitsjustjose.geolosys.api.PlutonType;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DepositMultiOreBiomeRestricted extends DepositMultiOre
{
    private List<Biome> biomes;
    private List<BiomeDictionary.Type> biomeTypes;
    private boolean useWhitelist;

    public DepositMultiOreBiomeRestricted(HashMap<BlockState, Integer> oreBlocks,
            HashMap<BlockState, Integer> sampleBlocks, int yMin, int yMax, int size, int chance,
            String[] dimensionBlacklist, List<BlockState> blockStateMatchers, List<Biome> biomes,
            List<BiomeDictionary.Type> biomeTypes, boolean useWhitelist, PlutonType type, float density)
    {
        super(oreBlocks, sampleBlocks, yMin, yMax, size, chance, dimensionBlacklist, blockStateMatchers, type, density);
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
                if (type.getName().equalsIgnoreCase(otherType.getName()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getFriendlyName()
    {
        StringBuilder sb = new StringBuilder();

        for (BlockState state : this.oreBlocks.keySet())
        {
            String name = Utils.blockStateToName(state);
            // The name hasn't already been added
            if (sb.indexOf(name) == -1)
            {
                sb.append(" & ");
                sb.append(name);
            }
        }
        // Return substr(3) to ignore the first " & "
        return sb.toString().substring(3);
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