package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class Types {
    public enum Ores  {
        LIGNITE_COAL(1, "lignite", "lignite", "Lignite Coal", 2),
        BITUMINOUS_COAL(1, "bituminous_coal", "bituminous_coal", "Bituminous Coal", 2),
        ANTHRACITE_COAL(2, "anthracite_coal", "anthracite_coal", "Anthracitic Coal", 2),
        COAL(0, "coal", "coal", "coal", 2), CINNABAR(2, "cinnabar", "redstone", "redstone", 0),
        GOLD(2, "gold", "gold", "gold", 0), LAPIS(1, "lapis", "lapis", "lapis", 5),
        QUARTZ(1, "quartz", "various quartz types", "quartz", 5), KIMBERLITE(2, "kimberlite", "diamond", "diamond", 7),
        BERYL(2, "beryl", "emerald", "emerald", 7), NETHER_GOLD(1, "nether_gold", "nether gold", "gold", 1),
        ANCIENT_DEBRIS(3, "ancient_debris", "ancient debris", "netherite", 0),
        HEMATITE(1, "hematite", "hematite", "iron", 0), LIMONITE(2, "limonite", "limonite", "nickel", 0),
        MALACHITE(1, "malachite", "malachite", "poor copper", 0), AZURITE(2, "azurite", "azurite", "copper", 0),
        CASSITERITE(1, "cassiterite", "cassiterite", "poor tin", 0), TEALLITE(2, "teallite", "teallite", "tin", 0),
        GALENA(2, "galena", "galena", "silver & lead", 0), BAUXITE(0, "bauxite", "bauxite", "aluminum", 0),
        PLATINUM(2, "platinum", "platinum", "platinum", 0), AUTUNITE(2, "autunite", "autunite", "uranium", 0),
        SPHALERITE(1, "sphalerite", "sphalerite", "zinc", 0);

        private final int toolLevel;
        private final String serializedName;
        private final String unlocalizedName;
        private final String resource;
        private final int xp;

        private Block instance;
        private Block sample;

        Ores(int toolLevel, String name, String unlocalizedName, String resource, int xp) {
            this.toolLevel = toolLevel;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
            this.resource = resource;
            this.xp = xp;
        }

        public int getToolLevel() {
            return this.toolLevel;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getResource() {
            return this.resource;
        }

        public String getName() {
            return this.serializedName;
        }

        public String getString() {
            return this.serializedName;
        }

        public int getXp() {
            return this.xp;
        }

        @Nullable
        public Block getBlock() {
            return this.instance;
        }

        @Nullable
        public Block getSample() {
            return this.sample;
        }

        public void setBlock(Block b) {
            this.instance = b;
        }

        public void setSample(Block b) {
            this.sample = b;
        }
    }
}
