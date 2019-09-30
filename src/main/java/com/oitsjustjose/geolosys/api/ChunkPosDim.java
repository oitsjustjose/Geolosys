package com.oitsjustjose.geolosys.api;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.io.Serializable;

/**
 * ChunkPosSerializable is a serializable version of Mojang's ChunkPos As such, it stores a chunk's X and Z position
 */
public class ChunkPosDim implements Serializable
{
    private static final long serialVersionUID = 6006452707959877895L;
    private int x;
    private int z;
    private String dim;

    /**
     * @param pos A Mojang ChunkPos initializer for ChunkPosSerializable
     */
    public ChunkPosDim(ChunkPos pos, String dim)
    {
        this(pos.x, pos.z, dim);
    }

    /**
     * @param pos A Mojang ChunkPos initializer for ChunkPosSerializable
     */
    public ChunkPosDim(BlockPos pos, String dim)
    {
        this(new ChunkPos(pos), dim);
    }

    /**
     * @param x The X position which the Chunk starts at
     * @param z The Z position which the Chunk starts at
     */
    public ChunkPosDim(int x, int z, String dim)
    {
        this.x = x;
        this.z = z;
        this.dim = dim;
    }

    public ChunkPosDim(String asString)
    {
        String[] parts = asString.replace("[", "").replace("]", "").split(",");
        assert parts.length == 3;
        this.x = Integer.parseInt(parts[0]);
        this.z = Integer.parseInt(parts[1]);
        this.dim = parts[2];
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
    public String getDimension()
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
        else if (other instanceof ChunkPosDim)
        {
            ChunkPosDim c = (ChunkPosDim) other;
            return c.getX() == this.getX() && c.getZ() == this.getZ()
                    && c.getDimension().equalsIgnoreCase(this.getDimension());
        }
        return false;
    }
}