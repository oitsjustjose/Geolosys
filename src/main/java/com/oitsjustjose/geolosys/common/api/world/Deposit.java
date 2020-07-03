package com.oitsjustjose.geolosys.common.api.world;

import java.util.List;

import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class Deposit implements IOre {
    private IBlockState oreBlock;
    private IBlockState sampleBlock;
    private int yMin;
    private int yMax;
    private int size;
    private int chance;
    private int[] dimensionBlacklist;
    private List<IBlockState> blockStateMatchers;
    private float density;

    public Deposit(IBlockState oreBlock, IBlockState sampleBlock, int yMin, int yMax, int size, int chance,
            int[] dimensionBlacklist, List<IBlockState> blockStateMatchers, float density) {
        this.oreBlock = oreBlock;
        this.sampleBlock = sampleBlock;
        this.yMin = yMin;
        this.yMax = yMax;
        this.size = size;
        this.chance = chance;
        this.dimensionBlacklist = dimensionBlacklist;
        this.blockStateMatchers = blockStateMatchers;
        this.density = density;
    }

    public IBlockState getOre() {
        return this.oreBlock;
    }

    public IBlockState getSample() {
        return this.sampleBlock;
    }

    public String getFriendlyName() {
        return new ItemStack(this.oreBlock.getBlock(), 1, this.oreBlock.getBlock().getMetaFromState(this.oreBlock))
                .getDisplayName();
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
            if (Utils.doStatesMatch(s, state)) {
                return true;
            }
        }
        return false;
    }

    public List<IBlockState> getBlockStateMatchers() {
        return this.blockStateMatchers;
    }

    public boolean oreMatches(IBlockState other) {
        return Utils.doStatesMatch(this.oreBlock, other);
    }

    public boolean sampleMatches(IBlockState other) {
        return Utils.doStatesMatch(this.sampleBlock, other);
    }

    public float getDensity() {
        return this.density;
    }
}