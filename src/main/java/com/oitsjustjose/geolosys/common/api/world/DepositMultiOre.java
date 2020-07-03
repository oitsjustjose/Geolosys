package com.oitsjustjose.geolosys.common.api.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@SuppressWarnings("unchecked")
public class DepositMultiOre implements IOre {
    private ArrayList<IBlockState> ores = new ArrayList<>();
    private ArrayList<IBlockState> samples = new ArrayList<>();
    public HashMap<IBlockState, Integer> oreBlocks;
    public HashMap<IBlockState, Integer> sampleBlocks;
    private int yMin;
    private int yMax;
    private int size;
    private int chance;
    private int[] dimensionBlacklist;
    private List<IBlockState> blockStateMatchers;
    private float density;
    private String customName;

    public DepositMultiOre(HashMap<IBlockState, Integer> oreBlocks, HashMap<IBlockState, Integer> sampleBlocks,
            int yMin, int yMax, int size, int chance, int[] dimensionBlacklist, List<IBlockState> blockStateMatchers,
            float density, @Nullable String customName) {
        // Sanity checking:
        int sum = 0;
        for (IBlockState key : oreBlocks.keySet()) {
            sum += oreBlocks.get(key);
        }
        assert sum == 100 : "Sums of chances should equal 100";
        sum = 0;
        for (IBlockState key : sampleBlocks.keySet()) {
            sum += sampleBlocks.get(key);
        }
        assert sum == 100 : "Sums of chances should equal 100";

        int last = 0;
        for (IBlockState key : oreBlocks.keySet()) {
            for (int i = last; i < last + oreBlocks.get(key); i++) {
                this.ores.add(i, key);
            }
            last += oreBlocks.get(key);
        }

        last = 0;
        for (IBlockState key : sampleBlocks.keySet()) {
            for (int i = last; i < last + sampleBlocks.get(key); i++) {
                this.samples.add(i, key);
            }
            last += sampleBlocks.get(key);
        }

        this.oreBlocks = (HashMap<IBlockState, Integer>) oreBlocks.clone();
        this.sampleBlocks = (HashMap<IBlockState, Integer>) sampleBlocks.clone();
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.chance = chance;
        this.dimensionBlacklist = dimensionBlacklist;
        this.blockStateMatchers = blockStateMatchers;
        this.density = density;
        if (customName != null) {
            this.customName = customName;
        }
    }

    public ArrayList<IBlockState> getOres() {
        return this.ores;
    }

    public ArrayList<IBlockState> getSamples() {
        return this.samples;
    }

    public IBlockState getOre() {
        IBlockState backup = null;
        try {
            return this.ores.get(new Random().nextInt(100));
        } catch (IndexOutOfBoundsException e) {
            for (IBlockState s : this.oreBlocks.keySet()) {
                backup = s;
                break;
            }
        }
        return backup;
    }

    public IBlockState getSample() {
        IBlockState backup = null;
        try {
            return this.samples.get(new Random().nextInt(100));
        } catch (IndexOutOfBoundsException e) {
            for (IBlockState s : this.sampleBlocks.keySet()) {
                backup = s;
                break;
            }
        }
        return backup;
    }

    public String getFriendlyName() {
        if (this.customName != null) {
            return this.customName;
        }

        StringBuilder sb = new StringBuilder();

        for (IBlockState state : this.ores) {
            String name = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state)).getDisplayName();
            // The name hasn't already been added
            if (sb.indexOf(name) == -1) {
                sb.append(" & ");
                sb.append(name);
            }
        }
        // Return substr(3) to ignore the first " & "
        return sb.toString().substring(3);
    }

    public String getFriendlyName(World world, BlockPos pos, EntityPlayer player) {
        StringBuilder sb = new StringBuilder();

        for (IBlockState state : this.ores) {
            String name = Utils.blockStateToName(state, world, pos, player);
            // The name hasn't already been added
            if (sb.indexOf(name) == -1) {
                sb.append(" & ");
                sb.append(name);
            }
        }
        // Return substr(3) to ignore the first " & "
        return sb.toString().substring(3);
    }

    public int getYMin() {
        return this.yMin;
    }

    public int getYMax() {
        return this.yMax;
    }

    public int getChance() {
        return this.chance;
    }

    public int getSize() {
        return this.size;
    }

    public int[] getDimensionBlacklist() {
        return this.dimensionBlacklist;
    }

    public boolean canReplace(IBlockState state) {
        if (this.blockStateMatchers == null) {
            return true;
        }
        for (IBlockState s : this.blockStateMatchers) {
            if (s == state) {
                return true;
            }
        }
        return this.blockStateMatchers.contains(state);
    }

    public List<IBlockState> getBlockStateMatchers() {
        return this.blockStateMatchers;
    }

    public boolean oreMatches(ArrayList<IBlockState> other) {
        for (IBlockState state1 : this.oreBlocks.keySet()) {
            boolean isMatchInOtherArrayList = false;
            for (IBlockState state2 : other) {
                if (Utils.doStatesMatch(state1, state2)) {
                    isMatchInOtherArrayList = true;
                    break;
                }
            }
            if (!isMatchInOtherArrayList) {
                return false;
            }
        }

        return this.oreBlocks.size() == other.size();
    }

    public boolean sampleMatches(ArrayList<IBlockState> other) {
        for (IBlockState state1 : this.sampleBlocks.keySet()) {
            boolean isMatchInOtherArrayList = false;
            for (IBlockState state2 : other) {
                if (Utils.doStatesMatch(state1, state2)) {
                    isMatchInOtherArrayList = true;
                    break;
                }
            }
            if (!isMatchInOtherArrayList) {
                return false;
            }
        }
        return this.sampleBlocks.size() == other.size();
    }

    public boolean oreMatches(IBlockState other) {
        for (IBlockState s : this.ores) {
            if (Utils.doStatesMatch(s, other)) {
                return true;
            }
        }
        return false;
    }

    public boolean sampleMatches(IBlockState other) {
        for (IBlockState s : this.samples) {
            if (Utils.doStatesMatch(s, other)) {
                return true;
            }
        }
        return false;
    }

    public float getDensity() {
        return this.density;
    }
}