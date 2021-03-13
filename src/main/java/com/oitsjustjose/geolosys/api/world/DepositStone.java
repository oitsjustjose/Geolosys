package com.oitsjustjose.geolosys.api.world;

import java.util.HashSet;

import com.oitsjustjose.geolosys.api.PlutonType;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.BlockState;

public class DepositStone implements IDeposit {
    private BlockState block;
    private int yMin;
    private int yMax;
    private int chance;
    private int size;
    private String[] dimFilter;
    private boolean isDimFilterBl;

    public DepositStone(BlockState stoneBlock, int yMin, int yMax, int chance, int size, String[] dimFilter,
            boolean isDimFilterBl) {
        this.block = stoneBlock;
        this.yMin = yMin;
        this.yMax = yMax;
        this.chance = chance;
        this.size = size;
        this.dimFilter = dimFilter;
        this.isDimFilterBl = isDimFilterBl;
    }

    public String[] getDimensionFilter() {
        return this.dimFilter;
    }

    public boolean isDimensionFilterBlacklist() {
        return this.isDimFilterBl;
    }

    public BlockState getOre() {
        return this.block;
    }

    public BlockState getSampleBlock() {
        return null;
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

    public boolean canReplace(BlockState state) {
        for (BlockState s : Utils.getDefaultMatchers()) {
            if (Utils.doStatesMatch(state, s)) {
                return true;
            }
        }
        return false;
    }

    public HashSet<BlockState> getBlockStateMatchers() {
        return null;
    }

    public boolean oreMatches(BlockState other) {
        return Utils.doStatesMatch(other, this.block);
    }

    public boolean sampleMatches(BlockState other) {
        return true;
    }

    public float getDensity() {
        return 1.0F;
    }

    public PlutonType getPlutonType() {
        return PlutonType.DENSE;
    }

    public String getFriendlyName() {
        return Utils.blockStateToName(this.getOre());
    }
}