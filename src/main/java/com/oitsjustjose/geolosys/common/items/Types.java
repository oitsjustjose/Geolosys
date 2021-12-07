package com.oitsjustjose.geolosys.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class Types {
    public enum Coals {
        PEAT(6, "peat", false), LIGNITE(12, "lignite", false), BITUMINOUS(16, "bituminous", false),
        ANTHRACITE(20, "anthracite", false), LIGNITE_COKE(24, "lignite", true), BITUMINOUS_COKE(32, "bituminous", true);

        private Item instance;

        private final int burnTime;
        private final String serializedName;
        private final String unlocalizedName;
        private final boolean isCoalCoke;

        Coals(int burnTime, String name, boolean isCoalCoke) {
            this.burnTime = burnTime;
            this.serializedName = name;
            this.unlocalizedName = name;
            this.isCoalCoke = isCoalCoke;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getName() {
            return this.serializedName;
        }

        public int getBurnTime() {
            return this.burnTime * 200;
        }

        public String getString() {
            return this.serializedName;
        }

        public boolean isCoalCoke() {
            return this.isCoalCoke;
        }

        public void setItem(Item i) {
            this.instance = i;
        }

        public Item getItem() {
            return this.instance;
        }
    }

    public enum Ingots {
        TIN("tin"), SILVER("silver"), LEAD("lead"), ALUMINUM("aluminum"), NICKEL("nickel"),
        PLATINUM("platinum"), ZINC("zinc");

        private Item instance;

        private final String serializedName;
        private final String unlocalizedName;

        Ingots(String name) {
            this.serializedName = name;
            this.unlocalizedName = name;
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

    public enum Nuggets {
        COPPER("copper"), TIN("tin"), SILVER("silver"), LEAD("lead"), ALUMINUM("aluminum"), NICKEL("nickel"),
        PLATINUM("platinum"), ZINC("zinc");

        private Item instance;

        private final String serializedName;
        private final String unlocalizedName;

        Nuggets(String name) {
            this.serializedName = name;
            this.unlocalizedName = name;
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

    public enum Clusters {
        IRON("iron"), GOLD("gold"), COPPER("copper"), TIN("tin"), SILVER("silver"), LEAD("lead"), ALUMINUM("aluminum"),
        NICKEL("nickel"), PLATINUM("platinum"), URANIUM("uranium"), ZINC("zinc"), YELLORIUM("yellorium"),
        OSMIUM("osmium"), ANCIENT_DEBRIS("ancient_debris"), NETHER_GOLD("nether_gold");

        private Item instance;

        private final String serializedName;
        private final String unlocalizedName;

        Clusters(String name) {
            this.serializedName = name;
            this.unlocalizedName = name;
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
