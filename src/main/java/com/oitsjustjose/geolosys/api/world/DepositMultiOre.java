package com.oitsjustjose.geolosys.api.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;

@SuppressWarnings("unchecked")
public class DepositMultiOre implements IOre
{
    private ArrayList<BlockState> ores = new ArrayList<>();
    private ArrayList<BlockState> samples = new ArrayList<>();
    public HashMap<BlockState, Integer> oreBlocks;
    public HashMap<BlockState, Integer> sampleBlocks;
    private int yMin;
    private int yMax;
    private int size;
    private int chance;
    private int[] dimensionBlacklist;
    private List<BlockState> blockStateMatchers;
    private float density;

    public DepositMultiOre(HashMap<BlockState, Integer> oreBlocks, HashMap<BlockState, Integer> sampleBlocks, int yMin,
            int yMax, int size, int chance, int[] dimensionBlacklist, List<BlockState> blockStateMatchers,
            float density)
    {
        // Sanity checking:
        int sum = 0;
        for (BlockState key : oreBlocks.keySet())
        {
            sum += oreBlocks.get(key);
        }
        assert sum == 100 : "Sums of chances should equal 100";
        sum = 0;
        for (BlockState key : sampleBlocks.keySet())
        {
            sum += sampleBlocks.get(key);
        }
        assert sum == 100 : "Sums of chances should equal 100";

        int last = 0;
        for (BlockState key : oreBlocks.keySet())
        {
            for (int i = last; i < last + oreBlocks.get(key); i++)
            {
                this.ores.add(i, key);
            }
            last += oreBlocks.get(key);
        }

        last = 0;
        for (BlockState key : sampleBlocks.keySet())
        {
            for (int i = last; i < last + sampleBlocks.get(key); i++)
            {
                this.samples.add(i, key);
            }
            last += sampleBlocks.get(key);
        }

        this.oreBlocks = (HashMap<BlockState, Integer>) oreBlocks.clone();
        this.sampleBlocks = (HashMap<BlockState, Integer>) sampleBlocks.clone();
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.chance = chance;
        this.dimensionBlacklist = dimensionBlacklist;
        this.blockStateMatchers = blockStateMatchers;
        this.density = density;
    }

    public ArrayList<BlockState> getOres()
    {
        return this.ores;
    }

    public ArrayList<BlockState> getSamples()
    {
        return this.samples;
    }

    public BlockState getOre()
    {
        BlockState backup = null;
        try
        {
            return this.ores.get(new Random().nextInt(100));
        }
        catch (IndexOutOfBoundsException e)
        {
            for (BlockState s : this.oreBlocks.keySet())
            {
                backup = s;
                break;
            }
        }
        return backup;
    }

    public BlockState getSample()
    {
        BlockState backup = null;
        try
        {
            return this.samples.get(new Random().nextInt(100));
        }
        catch (IndexOutOfBoundsException e)
        {
            for (BlockState s : this.sampleBlocks.keySet())
            {
                backup = s;
                break;
            }
        }
        return backup;
    }

    public String getFriendlyName()
    {
        StringBuilder sb = new StringBuilder();

        for (BlockState state : this.ores)
        {
            String name = new ItemStack(state.getBlock(), 1).getDisplayName().toString();
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

    public boolean canReplace(BlockState state)
    {
        if (this.blockStateMatchers == null)
        {
            return true;
        }
        for (BlockState s : this.blockStateMatchers)
        {
            if (s == state)
            {
                return true;
            }
        }
        return this.blockStateMatchers.contains(state);
    }

    public List<BlockState> getBlockStateMatchers()
    {
        return this.blockStateMatchers;
    }

    public boolean oreMatches(ArrayList<BlockState> other)
    {
        for (BlockState state1 : this.oreBlocks.keySet())
        {
            boolean isMatchInOtherArrayList = false;
            for (BlockState state2 : other)
            {
                if (Utils.doStatesMatch(state1, state2))
                {
                    isMatchInOtherArrayList = true;
                    break;
                }
            }
            if (!isMatchInOtherArrayList)
            {
                return false;
            }
        }

        return this.oreBlocks.size() == other.size();
    }

    public boolean sampleMatches(ArrayList<BlockState> other)
    {
        for (BlockState state1 : this.sampleBlocks.keySet())
        {
            boolean isMatchInOtherArrayList = false;
            for (BlockState state2 : other)
            {
                if (Utils.doStatesMatch(state1, state2))
                {
                    isMatchInOtherArrayList = true;
                    break;
                }
            }
            if (!isMatchInOtherArrayList)
            {
                return false;
            }
        }
        return this.sampleBlocks.size() == other.size();
    }

    public boolean oreMatches(BlockState other)
    {
        for (BlockState s : this.ores)
        {
            if (Utils.doStatesMatch(s, other))
            {
                return true;
            }
        }
        return false;
    }

    public boolean sampleMatches(BlockState other)
    {
        for (BlockState s : this.samples)
        {
            if (Utils.doStatesMatch(s, other))
            {
                return true;
            }
        }
        return false;
    }

    public float getDensity()
    {
        return this.density;
    }
}