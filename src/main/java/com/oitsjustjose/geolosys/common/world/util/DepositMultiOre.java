package com.oitsjustjose.geolosys.common.world.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class DepositMultiOre implements IOre
{
    private ArrayList<IBlockState> ores = new ArrayList<>();
    private ArrayList<IBlockState> samples = new ArrayList<>();
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

        Geolosys.getInstance().LOGGER.info("Creating object...");
        int last = 0;
        for (IBlockState key : oreBlocks.keySet())
        {
            for (int i = last; i < last + oreBlocks.get(key); i++)
            {
                this.ores.add(i, key);
            }
            last += oreBlocks.get(key);
        }
        Geolosys.getInstance().LOGGER.info("Done creating the ores arraylist");

        last = 0;
        for (IBlockState key : sampleBlocks.keySet())
        {
            for (int i = last; i < last + sampleBlocks.get(key); i++)
            {
                this.samples.add(i, key);
            }
            last += sampleBlocks.get(key);
        }

        Geolosys.getInstance().LOGGER.info("Done creating the samples arraylist");

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
            if (sb.indexOf(name) == -1)
            {
                sb.append(" & ");
                sb.append(name);
            }
        }
        // Return substr(3) to ignore the first " & "
        return sb.toString().substring(3);
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

    public List<IBlockState> getBlockStateMatchers()
    {
        return this.blockStateMatchers;
    }
}