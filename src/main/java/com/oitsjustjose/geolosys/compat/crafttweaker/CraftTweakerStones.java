package com.oitsjustjose.geolosys.compat.crafttweaker;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("crafttweaker")
@ZenRegister
@ZenClass("mods.geolosys.stones")
public class CraftTweakerStones {

    @ZenMethod
    public static void addStone(crafttweaker.api.block.IBlockState stoneBlock, int yMin, int yMax, int chance, int size,
            int[] dimBlacklist) {
        if (CraftTweakerMC.getBlockState(stoneBlock) != null) {
            GeolosysAPI.registerStoneDeposit(CraftTweakerMC.getBlockState(stoneBlock), yMin, yMax, chance, size,
                    dimBlacklist);
        } else {
            Geolosys.getInstance().LOGGER.info("There was an error parsing a CraftTweaker-made stone");
        }
    }

}