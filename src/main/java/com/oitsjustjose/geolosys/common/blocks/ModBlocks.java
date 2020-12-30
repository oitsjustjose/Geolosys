package com.oitsjustjose.geolosys.common.blocks;

import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;

import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {
    private static ModBlocks instance;

    public Block peat;

    private ModBlocks() {
        for (Types.Ores oreType : Types.Ores.values()) {
            final String SAMPLE_REGISTRY_NAME = oreType.getName().toLowerCase() + "_ore_sample";
            final String ORE_REGISTRY_NAME = oreType.getName().toLowerCase() + "_ore";
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(oreType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE).setRequiresTool();

            if (oreType.getVanillaParent() != null) {
                blockProp.lootFrom(oreType.getVanillaParent());
            }

            Block block = new OreBlock(oreType.getVanillaParent(), blockProp).setRegistryName(Constants.MODID,
                    ORE_REGISTRY_NAME);
            Block sample = oreType.getVanillaParent() == null
                    ? sample = new SampleBlock(block).setRegistryName(Constants.MODID, SAMPLE_REGISTRY_NAME)
                    : new SampleBlock().setRegistryName(Constants.MODID, SAMPLE_REGISTRY_NAME);

            oreType.setBlock(block);
            oreType.setSample(sample);
        }

        peat = new Block(Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(4F, 3F)
                .sound(SoundType.SOUL_SOIL).harvestTool(ToolType.SHOVEL)).setRegistryName(Constants.MODID, "peat");
    }

    public static ModBlocks getInstance() {
        if (instance == null) {
            instance = new ModBlocks();
        }
        return instance;
    }

    public void register(RegistryEvent.Register<Block> blockRegistryEvent) {
        for (Types.Ores oreType : Types.Ores.values()) {
            blockRegistryEvent.getRegistry().register(oreType.getBlock());
            blockRegistryEvent.getRegistry().register(oreType.getSample());
        }
        blockRegistryEvent.getRegistry().register(peat);
    }

    public void registerItemBlocks(RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Types.Ores oreType : Types.Ores.values()) {
            Item iBlock = new BlockItem(oreType.getBlock(), new Item.Properties().group(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getBlock().getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }

        for (Types.Ores oreType : Types.Ores.values()) {
            Item iBlock = new BlockItem(oreType.getSample(), new Item.Properties().group(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getSample().getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }

        Item iBlock = new BlockItem(peat, new Item.Properties().group(GeolosysGroup.getInstance()))
                .setRegistryName(peat.getRegistryName());
        itemRegistryEvent.getRegistry().register(iBlock);
    }
}