package com.oitsjustjose.geolosys.compat.ie;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import java.util.LinkedHashMap;

public class IECompat
{
    public static void init()
    {
        OreDictionary.registerOre("clumpCoal", Items.COAL);
        final Item AE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"));

        if (AE_MATERIAL != null)
        {
            OreDictionary.registerOre("crystalCertusQuartzCharged", new ItemStack(AE_MATERIAL, 1, 1));
        }
        // Remove the vanilla ones
        LinkedHashMap<ExcavatorHandler.MineralMix, Integer> list = (LinkedHashMap<ExcavatorHandler.MineralMix, Integer>) ExcavatorHandler.mineralList.clone();

        for (ExcavatorHandler.MineralMix mix : list.keySet())
        {
            if (mix.name.equalsIgnoreCase("Iron"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Bauxite"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Cassiterite"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Coal"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Copper"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Galena"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Gold"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Iron"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Lapis"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Lead"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Magnetite"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Nickel"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Platinum"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Pyrite"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Quartzite"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Silver"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Uranium"))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
        }

        ExcavatorHandler.addMineral("Coal", Geolosys.getInstance().configOres.coalChance, 0.05F, new String[]{"clumpCoal", "dustSulfur"}, new float[]{0.98F, 0.02F});
        ExcavatorHandler.addMineral("Cinnabar", Geolosys.getInstance().configOres.cinnabarChance, 0.05F, new String[]{"dustRedstone", "crystalCinnabar", "gemRedstone"}, new float[]{.959F, .034F, .017F});
        ExcavatorHandler.addMineral("Gold", Geolosys.getInstance().configOres.goldChance, 0.05F, new String[]{"geolosysOreGold"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Lapis", Geolosys.getInstance().configOres.lapisChance, 0.05F, new String[]{"dustLapis"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Quartz", Geolosys.getInstance().configOres.quartzChance, 0.05F, new String[]{"gemQuartz", "crystalCertusQuartz", "crystalCertusQuartzCharged", "gemQuartzBlack"}, new float[]{.8F, .08F, .04F, .08F});
        ExcavatorHandler.addMineral("Kimberlite", Geolosys.getInstance().configOres.kimberliteChance, 0.05F, new String[]{"gemDiamond"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Beryl", Geolosys.getInstance().configOres.berylChance, 0.05F, new String[]{"gemEmerald"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Hematite", Geolosys.getInstance().configOres.hematiteChance, 0.25F, new String[]{"geolosysOreIron"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Limonite", Geolosys.getInstance().configOres.limoniteChance, 0.05F, new String[]{"geolosysOreIron", "geolosysOreNickel"}, new float[]{0.8F, 0.2F});
        ExcavatorHandler.addMineral("Malachite", Geolosys.getInstance().configOres.malachiteChance, 0.25F, new String[]{"geolosysOreCopper"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Azurite", Geolosys.getInstance().configOres.azuriteChance, 0.05F, new String[]{"geolosysOreCopper"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Cassiterite", Geolosys.getInstance().configOres.cassiteriteChance, 0.25F, new String[]{"geolosysOreTin"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Teallite", Geolosys.getInstance().configOres.tealliteChance, 0.05F, new String[]{"geolosysOreTin"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Galena", Geolosys.getInstance().configOres.galenaChance, 0.05F, new String[]{"geolosysOreSilver", "geolosysOreLead"}, new float[]{0.5F, 0.5F});
        ExcavatorHandler.addMineral("Bauxite", Geolosys.getInstance().configOres.bauxiteChance, 0.15F, new String[]{"geolosysOreAluminum"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Platinum", Geolosys.getInstance().configOres.platinumChance, 0.05F, new String[]{"geolosysOrePlatinum", "geolosysOreOsmium"}, new float[]{0.5F, 0.5F});
        ExcavatorHandler.addMineral("Autunite", Geolosys.getInstance().configOres.autuniteChance, 0.05F, new String[]{"geolosysOreUranium", "geolosysOreYellorium"}, new float[]{0.5F, 0.5F});
        ExcavatorHandler.addMineral("Sphalerite", Geolosys.getInstance().configOres.sphaleriteChance, 0.05F, new String[]{"geolosysOreZinc"}, new float[]{1.0F});
    }
}
