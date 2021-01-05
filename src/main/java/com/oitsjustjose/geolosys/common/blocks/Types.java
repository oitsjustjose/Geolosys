package com.oitsjustjose.geolosys.common.blocks;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.ToolType;

public class Types {
    public static Block.Properties ORE_PROP = Block.Properties.create(Material.ROCK, MaterialColor.STONE)
            .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool();

    public enum Coals implements IStringSerializable {

        PEAT("peat", "peat", "Peat", new PeatBlock().setRegistryName(Constants.MODID, "peat")),
        LIGNITE("lignite", "lignite", "Lignite Coal",
                new Block(ORE_PROP.harvestLevel(1)).setRegistryName(Constants.MODID, "lignite_ore")),
        BITUMINOUS("bituminous_coal", "bituminous_coal", "Bituminous Coal",
                new Block(ORE_PROP.harvestLevel(1)).setRegistryName(Constants.MODID, "bituminous_coal_ore")),
        ANTHRACITE("anthracite", "anthracite", "Anthracitic Coal",
                new Block(ORE_PROP.harvestLevel(2)).setRegistryName(Constants.MODID, "anthracite_ore"));

        private final String serializedName;
        private final String unlocalizedName;
        private final String resource;

        private Block instance;

        Coals(String name, String unlocalizedName, String resource, @Nullable Block instBlk) {
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
            this.resource = resource;
            if (instBlk != null) {
                this.instance = instBlk;
            }
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
        public Block getBlock() {
            return this.instance;
        }

        public void setBlock(Block b) {
            this.instance = b;
        }
    }

    public enum Ores implements IStringSerializable {
        COAL(0, "coal", "coal", "coal", Blocks.COAL_ORE),
        CINNABAR(2, "cinnabar", "redstone", "redstone", Blocks.REDSTONE_ORE), GOLD(2, "gold", "gold", "gold", null),
        LAPIS(1, "lapis", "lapis", "lapis", Blocks.LAPIS_ORE),
        QUARTZ(1, "quartz", "various quartz types", "quartz", Blocks.NETHER_QUARTZ_ORE),
        KIMBERLITE(2, "kimberlite", "diamond", "diamond", Blocks.DIAMOND_ORE),
        BERYL(2, "beryl", "emerald", "emerald", Blocks.EMERALD_ORE),
        NETHER_GOLD(1, "nether_gold", "nether gold", "gold", null),
        ANCIENT_DEBRIS(3, "ancient_debris", "ancient debris", "netherite", null),
        HEMATITE(1, "hematite", "hematite", "iron", null), LIMONITE(2, "limonite", "limonite", "nickel", null),
        MALACHITE(1, "malachite", "malachite", "poor copper", null), AZURITE(2, "azurite", "azurite", "copper", null),
        CASSITERITE(1, "cassiterite", "cassiterite", "poor tin", null),
        TEALLITE(2, "teallite", "teallite", "tin", null), GALENA(2, "galena", "galena", "silver & lead", null),
        BAUXITE(0, "bauxite", "bauxite", "aluminum", null), PLATINUM(2, "platinum", "platinum", "platinum", null),
        AUTUNITE(2, "autunite", "autunite", "uranium", null), SPHALERITE(1, "sphalerite", "sphalerite", "zinc", null);

        private final int toolLevel;
        private final Block parent;
        private final String serializedName;
        private final String unlocalizedName;
        private final String resource;

        private Block instance;
        private Block sample;

        Ores(int toolLevel, String name, String unlocalizedName, String resource, @Nullable Block parent) {
            this.toolLevel = toolLevel;
            this.parent = parent;
            this.serializedName = name;
            this.unlocalizedName = unlocalizedName;
            this.resource = resource;
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
    }
}