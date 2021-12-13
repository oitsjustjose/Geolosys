package com.oitsjustjose.geolosys.common.blocks;

import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;

public class Types {
    public enum Ores {
        LIGNITE_COAL("lignite", 2),
        BITUMINOUS_COAL("bituminous_coal", 2),
        ANTHRACITE_COAL("anthracite_coal", 2),
        COAL("coal", 2),
        CINNABAR("cinnabar", 0),
        GOLD("gold", 0),
        LAPIS("lapis", 5),
        QUARTZ("quartz", 5),
        KIMBERLITE("kimberlite", 7),
        BERYL("beryl", 7),
        NETHER_GOLD("nether_gold", 1),
        ANCIENT_DEBRIS("ancient_debris", 0),
        HEMATITE("hematite", 0),
        LIMONITE("limonite", 0),
        MALACHITE("malachite", 0),
        AZURITE("azurite", 0),
        CASSITERITE("cassiterite", 0),
        TEALLITE("teallite", 0),
        GALENA("galena", 0),
        BAUXITE("bauxite", 0),
        PLATINUM("platinum", 0),
        AUTUNITE("autunite", 0),
        SPHALERITE("sphalerite", 0);

        private final String unlocalizedName;
        private final int xp;

        private Block instance;
        private Block sample;

        Ores(String unlocalizedName, int xp) {
            this.unlocalizedName = unlocalizedName;
            this.xp = xp;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
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

    public enum DeepslateOres {
        LIGNITE_COAL("deepslate_lignite", Ores.LIGNITE_COAL, 2),
        BITUMINOUS_COAL("deepslate_bituminous_coal", Ores.BITUMINOUS_COAL, 2),
        ANTHRACITE_COAL("deepslate_anthracite_coal", Ores.ANTHRACITE_COAL, 2),
        COAL("deepslate_coal", Ores.COAL, 2),
        CINNABAR("deepslate_cinnabar", Ores.CINNABAR, 0),
        GOLD("deepslate_gold", Ores.GOLD, 0),
        LAPIS("deepslate_lapis", Ores.LAPIS, 5),
        QUARTZ("deepslate_quartz", Ores.QUARTZ, 5),
        KIMBERLITE("deepslate_kimberlite", Ores.KIMBERLITE, 7),
        BERYL("deepslate_beryl", Ores.BERYL, 7),
        HEMATITE("deepslate_hematite", Ores.HEMATITE, 0),
        LIMONITE("deepslate_limonite", Ores.LIMONITE, 0),
        MALACHITE("deepslate_malachite", Ores.MALACHITE, 0),
        AZURITE("deepslate_azurite", Ores.AZURITE, 0),
        CASSITERITE("deepslate_cassiterite", Ores.CASSITERITE, 0),
        TEALLITE("deepslate_teallite", Ores.TEALLITE, 0),
        GALENA("deepslate_galena", Ores.GALENA, 0),
        BAUXITE("deepslate_bauxite", Ores.BAUXITE, 0),
        PLATINUM("deepslate_platinum", Ores.PLATINUM, 0),
        AUTUNITE("deepslate_autunite", Ores.AUTUNITE, 0),
        DEEPSLATE_SPHALERITE("deepslate_sphalerite", Ores.SPHALERITE, 0);

        private final String unlocalizedName;
        private final int xp;

        private Block instance;
        private Block sample;
        private Ores origin;

        DeepslateOres(String unlocalizedName, Ores origin, int xp) {
            this.unlocalizedName = unlocalizedName;
            this.origin = origin;
            this.xp = xp;
        }

        public String toString() {
            return this.unlocalizedName;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public int getXp() {
            return this.xp;
        }

        @Nullable
        public Block getBlock() {
            return this.instance;
        }

        @Nullable
        public Ores getOrigin() {
            return this.origin;
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
