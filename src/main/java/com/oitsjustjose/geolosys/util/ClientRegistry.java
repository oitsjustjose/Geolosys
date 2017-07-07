package com.oitsjustjose.geolosys.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ClientRegistry
{
	private HashMap<ItemStack, ModelResourceLocation> LOCATIONS = new HashMap();

	@SubscribeEvent
	public void handleModels(ModelRegistryEvent event)
	{
		for (Map.Entry<ItemStack, ModelResourceLocation> entry : LOCATIONS.entrySet())
		{
			ModelLoader.setCustomModelResourceLocation(entry.getKey().getItem(), entry.getKey().getItemDamage(), entry.getValue());
		}
	}

	public void register(Block block, String variant)
	{
		LOCATIONS.put(new ItemStack(block), new ModelResourceLocation(block.getRegistryName(), variant));
	}

	public void register(ItemStack itemstack, ResourceLocation resLoc, String variant)
	{
		LOCATIONS.put(itemstack, new ModelResourceLocation(resLoc, variant));
	}

	public void register(ItemStack stack, String variant)
	{
		LOCATIONS.put(stack, new ModelResourceLocation(stack.getItem().getRegistryName(), variant));
	}
}
