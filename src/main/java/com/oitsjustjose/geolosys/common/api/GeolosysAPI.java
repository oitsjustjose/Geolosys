package com.oitsjustjose.geolosys.common.api;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.world.OreGenerator;
import com.oitsjustjose.geolosys.common.world.StoneGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The Geolosys API is intended for use by anyone who wants to tap into all the locations that deposits exist
 * Access is pretty easy, just reference this class's currentWorldDeposits HashMap
 */
public class GeolosysAPI
{
    // A K:V pair of block states to pair deposits with their samples
    public static HashMap<IBlockState, IBlockState> oreBlocks = new HashMap<>();
    // A collection of blocks which Geolosys can replace in generation
    public static ArrayList<IBlockState> replacementMats = new ArrayList<>();
    // A matched pair of IBlockStates whose K:V = block:replacement
    public static HashMap<IBlockState, List<IBlockState>> oreBlocksSpecific = new HashMap<>();
    // A collection of blocks to ignore in the OreConverter feature
    public static ArrayList<IBlockState> oreConverterBlacklist = new ArrayList<>();
    // A K:V pair of IBlockStates with their sample sizes
    public static HashMap<IBlockState, Integer> sampleCounts = new HashMap<>();


    /**
     * Adds a deposit for Geolosys to handle the generation of.
     *
     * @param oreBlock     The block you want UNDERGROUND as an ore
     * @param sampleBlock  The block you want ON THE SURFACE as a sample
     * @param yMin         The minimum Y level this deposit can generate at
     * @param yMax         The maximum Y level this deposit can generate at
     * @param size         The size of the deposit
     * @param chance       The chance of the deposit generating (higher = more likely)
     * @param dimBlacklist An array of dimension numbers in which this deposit cannot generate
     */
    public static void registerMineralDeposit(IBlockState oreBlock, IBlockState sampleBlock, int yMin, int yMax, int size, int chance, int[] dimBlacklist)
    {
        OreGenerator.addOreGen(oreBlock, size, yMin, yMax, chance, dimBlacklist);
        oreBlocks.put(oreBlock, sampleBlock);
        sampleCounts.put(sampleBlock, size);
    }

    /**
     * Adds a deposit for Geolosys to handle the generation of.
     *
     * @param oreBlock    The block you want UNDERGROUND as an ore
     * @param sampleBlock The block you want ON THE SURFACE as a sample
     * @param ore         The ore (wrapper object) containing the generation info
     */
    public static void registerMineralDeposit(IBlockState oreBlock, IBlockState sampleBlock, ConfigOres.Ore ore)
    {
        registerMineralDeposit(oreBlock, sampleBlock, ore.getMinY(), ore.getMaxY(), ore.getSize(), ore.getChance(), ore.getBlacklist());
    }

    /**
     * Adds a deposit for Geolosys to handle the generation of.
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
    public static void registerMineralDeposit(IBlockState oreBlock, IBlockState sampleBlock, int yMin, int yMax, int size, int chance, int[] dimBlacklist, List<IBlockState> blockStateMatchers)
    {
        oreBlocks.put(oreBlock, sampleBlock);
        sampleCounts.put(sampleBlock, size);
        oreBlocksSpecific.put(oreBlock, blockStateMatchers);
        OreGenerator.addOreGen(oreBlock, size, yMin, yMax, chance, dimBlacklist);
    }


    /**
     * Ads a stone type for Geolosys to handle the generation of.
     *
     * @param stoneBlock The block you want to generate
     * @param yMin       The minimum Y level this deposit can generate at
     * @param yMax       The maximum Y level this deposit can generate at
     * @param chance     The chance of the deposit generating (higher = more likely)
     */
    public static void registerStoneDeposit(IBlockState stoneBlock, int yMin, int yMax, int chance)
    {
        StoneGenerator.addStoneGen(stoneBlock, yMin, yMax, chance);
        replacementMats.add(stoneBlock);
    }

    @SuppressWarnings("unchecked")
    public static LinkedHashMap<ChunkPosSerializable, Boolean> getRegennedChunks(File file)
    {
        LinkedHashMap<ChunkPosSerializable, Boolean> regennedChunksDeprecated = new LinkedHashMap<>();
        try
        {
            FileInputStream fileInRegen = new FileInputStream(file);
            ObjectInputStream inRegen = new ObjectInputStream(fileInRegen);
            regennedChunksDeprecated = (LinkedHashMap<ChunkPosSerializable, Boolean>) inRegen.readObject();
            inRegen.close();
            fileInRegen.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return regennedChunksDeprecated;
    }

    @SuppressWarnings("unchecked")
    public static HashMap<ChunkPosSerializable, String> getDeposits(File file)
    {
        HashMap<ChunkPosSerializable, String> currentWorldDepositsDeprecated = new HashMap<>();
        try
        {
            FileInputStream fileInDeposits = new FileInputStream(file);
            ObjectInputStream inDeposits = new ObjectInputStream(fileInDeposits);
            currentWorldDepositsDeprecated = (HashMap<ChunkPosSerializable, String>) inDeposits.readObject();
            inDeposits.close();
            fileInDeposits.close();
        }
        catch (IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return currentWorldDepositsDeprecated;
    }

    public static ChunkPosSerializable chunkPosSerializableFromString(String fromString)
    {
        String[] parts = fromString.replace("[", "").replace("]", "").split(",");
        return new ChunkPosSerializable(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    /**
     * ChunkPosSerializable is a serializable version of Mojang's ChunkPos
     * As such, it stores a chunk's X and Z position
     */
    public static class ChunkPosSerializable implements Serializable
    {
        private int x;
        private int z;
        private int dim;

        /**
         * @param pos A Mojang ChunkPos initializer for ChunkPosSerializable
         */
        public ChunkPosSerializable(ChunkPos pos, int dim)
        {
            this(pos.x, pos.z, dim);
        }

        /**
         * @param x The X position which the Chunk starts at
         * @param z The Z position which the Chunk starts at
         */
        public ChunkPosSerializable(int x, int z, int dim)
        {
            this.x = x;
            this.z = z;
            this.dim = dim;
        }

        /**
         * @return The X value at which the Chunk starts at
         */
        public int getX()
        {
            return this.x;
        }

        /**
         * @return The Z value at which the Chunk starts at
         */
        public int getZ()
        {
            return this.z;
        }

        /**
         * @return The dimension of the chunk
         */
        public int getDimension()
        {
            return this.dim;
        }

        /**
         * @return A Mojang ChunkPos variant of this object
         */
        public ChunkPos toChunkPos()
        {
            return new ChunkPos(this.x, this.z);
        }

        @Override
        public String toString()
        {
            return "[" + this.getX() + "," + this.getZ() + "," + this.getDimension() + "]";
        }

        @Override
        public boolean equals(Object other)
        {
            if (other == this)
            {
                return true;
            }
            else if (other instanceof ChunkPosSerializable)
            {
                ChunkPosSerializable c = (ChunkPosSerializable) other;
                return c.getX() == this.getX() && c.getZ() == this.getZ() && c.getDimension() == this.getDimension();
            }
            return false;
        }
    }

}
