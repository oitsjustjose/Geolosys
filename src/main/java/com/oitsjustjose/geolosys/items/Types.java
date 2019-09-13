package com.oitsjustjose.geolosys.items;

import net.minecraft.util.IStringSerializable;

public class Types
{
    public enum Coal implements IStringSerializable
    {
        PEAT(0, 12, "peat"), LIGNITE(1, 16, "lignite"), BITUMINOUS(2, 24, "bituminous"),
        ANTHRACITE(3, 32, "anthracite");

        private static final Coal[] META_LOOKUP = new Coal[values().length];

        static
        {
            for (Coal type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final int burnTime;
        private final String serializedName;
        private final String unlocalizedName;

        Coal(int meta, int burnTime, String name)
        {
            this.meta = meta;
            this.burnTime = burnTime;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static Coal byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public String getName()
        {
            return this.serializedName;
        }

        public int getBurnTime()

        {
            return this.burnTime;
        }

    }

    public enum CoalCoke implements IStringSerializable
    {
        LIGNITE(0, 24, "lignite"), BITUMINOUS(1, 32, "bituminous");

        private static final CoalCoke[] META_LOOKUP = new CoalCoke[values().length];

        static
        {
            for (CoalCoke type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final int burnTime;
        private final String serializedName;
        private final String unlocalizedName;

        CoalCoke(int meta, int burnTime, String name)
        {
            this.meta = meta;
            this.burnTime = burnTime;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static CoalCoke byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public String getName()
        {
            return this.serializedName;
        }

        public int getBurnTime()

        {
            return this.burnTime;
        }

    }

    public enum Ingot implements IStringSerializable
    {
        COPPER(0, "copper"), TIN(1, "tin"), SILVER(2, "silver"), LEAD(3, "lead"), ALUMINUM(4, "aluminum"),
        NICKEL(5, "nickel"), PLATINUM(6, "platinum"), ZINC(7, "zinc");

        private static final Ingot[] META_LOOKUP = new Ingot[values().length];

        static
        {
            for (Ingot type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        Ingot(int meta, String name)
        {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static Ingot byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public String getName()
        {
            return this.serializedName;
        }
    }

    public enum Cluster implements IStringSerializable
    {
        IRON(0, "iron"), GOLD(1, "gold"), COPPER(2, "copper"), TIN(3, "tin"), SILVER(4, "silver"), LEAD(5, "lead"),
        ALUMINUM(6, "aluminum"), NICKEL(7, "nickel"), PLATINUM(8, "platinum"), URANIUM(9, "uranium"), ZINC(10, "zinc"),
        YELLORIUM(11, "yellorium"), OSMIUM(12, "osmium");

        private static final Cluster[] META_LOOKUP = new Cluster[values().length];

        static
        {
            for (Cluster type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        Cluster(int meta, String name)
        {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static Cluster byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public String getName()
        {
            return this.serializedName;
        }
    }
}
