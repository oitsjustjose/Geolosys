package com.oitsjustjose.geolosys.common.blocks;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.oitsjustjose.geolosys.Geolosys;
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

    private ModBlocks() {
        for (Types.Ores oreType : Types.Ores.values()) {
            Properties blockProp;
            Geolosys.getInstance().LOGGER.info(oreType.name());
            if (oreType.getVanillaParent() != null) {
                blockProp = Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(7.5F, 10F)
                        .sound(SoundType.STONE).harvestLevel(oreType.getToolLevel()).harvestTool(ToolType.PICKAXE)
                        .lootFrom(oreType.getVanillaParent()).setRequiresTool();
            } else {
                blockProp = Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(7.5F, 10F)
                        .sound(SoundType.STONE).harvestLevel(oreType.getToolLevel()).harvestTool(ToolType.PICKAXE)
                        .setRequiresTool();
            }

            Block block = new OreBlock(oreType.getVanillaParent(), blockProp).setRegistryName(Constants.MODID,
                    oreType.getName() + "_ore");

            Block sample;
            // We don't want samples to adopt vanilla drops
            if (oreType.getVanillaParent() != null) {
                sample = new SampleBlock().setRegistryName(Constants.MODID, oreType.getName() + "_ore_sample");
            } else {
                sample = new SampleBlock(block).setRegistryName(Constants.MODID, oreType.getName() + "_ore_sample");
            }

            oreType.setBlock(block);
            oreType.setSample(sample);
        }
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
    }

    public void registerIb(RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Types.Ores oreType : Types.Ores.values()) {
            // Register the ore
            Item oreItem = new BlockItem(oreType.getBlock(), new Item.Properties().group(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getBlock().getRegistryName());
            itemRegistryEvent.getRegistry().register(oreItem);

            // Register the sample
            Item sampleItem = new BlockItem(oreType.getSample(),
                    new Item.Properties().group(GeolosysGroup.getInstance()))
                            .setRegistryName(oreType.getSample().getRegistryName());
            itemRegistryEvent.getRegistry().register(sampleItem);
        }
    }
}