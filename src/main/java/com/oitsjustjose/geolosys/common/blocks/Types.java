package com.oitsjustjose.geolosys.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.IStringSerializable;

public class Types
{
    public enum Vanilla implements IStringSerializable
    {
        COAL(0, 0, "coal", "coal", Blocks.COAL_ORE), CINNABAR(1, 2, "cinnabar", "redstone", Blocks.REDSTONE_ORE),
        GOLD(2, 2, "gold", "gold", null), LAPIS(3, 1, "lapis", "lapis", Blocks.LAPIS_ORE),
        QUARTZ(4, 1, "quartz", "various quartz types", Blocks.NETHER_QUARTZ_ORE),
        KIMBERLITE(5, 2, "kimberlite", "diamond", Blocks.DIAMOND_ORE),
        BERYL(6, 2, "beryl", "emerald", Blocks.EMERALD_ORE);

        private static final Vanilla[] META_LOOKUP = new Vanilla[values().length];

        static
        {
            for (Vanilla type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final int toolLevel;
        private final String unlocalizedName;
        private final String resource;
        private final Block parentBlock;

        Vanilla(int meta, int toolLevel, String name, String resource, @Nullable Block parent)
        {
            this.meta = meta;
            this.toolLevel = toolLevel;
            this.unlocalizedName = name;
            this.resource = resource;
            this.parentBlock = parent;
        }

        public static Vanilla byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public Block getVanillaParent()
        {
            return this.parentBlock;
        }

        public int getToolLevel()
        {
            return this.toolLevel;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public String getResource()
        {
            return this.resource;
        }

        public String getName()
        {
            return this.unlocalizedName;
        }
    }

    public enum Modded implements IStringSerializable
    {
        HEMATITE(0, 1, "hematite", "hematite", "iron"), LIMONITE(1, 2, "limonite", "limonite", "nickel"),
        MALACHITE(2, 1, "malachite", "malachite", "poor copper"), AZURITE(3, 2, "azurite", "azurite", "copper"),
        CASSITERITE(4, 1, "cassiterite", "cassiterite", "poor tin"), TEALLITE(5, 2, "teallite", "teallite", "tin"),
        GALENA(6, 2, "galena", "galena", "silver & lead"), BAUXITE(7, 0, "bauxite", "bauxite", "aluminum"),
        PLATINUM(8, 2, "platinum", "platinum", "platinum"), AUTUNITE(9, 2, "autunite", "autunite", "uranium"),
        SPHALERITE(10, 1, "sphalerite", "sphalerite", "zinc");

        private static final Modded[] META_LOOKUP = new Modded[values().length];

        static
        {
            for (Modded type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private final int meta;
        private final int toolLevel;
        private final String serializedName;
        private final String unlocalizedName;
        private final String resource;

        Modded(int meta, int toolLevel, String name, String unlocalizedName, String resource)
        {
            this.meta = meta;
            this.toolLevel = toolLevel;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
            // this.serializedName = (meta == 0 && ModConfig.compat.vanillaMode) ? name + "_vanilla" : name;
            // this.unlocalizedName = (meta == 0 && ModConfig.compat.vanillaMode) ? unlocalizedName + "_vanilla"
            // : unlocalizedName;
            this.resource = resource;

        }

        public static Modded byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getToolLevel()
        {
            return this.toolLevel;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public String getResource()
        {
            return this.resource;
        }

        public String getName()
        {
            return this.serializedName;
        }
    }
}