package com.oitsjustjose.geolosys.common.blocks;

import com.google.common.collect.Maps;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;

import java.util.HashMap;

public class BlockInit
{
    private static BlockInit instance;

    private HashMap<String, Block> blocks;

    private BlockInit()
    {
        blocks = Maps.newHashMap();

        for (Types.Vanilla vanillaType : Types.Vanilla.values())
        {
            // Use the vanilla blocks if possible, otherwise I'll build it myself.
            Properties blockProp;
            if (vanillaType.getVanillaParent() != null)
            {
                blockProp = Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(7.5F, 10F)
                        .sound(SoundType.STONE).harvestLevel(vanillaType.getToolLevel()).harvestTool(ToolType.PICKAXE)
                        .lootFrom(vanillaType.getVanillaParent());
            }
            else
            {
                blockProp = Properties.create(Material.ROCK, MaterialColor.STONE).hardnessAndResistance(7.5F, 10F)
                        .sound(SoundType.STONE).harvestLevel(vanillaType.getToolLevel()).harvestTool(ToolType.PICKAXE);
            }

            Block block = new OreBlock(vanillaType.getVanillaParent(), blockProp).setRegistryName(Constants.MODID,
                    vanillaType.getName() + "_ore");
            blocks.put(block.getRegistryName().toString(), block);

            Block sample = new SampleBlock(block).setRegistryName(Constants.MODID,
                    vanillaType.getName() + "_ore_sample");
            blocks.put(sample.getRegistryName().toString(), sample);
        }

        for (Types.Modded moddedType : Types.Modded.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(moddedType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName(Constants.MODID, moddedType.getName() + "_ore");
            blocks.put(block.getRegistryName().toString(), block);

            Block sample = new SampleBlock(block).setRegistryName(Constants.MODID,
                    moddedType.getName() + "_ore_sample");
            blocks.put(sample.getRegistryName().toString(), sample);
        }

    }

    public static BlockInit getInstance()
    {
        if (instance == null)
        {
            instance = new BlockInit();
        }
        return instance;
    }

    public void registerBlocks(RegistryEvent.Register<Block> blockRegistryEvent)
    {
        for (Block b : this.getModBlocks().values())
        {
            blockRegistryEvent.getRegistry().register(b);
        }
    }

    public void registerBlockItems(RegistryEvent.Register<Item> itemRegistryEvent)
    {
        for (Block b : this.getModBlocks().values())
        {
            Item iBlock = new BlockItem(b, new Item.Properties().group(GeolosysGroup.getInstance()))
                    .setRegistryName(b.getRegistryName());
            itemRegistryEvent.getRegistry().register(iBlock);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Block> getModBlocks()
    {
        return (HashMap<String, Block>) this.blocks.clone();
    }
}