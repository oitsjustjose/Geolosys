package com.oitsjustjose.geolosys.common.api.world;

import java.util.List;

import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.util.Utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class DepositStone implements IOre {
    private IBlockState block;
    private int yMin;
    private int yMax;
    private int chance;
    private int size;
    private int[] dimBlacklist;

    public DepositStone(IBlockState stoneBlock, int yMin, int yMax, int chance, int size, int[] dimBlacklist) {
        this.block = stoneBlock;
        this.yMin = yMin;
        this.yMax = yMax;
        this.chance = chance;
        this.size = size;
        this.dimBlacklist = dimBlacklist;
    }

    public int[] getDimensionBlacklist() {
        return this.dimBlacklist;
    }

    public IBlockState getOre() {
        return this.block;
    }

    public IBlockState getSample() {
        return null;
    }

    public String getFriendlyName() {
        return new ItemStack(this.block.getBlock(), 1, this.block.getBlock().getMetaFromState(this.block))
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

    public boolean canReplace(IBlockState state) {
        for (IBlockState s : GeolosysAPI.replacementMats) {
            if (Utils.doStatesMatch(state, s)) {
                return true;
            }
        }
        return false;
    }

    public List<IBlockState> getBlockStateMatchers() {
        return null;
    }

    public boolean oreMatches(IBlockState other) {
        return Utils.doStatesMatch(other, this.block);
    }

    public boolean sampleMatches(IBlockState other) {
        return true;
    }

    public float getDensity() {
        return 1.0F;
    }
}