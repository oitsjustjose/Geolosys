package com.oitsjustjose.geolosys.compat.ie;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.compat.ModMaterials;
import com.oitsjustjose.geolosys.config.ConfigOres;
import com.oitsjustjose.geolosys.config.ModConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.LinkedHashMap;

public class IECompat
{
    @SuppressWarnings("unchecked")
    public static void init()
    {
        ConfigOres conf = Geolosys.getInstance().configOres;
        OreDictionary.registerOre("clumpCoal", Items.COAL);
        if (ModMaterials.AE_MATERIAL != null)
        {
            OreDictionary.registerOre("crystalCertusQuartzCharged", new ItemStack(ModMaterials.AE_MATERIAL, 1, 1));
        }

        // Remove the vanilla ones
        LinkedHashMap<ExcavatorHandler.MineralMix, Integer> list = (LinkedHashMap<ExcavatorHandler.MineralMix, Integer>) ExcavatorHandler.mineralList.clone();
        for (ExcavatorHandler.MineralMix mix : list.keySet())
        {
            if (mix.name.equalsIgnoreCase("Iron") && (conf.hematiteChance > 0 || conf.limoniteChance > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Bauxite") && conf.bauxiteChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Cassiterite") && (conf.cassiteriteChance > 0 || conf.tealliteChance > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Coal") && conf.coalChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Copper") && (conf.malachiteChance > 0 || conf.azuriteChance > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Galena") && conf.galenaChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Gold") && conf.goldChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Lapis") && conf.lapisChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Lead") && conf.galenaChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Magnetite") && (conf.hematiteChance > 0 || conf.limoniteChance > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Nickel") && conf.limoniteChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Platinum") && conf.platinumChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Pyrite") && (conf.goldChance > 0 || conf.hematiteChance > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Quartzite") && conf.quartzChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Silver") && conf.galenaChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Uranium") && conf.autuniteChance > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
            }
        }

        // Add custom Geolosys entries
        ExcavatorHandler.addMineral("Coal", Geolosys.getInstance().configOres.coalChance, 0.1F, new String[]{"oreBlockCoal"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Cinnabar", Geolosys.getInstance().configOres.cinnabarChance, 0.05F, new String[]{"oreBlockCinnabar"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Gold", Geolosys.getInstance().configOres.goldChance, 0.05F, new String[]{"oreBlockGold"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Lapis", Geolosys.getInstance().configOres.lapisChance, 0.05F, new String[]{"oreBlockLapis"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Quartz", Geolosys.getInstance().configOres.quartzChance, 0.05F, new String[]{"oreBlockQuartz"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Kimberlite", Geolosys.getInstance().configOres.kimberliteChance, 0.05F, new String[]{"oreBlockKimberlite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Beryl", Geolosys.getInstance().configOres.berylChance, 0.05F, new String[]{"oreBlockBeryl"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Hematite", Geolosys.getInstance().configOres.hematiteChance, 0.25F, new String[]{"oreBlockHematite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Limonite", Geolosys.getInstance().configOres.limoniteChance, 0.05F, new String[]{"oreBlockLimonite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Malachite", Geolosys.getInstance().configOres.malachiteChance, 0.25F, new String[]{"oreBlockMalachite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Azurite", Geolosys.getInstance().configOres.azuriteChance, 0.05F, new String[]{"oreBlockAzurite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Cassiterite", Geolosys.getInstance().configOres.cassiteriteChance, 0.25F, new String[]{"oreBlockCassiterite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Teallite", Geolosys.getInstance().configOres.tealliteChance, 0.05F, new String[]{"oreBlockTeallite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Galena", Geolosys.getInstance().configOres.galenaChance, 0.05F, new String[]{"oreBlockGalena"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Bauxite", Geolosys.getInstance().configOres.bauxiteChance, 0.15F, new String[]{"oreBlockBauxite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Osmium", Geolosys.getInstance().configOres.platinumChance, 0.05F, new String[]{"oreBlockPlatinum"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Autunite", Geolosys.getInstance().configOres.autuniteChance, 0.05F, new String[]{"oreBlockAutunite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Sphalerite", Geolosys.getInstance().configOres.sphaleriteChance, 0.05F, new String[]{"oreBlockSphalerite"}, new float[]{1.0F});
    }
}
