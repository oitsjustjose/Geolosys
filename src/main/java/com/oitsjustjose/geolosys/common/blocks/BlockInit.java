package com.oitsjustjose.geolosys.common.blocks;

import java.util.HashMap;

import com.oitsjustjose.geolosys.common.utils.GeolosysItemGroup;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;

public class BlockInit
{
    private static BlockInit instance;

    private HashMap<String, Block> blocks;

    private BlockInit()
    {
        blocks = new HashMap<String, Block>();

        for (Types.Vanilla vanillaType : Types.Vanilla.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(vanillaType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName("geolosys", vanillaType.getName() + "_ore");
            blocks.put(block.getRegistryName().toString(), block);
        }

        for (Types.Modded moddedType : Types.Modded.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(moddedType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName("geolosys", moddedType.getName() + "_ore");
            blocks.put(block.getRegistryName().toString(), block);
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
            Item iBlock = new BlockItem(b, new Item.Properties().group(GeolosysItemGroup.getInstance()))
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