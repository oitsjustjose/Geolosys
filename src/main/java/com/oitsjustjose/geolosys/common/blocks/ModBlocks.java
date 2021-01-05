package com.oitsjustjose.geolosys.common.blocks;

import java.util.ArrayList;

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
    private ArrayList<Block> extras = new ArrayList<Block>();

    public Block peat = new PeatBlock().setRegistryName(Constants.MODID, "peat");
    public Block rhododendron = new PlantBlock(false, peat).setRegistryName(Constants.MODID, "rhododendron");

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

            if (oreType.hasSample()) {
                Block sample = oreType.getVanillaParent() == null
                        ? sample = new SampleBlock(block).setRegistryName(Constants.MODID, SAMPLE_REGISTRY_NAME)
                        : new SampleBlock().setRegistryName(Constants.MODID, SAMPLE_REGISTRY_NAME);
                oreType.setSample(sample);
            }

            oreType.setBlock(block);
        }

        extras.add(peat);
        extras.add(rhododendron);
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
            if (oreType.hasSample()) {
                blockRegistryEvent.getRegistry().register(oreType.getSample());
            }
        }
        for (Block extra : extras) {
            blockRegistryEvent.getRegistry().register(extra);
        }
    }

    public void registerItemBlocks(RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Types.Ores oreType : Types.Ores.values()) {
            Item iBlock = new BlockItem(oreType.getBlock(), new Item.Properties().group(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getBlock().getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }

        for (Types.Ores oreType : Types.Ores.values()) {
            if (oreType.hasSample()) {
                Item iBlock = new BlockItem(oreType.getSample(),
                        new Item.Properties().group(GeolosysGroup.getInstance()))
                                .setRegistryName(oreType.getSample().getRegistryName());
                itemRegistryEvent.getRegistry().register(iBlock);
            }
        }

        for (Block extra : extras) {
            Item iBlock = new BlockItem(extra, new Item.Properties().group(GeolosysGroup.getInstance()))
                    .setRegistryName(extra.getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }
    }
}