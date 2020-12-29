package com.oitsjustjose.geolosys.common.items;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class Types {
    public enum Coals implements IStringSerializable {
        PEAT(0, 12, "peat"), LIGNITE(1, 16, "lignite"), BITUMINOUS(2, 24, "bituminous"),
        ANTHRACITE(3, 32, "anthracite");

        private static final Coals[] META_LOOKUP = new Coals[values().length];

        static {
            for (Coals type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private Item instance;

        private final int meta;
        private final int burnTime;
        private final String serializedName;
        private final String unlocalizedName;

        Coals(int meta, int burnTime, String name) {
            this.meta = meta;
            this.burnTime = burnTime;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static Coals byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getName() {
            return this.serializedName;
        }

        public int getBurnTime() {
            return this.burnTime;
        }

        public String getString() {
            return this.serializedName;
        }

        public void setItem(Item i) {
            this.instance = i;
        }

        public Item getItem() {
            return this.instance;
        }
    }

    public enum CoalCokes implements IStringSerializable {
        LIGNITE(0, 24, "lignite"), BITUMINOUS(1, 32, "bituminous");

        private static final CoalCokes[] META_LOOKUP = new CoalCokes[values().length];

        static {
            for (CoalCokes type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }

        private Item instance;

        private final int meta;
        private final int burnTime;
        private final String serializedName;
        private final String unlocalizedName;

        CoalCokes(int meta, int burnTime, String name) {
            this.meta = meta;
            this.burnTime = burnTime;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static CoalCokes byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getName() {
            return this.serializedName;
        }

        public int getBurnTime() {
            return this.burnTime;
        }

        public String getString() {
            return this.serializedName;
        }

        public void setItem(Item i) {
            this.instance = i;
        }

        public Item getItem() {
            return this.instance;
        }
    }

    public enum Ingots implements IStringSerializable {
        COPPER(0, "copper"), TIN(1, "tin"), SILVER(2, "silver"), LEAD(3, "lead"), ALUMINUM(4, "aluminum"),
        NICKEL(5, "nickel"), PLATINUM(6, "platinum"), ZINC(7, "zinc");

        private static final Ingots[] META_LOOKUP = new Ingots[values().length];

        static {
            for (Ingots type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
        private Item instance;

        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        Ingots(int meta, String name) {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static Ingots byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getName() {
            return this.serializedName;
        }

        public String getString() {
            return this.serializedName;
        }

        public void setItem(Item i) {
            this.instance = i;
        }

        public Item getItem() {
            return this.instance;
        }
    }

    public enum Clusters implements IStringSerializable {
        IRON(0, "iron"), GOLD(1, "gold"), COPPER(2, "copper"), TIN(3, "tin"), SILVER(4, "silver"), LEAD(5, "lead"),
        ALUMINUM(6, "aluminum"), NICKEL(7, "nickel"), PLATINUM(8, "platinum"), URANIUM(9, "uranium"), ZINC(10, "zinc"),
        YELLORIUM(11, "yellorium"), OSMIUM(12, "osmium"), ANCIENT_DEBRIS(13, "ancient_debris"),
        NETHER_GOLD(14, "nether_gold");

        private static final Clusters[] META_LOOKUP = new Clusters[values().length];

        static {
            for (Clusters type : values()) {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
        private Item instance;

        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        Clusters(int meta, String name) {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public static Clusters byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getName() {
            return this.serializedName;
        }

        public String getString() {
            return this.serializedName;
        }

        public void setItem(Item i) {
            this.instance = i;
        }

        public Item getItem() {
            return this.instance;
        }
    }
}
