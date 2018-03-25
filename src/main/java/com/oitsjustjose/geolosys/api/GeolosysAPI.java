package com.oitsjustjose.geolosys.api;

import net.minecraft.util.math.ChunkPos;

import java.io.Serializable;
import java.util.HashMap;

public class GeolosysAPI
{
    public static HashMap<ChunkPosSerializable, String> currentWorldDeposits = new HashMap<>();

    public static class ChunkPosSerializable implements Serializable
    {
        private int x;
        private int z;

        public ChunkPosSerializable(ChunkPos pos)
        {
            this(pos.x, pos.z);
        }

        public ChunkPosSerializable(int x, int z)
        {
            this.x = x;
            this.z = z;
        }

        public int getX()
        {
            return this.x;
        }

        public int getZ()
        {
            return this.z;
        }

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
