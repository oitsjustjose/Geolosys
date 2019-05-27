package com.oitsjustjose.geolosys.common.world.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class DepositMultiOre implements IOre
{
    private ArrayList<IBlockState> ores;
    private ArrayList<IBlockState> samples;
    private int yMin;
    private int yMax;
    private int size;
    private int chance;
    private int[] dimensionBlacklist;
    private List<IBlockState> blockStateMatchers;

    public DepositMultiOre(HashMap<IBlockState, Integer> oreBlocks, HashMap<IBlockState, Integer> sampleBlocks,
            int yMin, int yMax, int size, int chance, int[] dimensionBlacklist, List<IBlockState> blockStateMatchers)
    {
        // Sanity checking:
        int sum = 0;
        for (IBlockState key : oreBlocks.keySet())
        {
            sum += oreBlocks.get(key);
        }
        assert sum == 100 : "Sums of chances should equal 100";
        sum = 0;
        for (IBlockState key : sampleBlocks.keySet())
        {
            sum += sampleBlocks.get(key);
        }
        assert sum == 100 : "Sums of chances should equal 100";

        // Convert to my cool range stylization:
        for (IBlockState key : oreBlocks.keySet())
        {
            int prog = 0;
            while (prog < oreBlocks.get(key))
            {
                this.ores.add(key);
            }
        }

        for (IBlockState key : sampleBlocks.keySet())
        {
            int prog = 0;
            while (prog < sampleBlocks.get(key))
            {
                this.samples.add(key);
            }
        }

        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.chance = chance;
        this.dimensionBlacklist = dimensionBlacklist;
        this.blockStateMatchers = blockStateMatchers;
    }

    public ArrayList<IBlockState> getOres()
    {
        return this.ores;
    }

    public ArrayList<IBlockState> getSamples()
    {
        return this.samples;
    }

    public IBlockState getOre()
    {
        return this.ores.get(new Random().nextInt(100));
    }

    public IBlockState getSample()
    {
        return this.samples.get(new Random().nextInt(100));
    }

    public String getFriendlyName()
    {
        StringBuilder sb = new StringBuilder();

        for (IBlockState state : this.ores)
        {
            String name = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName();
            // The name hasn't already been added
            if(sb.indexOf(name) != -1) {
                sb.append(" & ");
                sb.append(name);
            }
        }
        return sb.toString();
    }

    public int getYMin()
    {
        return this.yMin;
    }

    public int getYMax()
    {
        return this.yMax;
    }

    public int getChance()
    {
        return this.chance;
    }

    public int getSize()
    {
        return this.size;
    }

    public int[] getDimensionBlacklist()
    {
        return this.dimensionBlacklist;
    }

    public boolean canReplace(IBlockState state)
    {
        if (this.blockStateMatchers == null)
        {
            return true;
        }
        for (IBlockState s : this.blockStateMatchers)
        {
            if (s == state)
            {
                return true;
            }
        }
        return this.blockStateMatchers.contains(state);
    }
}