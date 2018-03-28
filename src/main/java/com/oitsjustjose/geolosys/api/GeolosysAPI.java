package com.oitsjustjose.geolosys.api;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.world.OreGenerator;
import com.oitsjustjose.geolosys.world.StoneGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Geolosys API is intended for use by anyone who wants to tap into all the locations that deposits exist
 * Access is pretty easy, just reference this class's currentWorldDeposits HashMap
 */
public class GeolosysAPI
{
    // Some local instance variables I don't want others having access to..
    private static File fileLocation = null;
    private static HashMap<ChunkPosSerializable, String> currentWorldDeposits = new HashMap<>();
    // A K:V pair of block states to pair deposits with their samples
    public static HashMap<IBlockState, IBlockState> oreBlocks = new HashMap<>();
    // A collection of blocks which Geolosys can replace in generation
    public static ArrayList<IBlockState> replacementMats = new ArrayList<>();
    // A K:V pair of IBlockStates with their sample sizes
    public static HashMap<IBlockState, Integer> sampleCounts = new HashMap<>();
    /**
     * @return The world's current deposits throughout the world. The string is formatted as modid:block:meta
     */
    @SuppressWarnings("unchecked")

    public static HashMap<ChunkPosSerializable, String> getCurrentWorldDeposits()
    {
        return (HashMap<ChunkPosSerializable, String>) currentWorldDeposits.clone();
    }

    /**
     * @param pos   The Mojang ChunkPos to act as a key
     * @param state The String to act as a value
     */
    public static void putWorldDeposit(ChunkPos pos, String state)
    {
        currentWorldDeposits.put(new ChunkPosSerializable(pos), state);
    }

    /**
     * @param pos   The ChunkPosSerializable to act as a key
     * @param state The String to act as a value
     */
    public static void putWorldDeposit(ChunkPosSerializable pos, String state)
    {
        currentWorldDeposits.put(pos, state);
    }

    /**
     * Reads in the currentWorldDeposits from GeolosysDeposits.dat
     */
    @SuppressWarnings("unchecked")
    public static void readFromFile()
    {
        if (DimensionManager.getCurrentSaveRootDirectory() == null)
        {
            return;
        }
        else if (fileLocation == null)
        {
            fileLocation = new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat");
        }
        try
        {
            if (fileLocation.exists())
            {
                FileInputStream fileIn = new FileInputStream(fileLocation);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                currentWorldDeposits = (HashMap<ChunkPosSerializable, String>) in.readObject();
                in.close();
                fileIn.close();
            }
        }
        catch (IOException i)
        {
            Geolosys.getInstance().LOGGER.error("There was an error loading GeolosysDeposits.dat");
        }
        catch (ClassNotFoundException c)
        {
            Geolosys.getInstance().LOGGER.error("There was an error in the code for deserialization. Please contact oitsjustjose on GitHub with a log");
            Geolosys.getInstance().LOGGER.error(c.getMessage());
        }
    }

    /**
     * Writes the currentWorldDeposits to GeolosysDeposits.dat
     */
    public static void writeToFile()
    {
        if (DimensionManager.getCurrentSaveRootDirectory() == null)
        {
            return;
        }
        else if (fileLocation == null)
        {
            fileLocation = new File(DimensionManager.getCurrentSaveRootDirectory() + File.separator + "GeolosysDeposits.dat");
        }
        try
        {
            FileOutputStream fileOut = new FileOutputStream(fileLocation);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(GeolosysAPI.getCurrentWorldDeposits());
            out.close();
            fileOut.close();
        }
        catch (IOException i)
        {
            i.printStackTrace();
        }
    }

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


    /**
     * ChunkPosSerializable is a serializable version of Mojang's ChunkPos
     * As such, it stores a chunk's X and Z position
     */
    public static class ChunkPosSerializable implements Serializable
    {
        private int x;
        private int z;

        /**
         * @param pos A Mojang ChunkPos initializer for ChunkPosSerializable
         */
        public ChunkPosSerializable(ChunkPos pos)
        {
            this(pos.x, pos.z);
        }

        /**
         * @param x The X position which the Chunk starts at
         * @param z The Z position which the Chunk starts at
         */
        public ChunkPosSerializable(int x, int z)
        {
            this.x = x;
            this.z = z;
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
         * @return A Mojang ChunkPos variant of this object
         */
        public ChunkPos toChunkPos()
        {
            return new ChunkPos(this.x, this.z);
        }

        @Override
        public String toString()
        {
            return this.toChunkPos().toString();
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
                return c.getX() == this.getX() && c.getZ() == this.getZ();
            }
            return false;
        }
    }
}
