package com.oitsjustjose.geolosys.api;

import com.oitsjustjose.geolosys.api.world.*;
import com.oitsjustjose.geolosys.common.world.PlutonRegistry;
import com.oitsjustjose.geolosys.common.world.capability.IPlutonCapability;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Geolosys API is intended for use by anyone who wants to tap into all the locations that deposits exist Access is pretty
 * easy, just reference this class's currentWorldDeposits HashMap
 */
public class GeolosysAPI
{
    // A collection of BlockStates that can trigger the prospector's pick
    public static ArrayList<BlockState> proPickExtras = new ArrayList<>();
    // A collection of blocks which Geolosys can replace in generation
    public static ArrayList<BlockState> replacementMats = new ArrayList<>();
    // A collection of blocks to ignore in the OreConverter feature
    public static ArrayList<BlockState> oreConverterBlacklist = new ArrayList<>();
    // An instance of the registry for all generatable plutons
    public static PlutonRegistry plutonRegistry = PlutonRegistry.getInstance();

    @CapabilityInject(IPlutonCapability.class)
    public static final Capability<IPlutonCapability> PLUTON_CAPABILITY = null;

    /**
     * Adds a GENERIC deposit for Geolosys to handle the generation of.
     *
     * @param oreBlock           The block you want UNDERGROUND as an ore
     * @param sampleBlock        The block you want ON THE SURFACE as a sample
     * @param yMin               The minimum Y level this deposit can generate at
     * @param yMax               The maximum Y level this deposit can generate at
     * @param size               The size of the deposit
     * @param chance             The chance of the deposit generating (higher = more likely)
     * @param dimBlacklist       An array of dimension numbers in which this deposit cannot generate
     * @param blockStateMatchers A collection of blocks that this entry specifically can replace
     */
    public static void registerMineralDeposit(BlockState oreBlock, BlockState sampleBlock, int yMin, int yMax, int size,
            int chance, String[] dimBlacklist, List<BlockState> blockStateMatchers, float density)
    {
        Deposit tempDeposit = new Deposit(oreBlock, sampleBlock, yMin, yMax, size, chance, dimBlacklist,
                blockStateMatchers, density);
        plutonRegistry.addOrePluton(tempDeposit);
    }

    /**
     * Adds a multi-ore deposit for Geolosys to handle the generation of.
     *
     * @param oreBlockMap        A HashMap of BlockState:Integer where the integer represents the chance of that ore.
     * @param sampleBlockMap     A HashMap of BlockState:Integer where the integer represents the chance of that sample.
     * @param yMin               The minimum Y level this deposit can generate at
     * @param yMax               The maximum Y level this deposit can generate at
     * @param size               The size of the deposit
     * @param chance             The chance of the deposit generating (higher = more likely)
     * @param dimBlacklist       An array of dimension numbers in which this deposit cannot generate
     * @param blockStateMatchers A collection of blocks that this entry specifically can replace
     */
    public static void registerMineralDeposit(HashMap<BlockState, Integer> oreBlockMap,
            HashMap<BlockState, Integer> sampleBlockMap, int yMin, int yMax, int size, int chance, String[] dimBlacklist,
            List<BlockState> blockStateMatchers, float density)
    {
        DepositMultiOre tempDeposit = new DepositMultiOre(oreBlockMap, sampleBlockMap, yMin, yMax, size, chance,
                dimBlacklist, blockStateMatchers, density);
        plutonRegistry.addOrePluton(tempDeposit);
    }

    /**
     * Adds a biome-restricted deposit for Geolosys to handle the generation of.
     *
     * @param oreBlock           The {@link BlockState} to register as the pluton contents
     * @param sampleBlock        The {@link BlockState} to register as the representative sample
     * @param yMin               The minimum Y level this deposit can generate at
     * @param yMax               The maximum Y level this deposit can generate at
     * @param size               The size of the deposit
     * @param chance             The chance of the deposit generating (higher = more likely)
     * @param dimBlacklist       An array of dimension numbers in which this deposit cannot generate
     * @param blockStateMatchers A collection of blocks that this entry specifically can replace
     * @param biomeList          A List of Biomes which are to be black or whitelisted
     * @param isWhitelist        A boolean to determine is the biomeList a blacklist or whitelist
     */
    public static void registerMineralDeposit(BlockState oreBlock, BlockState sampleBlock, int yMin, int yMax, int size,
            int chance, String[] dimBlacklist, List<BlockState> blockStateMatchers, List<Biome> biomeList,
            List<BiomeDictionary.Type> biomeTypes, boolean isWhitelist, float density)
    {
        DepositBiomeRestricted tempDeposit = new DepositBiomeRestricted(oreBlock, sampleBlock, yMin, yMax, size, chance,
                dimBlacklist, blockStateMatchers, biomeList, biomeTypes, isWhitelist, density);
        plutonRegistry.addOrePluton(tempDeposit);
    }

    /**
     * Adds a biome-restricted multi-ore deposit for Geolosys to handle the generation of.
     *
     * @param oreBlockMap        A HashMap of BlockState:Integer where the integer represents the chance of that ore.
     * @param sampleBlockMap     A HashMap of BlockState:Integer where the integer represents the chance of that sample.
     * @param yMin               The minimum Y level this deposit can generate at
     * @param yMax               The maximum Y level this deposit can generate at
     * @param size               The size of the deposit
     * @param chance             The chance of the deposit generating (higher = more likely)
     * @param dimBlacklist       An array of dimension numbers in which this deposit cannot generate
     * @param blockStateMatchers A collection of blocks that this entry specifically can replace
     * @param biomeList          A List of Biomes which are to be black or whitelisted
     * @param isWhitelist        A boolean to determine is the biomeList a blacklist or whitelist
     */
    public static void registerMineralDeposit(HashMap<BlockState, Integer> oreBlockMap,
            HashMap<BlockState, Integer> sampleBlockMap, int yMin, int yMax, int size, int chance, String[] dimBlacklist,
            List<BlockState> blockStateMatchers, List<Biome> biomeList, List<BiomeDictionary.Type> biomeTypes,
            boolean isWhitelist, float density)
    {
        DepositMultiOreBiomeRestricted tempDeposit = new DepositMultiOreBiomeRestricted(oreBlockMap, sampleBlockMap,
                yMin, yMax, size, chance, dimBlacklist, blockStateMatchers, biomeList, biomeTypes, isWhitelist,
                density);
        plutonRegistry.addOrePluton(tempDeposit);
    }

    /**
     * Ads a stone type for Geolosys to handle the generation of.
     *
     * @param stoneBlock The block you want to generate
     * @param yMin       The minimum Y level this deposit can generate at
     * @param yMax       The maximum Y level this deposit can generate at
     * @param chance     The chance of the deposit generating (higher = more likely)
     */
    public static void registerStoneDeposit(BlockState stoneBlock, int yMin, int yMax, int chance, int size,
            String[] dimBlacklist)
    {
        DepositStone tempDeposit = new DepositStone(stoneBlock, yMin, yMax, chance, size, dimBlacklist);
        plutonRegistry.addStonePluton(tempDeposit);
    }



}