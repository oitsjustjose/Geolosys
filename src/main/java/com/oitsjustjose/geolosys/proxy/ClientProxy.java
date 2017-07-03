package com.oitsjustjose.geolosys.proxy;

import com.oitsjustjose.geolosys.Lib;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy
{
	String MODID = Lib.MODID;

	/**
	 * @param item
	 *            The Item to register a model registry for. You still have to make the model file, but now MC will know where to look
	 */
	@SideOnly(Side.CLIENT)
	public void register(Item item)
	{
		NonNullList<ItemStack> subItems = NonNullList.create();
		item.getSubItems(item.getCreativeTab(), subItems);
		System.out.println(subItems);
		for (ItemStack sub : subItems)
		{
			ModelBakery.registerItemVariants(item, item.getRegistryName());
//			System.out.println(item);
//			System.out.println(sub);
//			System.out.println(item.getRegistryName());
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, sub.getItemDamage(), new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}

	/**
	 * @param block
	 *            The Item to register a model registry for. You still have to make the model file, but now MC will know where to look
	 */
	@SideOnly(Side.CLIENT)
	public void register(Block block)
	{
		ItemBlock itemBlock = new ItemBlock(block);
		// Checks if the block has metadata / subtypes
		if (itemBlock.getHasSubtypes())
		{
			NonNullList<ItemStack> subItems = NonNullList.create();
			itemBlock.getSubItems(block.getCreativeTabToDisplayOn(), subItems);
			for (ItemStack sub : subItems)
			{
				ModelBakery.registerItemVariants(itemBlock, block.getRegistryName());
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, sub.getItemDamage(), new ModelResourceLocation(block.getRegistryName(), "inventory"));
			}
		}
		else
		{
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlock, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
		}
	}
}