package com.oitsjustjose.geolosys.common.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.IStringSerializable;

public class Types {
    public enum Ores implements IStringSerializable {
        LIGNITE_COAL(1, "lignite", "lignite", "Lignite Coal", false, null),
        BITUMINOUS_COAL(1, "bituminous_coal", "bituminous_coal", "Bituminous Coal", false, null),
        ANTHRACITE_COAL(2, "anthracite_coal", "anthracite_coal", "Anthracitic Coal", false, null),
        COAL(0, "coal", "coal", "coal", true, Blocks.COAL_ORE),
        CINNABAR(2, "cinnabar", "redstone", "redstone", true, Blocks.REDSTONE_ORE),
        GOLD(2, "gold", "gold", "gold", true, null), LAPIS(1, "lapis", "lapis", "lapis", true, Blocks.LAPIS_ORE),
        QUARTZ(1, "quartz", "various quartz types", "quartz", true, Blocks.NETHER_QUARTZ_ORE),
        KIMBERLITE(2, "kimberlite", "diamond", "diamond", true, Blocks.DIAMOND_ORE),
        BERYL(2, "beryl", "emerald", "emerald", true, Blocks.EMERALD_ORE),
        NETHER_GOLD(1, "nether_gold", "nether gold", "gold", true, null),
        ANCIENT_DEBRIS(3, "ancient_debris", "ancient debris", "netherite", true, null),
        HEMATITE(1, "hematite", "hematite", "iron", true, null),
        LIMONITE(2, "limonite", "limonite", "nickel", true, null),
        MALACHITE(1, "malachite", "malachite", "poor copper", true, null),
        AZURITE(2, "azurite", "azurite", "copper", true, null),
        CASSITERITE(1, "cassiterite", "cassiterite", "poor tin", true, null),
        TEALLITE(2, "teallite", "teallite", "tin", true, null),
        GALENA(2, "galena", "galena", "silver & lead", true, null),
        BAUXITE(0, "bauxite", "bauxite", "aluminum", true, null),
        PLATINUM(2, "platinum", "platinum", "platinum", true, null),
        AUTUNITE(2, "autunite", "autunite", "uranium", true, null),
        SPHALERITE(1, "sphalerite", "sphalerite", "zinc", true, null);

        private final int toolLevel;
        private final Block parent;
        private final String serializedName;
        private final String unlocalizedName;
        private final String resource;
        private final boolean hasSample;

        private Block instance;
        private Block sample;

        Ores(int toolLevel, String name, String unlocalizedName, String resource, boolean hasSample,
                @Nullable Block parent) {
            this.toolLevel = toolLevel;
            this.parent = parent;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
            this.resource = resource;
            this.hasSample = hasSample;
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

        @Nullable
        public Block getVanillaParent() {
            return this.parent;
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

        public boolean hasSample() {
            return this.hasSample;
        }
    }
}