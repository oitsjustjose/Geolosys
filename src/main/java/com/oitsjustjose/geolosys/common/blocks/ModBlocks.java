package com.oitsjustjose.geolosys.common.blocks;

import java.util.ArrayList;

import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {
    private static ModBlocks instance;
    private ArrayList<Block> extras = new ArrayList<Block>();

    public Block peat = new PeatBlock().setRegistryName(Constants.MODID, "peat");
    public Block rhododendron = new PlantBlock(false, peat).setRegistryName(Constants.MODID, "rhododendron");

    private ModBlocks() {
        for (Types.Ores oreType : Types.Ores.values()) {
            final String SAMPLE_REGISTRY_NAME = oreType.getUnlocalizedName().toLowerCase() + "_ore_sample";
            final String ORE_REGISTRY_NAME = oreType.getUnlocalizedName().toLowerCase() + "_ore";
            BlockBehaviour.Properties blockProp = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.STONE)
                    .strength(5.0F, 10F).sound(SoundType.STONE).requiresCorrectToolForDrops();

            Block block = new OreBlock(blockProp, oreType.getXp()).setRegistryName(Constants.MODID, ORE_REGISTRY_NAME);
            Block sample = new SampleBlock().setRegistryName(Constants.MODID, SAMPLE_REGISTRY_NAME);

            oreType.setSample(sample);
            oreType.setBlock(block);
        }

        for (Types.DeepslateOres oreType : Types.DeepslateOres.values()) {
            // No sample for deepslate ores - they use the standard stone sample.
            final String ORE_REGISTRY_NAME = oreType.getUnlocalizedName().toLowerCase() + "_ore";
            BlockBehaviour.Properties blockProp = BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DEEPSLATE)
                    .strength(7.5F, 10F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops();

            Block block = new OreBlock(blockProp, oreType.getXp()).setRegistryName(Constants.MODID, ORE_REGISTRY_NAME);
            oreType.setBlock(block);
            oreType.setSample(oreType.getOrigin().getSample());
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
            blockRegistryEvent.getRegistry().register(oreType.getSample());
        }

        for (Types.DeepslateOres oreType : Types.DeepslateOres.values()) {
            blockRegistryEvent.getRegistry().register(oreType.getBlock());
        }

        for (Block extra : extras) {
            blockRegistryEvent.getRegistry().register(extra);
        }
    }

    public void registerItemBlocks(RegistryEvent.Register<Item> itemRegistryEvent) {
        // Standard Ores
        for (Types.Ores oreType : Types.Ores.values()) {
            Item iBlock = new BlockItem(oreType.getBlock(), new Item.Properties().tab(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getBlock().getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }

        // Standard Samples
        for (Types.Ores oreType : Types.Ores.values()) {
            Item iBlock = new BlockItem(oreType.getSample(), new Item.Properties().tab(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getSample().getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }

        // Deepslate Ores (Use standard samples, so no sample is registered)
        for (Types.DeepslateOres oreType : Types.DeepslateOres.values()) {
            Item iBlock = new BlockItem(oreType.getBlock(), new Item.Properties().tab(GeolosysGroup.getInstance()))
                    .setRegistryName(oreType.getBlock().getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }

        // Misc (peat, rhodo, etc.)
        for (Block extra : extras) {
            Item iBlock = new BlockItem(extra, new Item.Properties().tab(GeolosysGroup.getInstance()))
                    .setRegistryName(extra.getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }
    }
}
