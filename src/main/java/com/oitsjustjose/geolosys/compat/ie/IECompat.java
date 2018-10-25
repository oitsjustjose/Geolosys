package com.oitsjustjose.geolosys.compat.ie;

import java.util.LinkedHashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.compat.ModMaterials;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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
        LinkedHashMap<ExcavatorHandler.MineralMix, Integer> list = (LinkedHashMap<ExcavatorHandler.MineralMix, Integer>) ExcavatorHandler.mineralList
                .clone();
        for (ExcavatorHandler.MineralMix mix : list.keySet())
        {
            if (mix.name.equalsIgnoreCase("Iron") && (conf.hematite.getChance() > 0 || conf.limonite.getChance() > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Bauxite") && conf.bauxite.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Cassiterite")
                    && (conf.cassiterite.getChance() > 0 || conf.teallite.getChance() > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Coal") && conf.coal.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Copper") && (conf.malachite.getChance() > 0 || conf.azurite.getChance() > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Galena") && conf.galena.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Gold") && conf.gold.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Lapis") && conf.lapis.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Lead") && conf.galena.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Magnetite")
                    && (conf.hematite.getChance() > 0 || conf.limonite.getChance() > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Nickel") && conf.limonite.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Platinum") && conf.platinum.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Pyrite") && (conf.gold.getChance() > 0 || conf.hematite.getChance() > 0))
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Quartzite") && conf.quartz.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Silver") && conf.galena.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
                continue;
            }
            if (mix.name.equalsIgnoreCase("Uranium") && conf.autunite.getChance() > 0)
            {
                ExcavatorHandler.mineralList.remove(mix);
            }
        }

        // Add custom Geolosys entries
        ConfigOres oreConf = Geolosys.getInstance().configOres;
        if (oreConf.coal.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Coal", Geolosys.getInstance().configOres.coal.getChance(), 0.1F, new String[]
            { "oreBlockCoal" }, new float[]
            { 1.0F });
        }
        if (oreConf.cinnabar.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Cinnabar", Geolosys.getInstance().configOres.cinnabar.getChance(), 0.05F,
                    new String[]
                    { "oreBlockCinnabar" }, new float[]
                    { 1.0F });
        }
        if (oreConf.gold.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Gold", Geolosys.getInstance().configOres.gold.getChance(), 0.05F, new String[]
            { "oreBlockGold" }, new float[]
            { 1.0F });
        }
        if (oreConf.lapis.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Lapis", Geolosys.getInstance().configOres.lapis.getChance(), 0.05F,
                    new String[]
                    { "oreBlockLapis" }, new float[]
                    { 1.0F });
        }
        if (oreConf.quartz.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Quartz", Geolosys.getInstance().configOres.quartz.getChance(), 0.05F,
                    new String[]
                    { "oreBlockQuartz" }, new float[]
                    { 1.0F });
        }
        if (oreConf.kimberlite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Kimberlite", Geolosys.getInstance().configOres.kimberlite.getChance(), 0.05F,
                    new String[]
                    { "oreBlockKimberlite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.beryl.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Beryl", Geolosys.getInstance().configOres.beryl.getChance(), 0.05F,
                    new String[]
                    { "oreBlockBeryl" }, new float[]
                    { 1.0F });
        }
        if (oreConf.hematite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Hematite", Geolosys.getInstance().configOres.hematite.getChance(), 0.25F,
                    new String[]
                    { "oreBlockHematite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.limonite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Limonite", Geolosys.getInstance().configOres.limonite.getChance(), 0.05F,
                    new String[]
                    { "oreBlockLimonite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.malachite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Malachite", Geolosys.getInstance().configOres.malachite.getChance(), 0.25F,
                    new String[]
                    { "oreBlockMalachite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.azurite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Azurite", Geolosys.getInstance().configOres.azurite.getChance(), 0.05F,
                    new String[]
                    { "oreBlockAzurite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.cassiterite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Cassiterite", Geolosys.getInstance().configOres.cassiterite.getChance(), 0.25F,
                    new String[]
                    { "oreBlockCassiterite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.teallite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Teallite", Geolosys.getInstance().configOres.teallite.getChance(), 0.05F,
                    new String[]
                    { "oreBlockTeallite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.galena.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Galena", Geolosys.getInstance().configOres.galena.getChance(), 0.05F,
                    new String[]
                    { "oreBlockGalena" }, new float[]
                    { 1.0F });
        }
        if (oreConf.bauxite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Bauxite", Geolosys.getInstance().configOres.bauxite.getChance(), 0.15F,
                    new String[]
                    { "oreBlockBauxite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.platinum.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Platinum", Geolosys.getInstance().configOres.platinum.getChance(), 0.05F,
                    new String[]
                    { "oreBlockPlatinum" }, new float[]
                    { 1.0F });
        }
        if (oreConf.autunite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Autunite", Geolosys.getInstance().configOres.autunite.getChance(), 0.05F,
                    new String[]
                    { "oreBlockAutunite" }, new float[]
                    { 1.0F });
        }
        if (oreConf.sphalerite.getChance() > 0)
        {
            ExcavatorHandler.addMineral("Sphalerite", Geolosys.getInstance().configOres.sphalerite.getChance(), 0.05F,
                    new String[]
                    { "oreBlockSphalerite" }, new float[]
                    { 1.0F });
        }
    }
}
