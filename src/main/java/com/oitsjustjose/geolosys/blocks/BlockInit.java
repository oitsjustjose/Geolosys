package com.oitsjustjose.geolosys.blocks;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class BlockInit
{
    private static BlockInit instance;

    private ArrayList<Block> blocks;

    private BlockInit()
    {
        blocks = new ArrayList<Block>();

        for (Types.Vanilla vanillaType : Types.Vanilla.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(vanillaType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName("geolosys", vanillaType.getName() + "_ore");
            blocks.add(block);
        }

        for (Types.Modded moddedType : Types.Modded.values())
        {
            Properties blockProp = Properties.create(Material.ROCK, MaterialColor.STONE)
                    .hardnessAndResistance(7.5F, 10F).sound(SoundType.STONE).harvestLevel(moddedType.getToolLevel())
                    .harvestTool(ToolType.PICKAXE);
            Block block = new Block(blockProp).setRegistryName("geolosys", moddedType.getName() + "_ore");
            blocks.add(block);
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

    @SuppressWarnings("unchecked")
    public ArrayList<Block> getModBlocks()
    {
        return (ArrayList<Block>) this.blocks.clone();
    }
}