package com.oitsjustjose.geolosys.compat.crafttweaker;

import java.util.ArrayList;
import java.util.List;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("crafttweaker")
@ZenRegister
@ZenClass("mods.geolosys.ores")
public class CraftTweakerOres
{
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState oreBlock,
                        crafttweaker.api.block.IBlockState sampleBlock, int yMin, int yMax, int size, int chance,
                        int[] dimBlacklist)
        {
                if (CraftTweakerMC.getBlockState(oreBlock) == null)
                {
                        Geolosys.getInstance().LOGGER.info("There was an error parsing a CraftTweaker-made ore block");
                }
                else if (CraftTweakerMC.getBlockState(sampleBlock) == null)
                {
                        Geolosys.getInstance().LOGGER
                                        .info("There was an error parsing a CraftTweaker-made sample block");
                }
                else
                {
                        GeolosysAPI.registerMineralDeposit(CraftTweakerMC.getBlockState(oreBlock),
                                        CraftTweakerMC.getBlockState(sampleBlock), yMin, yMax, size, chance,
                                        dimBlacklist);
                }
        }

        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState oreBlock,
                        crafttweaker.api.block.IBlockState sampleBlock, int yMin, int yMax, int size, int chance,
                        int[] dimBlacklist, crafttweaker.api.block.IBlockState[] blockStateMatchers)
        {
                List<IBlockState> toMCStates = new ArrayList<IBlockState>();
                for (crafttweaker.api.block.IBlockState state : blockStateMatchers)
                {
                        if (CraftTweakerMC.getBlockState(state) == null)
                        {
                                Geolosys.getInstance().LOGGER.info(
                                                "There was an error parsing a CraftTweaker-made blockStateMatcher. It has been skipped but the rest of the array has not been.");
                        }
                        toMCStates.add(CraftTweakerMC.getBlockState(state));
                }
                if (CraftTweakerMC.getBlockState(oreBlock) == null)
                {
                        Geolosys.getInstance().LOGGER.info("There was an error parsing a CraftTweaker-made ore block");
                }
                else if (CraftTweakerMC.getBlockState(sampleBlock) == null)
                {
                        Geolosys.getInstance().LOGGER
                                        .info("There was an error parsing a CraftTweaker-made sample block");
                }
                else
                {
                        GeolosysAPI.registerMineralDeposit(CraftTweakerMC.getBlockState(oreBlock),
                                        CraftTweakerMC.getBlockState(sampleBlock), yMin, yMax, size, chance,
                                        dimBlacklist, toMCStates);
                }
        }
}