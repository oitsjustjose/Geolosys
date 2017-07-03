package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Lib;
import com.oitsjustjose.geolosys.blocks.BlockOre;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlock
{

	public ItemBlockOre(Block block)
	{
		super(block);
		this.setHasSubtypes(true);
		this.setRegistryName(Lib.MODID, "ore");
		this.setMaxDamage(0);
	}
	
	@Override
    public int getMetadata(int damage)
    {
        return damage;
    }

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return BlockOre.EnumType.byMetadata(stack.getMetadata()).getName();
	}
}
