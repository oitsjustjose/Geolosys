package com.oitsjustjose.geolosys.compat.ie;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import com.oitsjustjose.geolosys.Geolosys;

import java.util.LinkedHashMap;

public class IECompat
{
    public static void init()
    {
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

        ExcavatorHandler.addMineral("Coal", Geolosys.getInstance().configOres.coalChance, 0.05F, new String[]{"geolosysOreCoal"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Cinnabar", Geolosys.getInstance().configOres.cinnabarChance, 0.09F, new String[]{"geolosysOreCinnabar"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Gold", Geolosys.getInstance().configOres.goldChance, 0.05F, new String[]{"geolosysOreGold"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Lapis", Geolosys.getInstance().configOres.lapisChance, 0.05F, new String[]{"geolosysOreLapis"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Quartz", Geolosys.getInstance().configOres.quartzChance, 0.05F, new String[]{"geolosysOreQuartz"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Kimberlite", Geolosys.getInstance().configOres.kimberliteChance, 0.05F, new String[]{"geolosysOreKimberlite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Beryl", Geolosys.getInstance().configOres.berylChance, 0.05F, new String[]{"geolosysOreBeryl"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Hematite", Geolosys.getInstance().configOres.hematiteChance, 0.05F, new String[]{"geolosysOreHematite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Limonite", Geolosys.getInstance().configOres.limoniteChance, 0.05F, new String[]{"geolosysOreLimonite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Malachite", Geolosys.getInstance().configOres.malachiteChance, 0.05F, new String[]{"geolosysOreMalachite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Azurite", Geolosys.getInstance().configOres.azuriteChance, 0.05F, new String[]{"geolosysOreAzurite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Cassiterite", Geolosys.getInstance().configOres.cassiteriteChance, 0.05F, new String[]{"geolosysOreCassiterite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Teallite", Geolosys.getInstance().configOres.tealliteChance, 0.05F, new String[]{"geolosysOreTeallite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Galena", Geolosys.getInstance().configOres.galenaChance, 0.05F, new String[]{"geolosysOreGalena"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Bauxite", Geolosys.getInstance().configOres.bauxiteChance, 0.05F, new String[]{"geolosysOreBauxite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Platinum", Geolosys.getInstance().configOres.platinumChance, 0.05F, new String[]{"geolosysOrePlatinum"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Autunite", Geolosys.getInstance().configOres.autuniteChance, 0.05F, new String[]{"geolosysOreAutunite"}, new float[]{1.0F});
        ExcavatorHandler.addMineral("Sphalerite", Geolosys.getInstance().configOres.sphaleriteChance, 0.05F, new String[]{"geolosysOreSphalerite"}, new float[]{1.0F});
    }
}
