package com.oitsjustjose.geolosys.util;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.Lib;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config
{
	public Configuration config;
	public ConfigCategory FeatureControl;
	public ConfigCategory Weights;

	public boolean enableIron;
	public boolean enableDiamonds;
	public boolean enableCopper;
	public boolean enableTin;
	public boolean enableSilverLead;
	public boolean enableAluminum;

	public int weightIron;
	public int weightDiamonds;
	public int weightEmeralds;
	public int weightCopper;
	public int weightTin;
	public int weightSilverLead;
	public int weightAluminum;

	public Config(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile, null, true);
			loadConfiguration();
		}
	}

	void loadConfiguration()
	{
		Property property;

		String category = "Feature Control";
		List<String> propertyOrder = Lists.newArrayList();
		FeatureControl = config.getCategory(category);
		FeatureControl.setComment("Enable or Disable features as a whole");

		property = config.get(category, "Iron", true);
		property.setComment("Enabling will generate Iron Ore as Limonite or Hematite");
		enableIron = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Diamonds", true);
		property.setComment("Enabling will allow Diamonds to be more common");
		enableDiamonds = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Copper", true);
		property.setComment("Enabling will generate Copper Ore as Malachite or Lazurite");
		enableCopper = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Tin", true);
		property.setComment("Enabling will generate Tin Ore as Cassiterite or Sphalerite");
		enableTin = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Silver & Lead", true);
		property.setComment("Enabling will generate Silver and Lead Ore as Galena");
		enableSilverLead = property.getBoolean();
		propertyOrder.add(property.getName());

		property = config.get(category, "Aluminum", true);
		property.setComment("Enabling will generate Aluminum Ore as Bauxite");
		enableAluminum = property.getBoolean();
		propertyOrder.add(property.getName());

		FeatureControl.setPropertyOrder(propertyOrder);

		// Weights
		category = "Ore Gen Weights";
		propertyOrder = Lists.newArrayList();
		Weights = config.getCategory(category);
		Weights.setComment("Fine-tuned adjustments for ore generation rates.");

		Weights.setPropertyOrder(propertyOrder);

		if (config.hasChanged())
		{
			config.save();
		}
	}

	@SubscribeEvent
	public void update(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(Lib.MODID))
		{
			loadConfiguration();
			// TODO: Anything that needs to be changed after updating configs goes here
		}
	}
}