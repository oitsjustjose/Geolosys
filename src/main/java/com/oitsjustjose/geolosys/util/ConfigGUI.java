package com.oitsjustjose.geolosys.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGUI extends GuiConfig
{
	public ConfigGUI(GuiScreen guiScreen)
	{
		super(guiScreen, getConfigElements(), Lib.MODID, false, false, "Geolosys Configuration");
	}

	private static List<IConfigElement> getConfigElements()
	{
		ArrayList<IConfigElement> list = new ArrayList<IConfigElement>();

		list.add(new ConfigElement(Geolosys.config.FeatureControl));
		list.add(new ConfigElement(Geolosys.config.Weights));
		list.add(new ConfigElement(Geolosys.config.Sizes));

		return list;
	}

	public static class GUIFactory implements IModGuiFactory
	{
		@Override
		public void initialize(Minecraft minecraftInstance)
		{

		}

		@Override
		public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
		{
			return null;
		}

		@Override
		public boolean hasConfigGui()
		{
			return true;
		}

		@Override
		public GuiScreen createConfigGui(GuiScreen parentScreen)
		{
			return new ConfigGUI(parentScreen);
		}
	}
}