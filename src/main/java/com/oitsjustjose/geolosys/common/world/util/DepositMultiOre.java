package com.oitsjustjose.geolosys.common.world.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.state.IBlockState;

public class DepositMultiOre implements IMultiOre
{
    private ArrayList<IBlockState> ores;
    private ArrayList<IBlockState> samples;

    public DepositMultiOre(HashMap<IBlockState, Integer> oreBlocks, HashMap<IBlockState, Integer> sampleBlocks)
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
}