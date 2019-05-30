package com.oitsjustjose.geolosys.compat.crafttweaker;

import java.util.ArrayList;
import java.util.HashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;

import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("crafttweaker")
@ZenRegister
@ZenClass("mods.geolosys.ores")
public class CraftTweakerOres
{
        /**
         * A CraftTweaker function to wrap the creation of a Deposit with default blockStateMatchers
         * 
         * @param oreBlock     The Blockstate of the ore to be placed.
         * @param sampleBlock  The blockstate of the sample to be placed to represent the oreBlock
         * @param yMin         The minimum Y level this deposit should be found
         * @param yMax         The maximum Y level this deposit should be found
         * @param size         The approximate size of this deposit
         * @param chance       The relative chance of this deposit's occurance in the world
         * @param dimBlacklist A blacklist of dimension IDs which the deposit may not appear in
         */
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
                                        dimBlacklist, null);
                }
        }

        /**
         * A CraftTweaker function to wrap the creation of a Deposit with custom blockStateMatchers
         * 
         * @param oreBlock           The Blockstate of the ore to be placed.
         * @param sampleBlock        The blockstate of the sample to be placed to represent the oreBlock
         * @param yMin               The minimum Y level this deposit should be found
         * @param yMax               The maximum Y level this deposit should be found
         * @param size               The approximate size of this deposit
         * @param chance             The relative chance of this deposit's occurance in the world
         * @param dimBlacklist       A blacklist of dimension IDs which the deposit may not appear in
         * @param blockStateMatchers An array of Blockstates which the deposit can replace during generation
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState oreBlock,
                        crafttweaker.api.block.IBlockState sampleBlock, int yMin, int yMax, int size, int chance,
                        int[] dimBlacklist, crafttweaker.api.block.IBlockState[] blockStateMatchers)
        {
                ArrayList<IBlockState> toMCStates = new ArrayList<IBlockState>();
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

        /**
         * A CraftTweaker function to wrap the creation of a DepositBiomeRestricted with default blockStateMatchers
         * 
         * @param oreBlock     The Blockstate of the ore to be placed.
         * @param sampleBlock  The blockstate of the sample to be placed to represent the oreBlock
         * @param yMin         The minimum Y level this deposit should be found
         * @param yMax         The maximum Y level this deposit should be found
         * @param size         The approximate size of this deposit
         * @param chance       The relative chance of this deposit's occurance in the world
         * @param dimBlacklist A blacklist of dimension IDs which the deposit may not appear in
         * @param biomes       A list of strings which represent the Biome registry names (from /ct biomes)
         * @param isWhitelist  A boolean value describing whether or not the biomes list is a whitelist or not.
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState oreBlock,
                        crafttweaker.api.block.IBlockState sampleBlock, int yMin, int yMax, int size, int chance,
                        int[] dimBlacklist, String[] biomes, boolean isWhitelist)
        {
                ArrayList<Biome> toMCBiomes = new ArrayList<Biome>();

                for (String biome : biomes)
                {
                        Biome b = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biome));
                        if (b == null)
                        {
                                Geolosys.getInstance().LOGGER.info("The biome name " + biome
                                                + " does not seem to exist. Try finding it using /ct biomes in game");
                        }
                        else
                        {
                                toMCBiomes.add(b);
                        }
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
                                        dimBlacklist, null, toMCBiomes, isWhitelist);
                }
        }

        /**
         * A CraftTweaker function to wrap the creation of a DepositBiomeRestricted with custom blockStateMatchers
         * 
         * @param oreBlock           The Blockstate of the ore to be placed.
         * @param sampleBlock        The blockstate of the sample to be placed to represent the oreBlock
         * @param yMin               The minimum Y level this deposit should be found
         * @param yMax               The maximum Y level this deposit should be found
         * @param size               The approximate size of this deposit
         * @param chance             The relative chance of this deposit's occurance in the world
         * @param dimBlacklist       A blacklist of dimension IDs which the deposit may not appear in
         * @param blockStateMatchers An array of Blockstates which the deposit can replace during generation
         * @param biomes             A list of strings which represent the Biome registry names (from /ct biomes)
         * @param isWhitelist        A boolean value describing whether or not the biomes list is a whitelist or not.
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState oreBlock,
                        crafttweaker.api.block.IBlockState sampleBlock, int yMin, int yMax, int size, int chance,
                        int[] dimBlacklist, String[] biomes, boolean isWhitelist,
                        crafttweaker.api.block.IBlockState[] blockStateMatchers)
        {
                ArrayList<IBlockState> toMCStates = new ArrayList<IBlockState>();
                ArrayList<Biome> toMCBiomes = new ArrayList<Biome>();

                for (crafttweaker.api.block.IBlockState state : blockStateMatchers)
                {
                        if (CraftTweakerMC.getBlockState(state) == null)
                        {
                                Geolosys.getInstance().LOGGER.info(
                                                "There was an error parsing a CraftTweaker-made blockStateMatcher. It has been skipped but the rest of the array has not been.");
                        }
                        toMCStates.add(CraftTweakerMC.getBlockState(state));
                }

                for (String biome : biomes)
                {
                        Biome b = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biome));
                        if (b == null)
                        {
                                Geolosys.getInstance().LOGGER.info("The biome name " + biome
                                                + " does not seem to exist. Try finding it using /ct biomes in game");
                        }
                        else
                        {
                                toMCBiomes.add(b);
                        }
                }

                Geolosys.getInstance().LOGGER.info(toMCBiomes);

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
                                        dimBlacklist, toMCStates, toMCBiomes, isWhitelist);
                }
        }

        /**
         * A CraftTweaker function to wrap the creation of a DepositMultiOre with default blockStateMatchers
         * 
         * @param oreBlocks          The list of Blockstates of the ores to be placed.
         * @param oreBlockChances    The list of percentages that each oreBlock has to gnenerate
         * @param sampleBlocks       The blockstates of the samples to be placed to represent the oreBlocks
         * @param sampleBlockChances The list of percentages that each sampleBlock has to gnenerate
         * @param yMin               The minimum Y level this deposit should be found
         * @param yMax               The maximum Y level this deposit should be found
         * @param size               The approximate size of this deposit
         * @param chance             The relative chance of this deposit's occurance in the world
         * @param dimBlacklist       A blacklist of dimension IDs which the deposit may not appear in
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState[] oreBlocks, int[] oreBlockChances,
                        crafttweaker.api.block.IBlockState[] sampleBlocks, int[] sampleBlockChances, int yMin, int yMax,
                        int size, int chance, int[] dimBlacklist)
        {
                HashMap<IBlockState, Integer> oreBlockMap = new HashMap<>();
                HashMap<IBlockState, Integer> sampleBlockMap = new HashMap<>();
                if (oreBlockChances.length != oreBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of oreBlocks to oreBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                if (sampleBlockChances.length != sampleBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of sampleBlocks to sampleBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        oreBlockMap.put(CraftTweakerMC.getBlockState(oreBlocks[i]), oreBlockChances[i]);
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        sampleBlockMap.put(CraftTweakerMC.getBlockState(sampleBlocks[i]), sampleBlockChances[i]);
                }
                GeolosysAPI.registerMineralDeposit(oreBlockMap, sampleBlockMap, yMin, yMax, size, chance, dimBlacklist,
                                null);
        }

        /**
         * A CraftTweaker function to wrap the creation of a DepositMultiOre with custom blockStateMatchers
         * 
         * @param oreBlocks          The list of Blockstates of the ores to be placed.
         * @param oreBlockChances    The list of percentages that each oreBlock has to gnenerate
         * @param sampleBlocks       The blockstates of the samples to be placed to represent the oreBlocks
         * @param sampleBlockChances The list of percentages that each sampleBlock has to gnenerate
         * @param yMin               The minimum Y level this deposit should be found
         * @param yMax               The maximum Y level this deposit should be found
         * @param size               The approximate size of this deposit
         * @param chance             The relative chance of this deposit's occurance in the world
         * @param dimBlacklist       A blacklist of dimension IDs which the deposit may not appear in
         * @param blockStateMatchers An array of Blockstates which the deposit can replace during generation
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState[] oreBlocks, int[] oreBlockChances,
                        crafttweaker.api.block.IBlockState[] sampleBlocks, int[] sampleBlockChances, int yMin, int yMax,
                        int size, int chance, int[] dimBlacklist,
                        crafttweaker.api.block.IBlockState[] blockStateMatchers)
        {
                ArrayList<IBlockState> toMCStates = new ArrayList<IBlockState>();
                for (crafttweaker.api.block.IBlockState state : blockStateMatchers)
                {
                        if (CraftTweakerMC.getBlockState(state) == null)
                        {
                                Geolosys.getInstance().LOGGER.info(
                                                "There was an error parsing a CraftTweaker-made blockStateMatcher. It has been skipped but the rest of the array has not been.");
                        }
                        toMCStates.add(CraftTweakerMC.getBlockState(state));
                }
                HashMap<IBlockState, Integer> oreBlockMap = new HashMap<>();
                HashMap<IBlockState, Integer> sampleBlockMap = new HashMap<>();
                if (oreBlockChances.length != oreBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of oreBlocks to oreBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                if (sampleBlockChances.length != sampleBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of sampleBlocks to sampleBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        oreBlockMap.put(CraftTweakerMC.getBlockState(oreBlocks[i]), oreBlockChances[i]);
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        sampleBlockMap.put(CraftTweakerMC.getBlockState(sampleBlocks[i]), sampleBlockChances[i]);
                }
                GeolosysAPI.registerMineralDeposit(oreBlockMap, sampleBlockMap, yMin, yMax, size, chance, dimBlacklist,
                                toMCStates);
        }

        /**
         * A CraftTweaker function to wrap the creation of a DepositMultiOreBiomeRestricted with default blockStateMatchers
         * 
         * @param oreBlocks          The list of Blockstates of the ores to be placed.
         * @param oreBlockChances    The list of percentages that each oreBlock has to gnenerate
         * @param sampleBlocks       The blockstates of the samples to be placed to represent the oreBlocks
         * @param sampleBlockChances The list of percentages that each sampleBlock has to gnenerate
         * @param yMin               The minimum Y level this deposit should be found
         * @param yMax               The maximum Y level this deposit should be found
         * @param size               The approximate size of this deposit
         * @param chance             The relative chance of this deposit's occurance in the world
         * @param dimBlacklist       A blacklist of dimension IDs which the deposit may not appear in
         * @param biomes             A list of strings which represent the Biome registry names (from /ct biomes)
         * @param isWhitelist        A boolean value describing whether or not the biomes list is a whitelist or not.
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState[] oreBlocks, int[] oreBlockChances,
                        crafttweaker.api.block.IBlockState[] sampleBlocks, int[] sampleBlockChances, int yMin, int yMax,
                        int size, int chance, int[] dimBlacklist, String[] biomes, boolean isWhitelist)
        {
                ArrayList<Biome> toMCBiomes = new ArrayList<Biome>();

                for (String biome : biomes)
                {
                        Biome b = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biome));
                        if (b == null)
                        {
                                Geolosys.getInstance().LOGGER.info("The biome name " + biome
                                                + " does not seem to exist. Try finding it using /ct biomes in game");
                        }
                        else
                        {
                                toMCBiomes.add(b);
                        }
                }

                HashMap<IBlockState, Integer> oreBlockMap = new HashMap<>();
                HashMap<IBlockState, Integer> sampleBlockMap = new HashMap<>();
                if (oreBlockChances.length != oreBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of oreBlocks to oreBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                if (sampleBlockChances.length != sampleBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of sampleBlocks to sampleBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        oreBlockMap.put(CraftTweakerMC.getBlockState(oreBlocks[i]), oreBlockChances[i]);
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        sampleBlockMap.put(CraftTweakerMC.getBlockState(sampleBlocks[i]), sampleBlockChances[i]);
                }
                GeolosysAPI.registerMineralDeposit(oreBlockMap, sampleBlockMap, yMin, yMax, size, chance, dimBlacklist,
                                null, toMCBiomes, isWhitelist);
        }

        /**
         * A CraftTweaker function to wrap the creation of a DepositMultiOreBiomeRestricted with custom blockStateMatchers
         * 
         * @param oreBlocks          The list of Blockstates of the ores to be placed.
         * @param oreBlockChances    The list of percentages that each oreBlock has to gnenerate
         * @param sampleBlocks       The blockstates of the samples to be placed to represent the oreBlocks
         * @param sampleBlockChances The list of percentages that each sampleBlock has to gnenerate
         * @param yMin               The minimum Y level this deposit should be found
         * @param yMax               The maximum Y level this deposit should be found
         * @param size               The approximate size of this deposit
         * @param chance             The relative chance of this deposit's occurance in the world
         * @param dimBlacklist       A blacklist of dimension IDs which the deposit may not appear in
         * @param biomes             A list of strings which represent the Biome registry names (from /ct biomes)
         * @param isWhitelist        A boolean value describing whether or not the biomes list is a whitelist or not.
         * @param blockStateMatchers An array of Blockstates which the deposit can replace during generation
         */
        @ZenMethod
        public static void addOre(crafttweaker.api.block.IBlockState[] oreBlocks, int[] oreBlockChances,
                        crafttweaker.api.block.IBlockState[] sampleBlocks, int[] sampleBlockChances, int yMin, int yMax,
                        int size, int chance, int[] dimBlacklist, String[] biomes, boolean isWhitelist,
                        crafttweaker.api.block.IBlockState[] blockStateMatchers)
        {
                ArrayList<Biome> toMCBiomes = new ArrayList<Biome>();

                for (String biome : biomes)
                {
                        Biome b = ForgeRegistries.BIOMES.getValue(new ResourceLocation(biome));
                        if (b == null)
                        {
                                Geolosys.getInstance().LOGGER.info("The biome name " + biome
                                                + " does not seem to exist. Try finding it using /ct biomes in game");
                        }
                        else
                        {
                                toMCBiomes.add(b);
                        }
                }

                ArrayList<IBlockState> toMCStates = new ArrayList<IBlockState>();
                for (crafttweaker.api.block.IBlockState state : blockStateMatchers)
                {
                        if (CraftTweakerMC.getBlockState(state) == null)
                        {
                                Geolosys.getInstance().LOGGER.info(
                                                "There was an error parsing a CraftTweaker-made blockStateMatcher. It has been skipped but the rest of the array has not been.");
                        }
                        toMCStates.add(CraftTweakerMC.getBlockState(state));
                }
                HashMap<IBlockState, Integer> oreBlockMap = new HashMap<>();
                HashMap<IBlockState, Integer> sampleBlockMap = new HashMap<>();
                if (oreBlockChances.length != oreBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of oreBlocks to oreBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                if (sampleBlockChances.length != sampleBlocks.length)
                {
                        Geolosys.getInstance().LOGGER.info(
                                        "The length of sampleBlocks to sampleBlockChances were not the same. They MUST be the same. This entry has been skipped");
                        return;
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        oreBlockMap.put(CraftTweakerMC.getBlockState(oreBlocks[i]), oreBlockChances[i]);
                }
                for (int i = 0; i < oreBlocks.length; i++)
                {
                        sampleBlockMap.put(CraftTweakerMC.getBlockState(sampleBlocks[i]), sampleBlockChances[i]);
                }
                GeolosysAPI.registerMineralDeposit(oreBlockMap, sampleBlockMap, yMin, yMax, size, chance, dimBlacklist,
                                toMCStates, toMCBiomes, isWhitelist);
        }
}