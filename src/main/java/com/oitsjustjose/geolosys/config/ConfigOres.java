package com.oitsjustjose.geolosys.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;

public class ConfigOres
{
    public int coalSize = -1;
    public int coalChance = -1;
    public int[] coalDimBlacklist = null;
    public int coalMinY = -1;
    public int coalMaxY = -1;

    public int cinnabarSize = -1;
    public int cinnabarChance = -1;
    public int[] cinnabarDimBlacklist = null;
    public int cinnabarMinY = -1;
    public int cinnabarMaxY = -1;

    public int goldSize = -1;
    public int goldChance = -1;
    public int[] goldDimBlacklist = null;
    public int goldMinY = -1;
    public int goldMaxY = -1;

    public int lapisSize = -1;
    public int lapisChance = -1;
    public int[] lapisDimBlacklist = null;
    public int lapisMinY = -1;
    public int lapisMaxY = -1;

    public int quartzSize = -1;
    public int quartzChance = -1;
    public int[] quartzDimBlacklist = null;
    public int quartzMinY = -1;
    public int quartzMaxY = -1;

    public int kimberliteSize = -1;
    public int kimberliteChance = -1;
    public int[] kimberliteDimBlacklist = null;
    public int kimberliteMinY = -1;
    public int kimberliteMaxY = -1;

    public int berylSize = -1;
    public int berylChance = -1;
    public int[] berylDimBlacklist = null;
    public int berylMinY = -1;
    public int berylMaxY = -1;

    public int hematiteSize = -1;
    public int hematiteChance = -1;
    public int[] hematiteDimBlacklist = null;
    public int hematiteMinY = -1;
    public int hematiteMaxY = -1;

    public int limoniteSize = -1;
    public int limoniteChance = -1;
    public int[] limoniteDimBlacklist = null;
    public int limoniteMinY = -1;
    public int limoniteMaxY = -1;

    public int malachiteSize = -1;
    public int malachiteChance = -1;
    public int[] malachiteDimBlacklist = null;
    public int malachiteMinY = -1;
    public int malachiteMaxY = -1;

    public int azuriteSize = -1;
    public int azuriteChance = -1;
    public int[] azuriteDimBlacklist = null;
    public int azuriteMinY = -1;
    public int azuriteMaxY = -1;

    public int cassiteriteSize = -1;
    public int cassiteriteChance = -1;
    public int[] cassiteriteDimBlacklist = null;
    public int cassiteriteMinY = -1;
    public int cassiteriteMaxY = -1;

    public int tealliteSize = -1;
    public int tealliteChance = -1;
    public int[] tealliteDimBlacklist = null;
    public int tealliteMinY = -1;
    public int tealliteMaxY = -1;

    public int galenaSize = -1;
    public int galenaChance = -1;
    public int[] galenaDimBlacklist = null;
    public int galenaMinY = -1;
    public int galenaMaxY = -1;

    public int bauxiteSize = -1;
    public int bauxiteChance = -1;
    public int[] bauxiteDimBlacklist = null;
    public int bauxiteMinY = -1;
    public int bauxiteMaxY = -1;

    public int platinumSize = -1;
    public int platinumChance = -1;
    public int[] platinumDimBlacklist = null;
    public int platinumMinY = -1;
    public int platinumMaxY = -1;

    public int autuniteSize = -1;
    public int autuniteChance = -1;
    public int[] autuniteDimBlacklist = null;
    public int autuniteMinY = -1;
    public int autuniteMaxY = -1;

    public int sphaleriteSize = -1;
    public int sphaleriteChance = -1;
    public int[] sphaleriteDimBlacklist = null;
    public int sphaleriteMinY = -1;
    public int sphaleriteMaxY = -1;

    public void validate(File configDir)
    {
        boolean dirty = false;
        for (Field f : this.getClass().getFields())
        {
            if (f.getType() == int.class)
            {
                try
                {
                    Field field = this.getClass().getField(f.getName());
                    int val = field.getInt(this);
                    if (val == -1)
                    {
                        dirty = true;
                        int defValue = -1;
                        // Dynamically find the default value using reflection
                        for (Field searchField : ConfigOresDefault.class.getFields())
                        {
                            if (searchField.getName().equalsIgnoreCase(f.getName() + "Default"))
                            {
                                defValue = searchField.getInt(ConfigOresDefault.class);
                                break;
                            }
                        }
                        f.setInt(this, defValue);
                    }
                }
                catch (IllegalAccessException | NoSuchFieldException e)
                {
                    e.printStackTrace();
                }
            }
            else if (f.getType().isArray())
            {
                try
                {
                    int[] val = (int[]) f.get(this);
                    if (val == null)
                    {
                        System.out.println(f.getName() + " is null");
                        dirty = true;
                        int[] defValue = new int[]{};
                        // Dynamically find the default value using reflection
                        for (Field searchField : ConfigOresDefault.class.getFields())
                        {
                            if (searchField.getName().equalsIgnoreCase(f.getName() + "Default"))
                            {
                                defValue = (int[]) searchField.get(ConfigOresDefault.class);
                                System.out.println(f.getName() + " is now " + Arrays.toString(defValue));
                                break;
                            }
                        }
                        f.set(this, defValue);
                    }
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }

        // Rewrite the JSON of missing components:
        if (dirty)
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            try
            {
                FileWriter fw = new FileWriter(configDir.getAbsolutePath() + "/geolosys_ores.json".replace("/", File.separator));
                fw.write(json);
                fw.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }


    public void populateConfigs()
    {
        coalSize = ConfigOresDefault.coalSizeDefault;
        coalChance = ConfigOresDefault.coalChanceDefault;
        coalDimBlacklist = ConfigOresDefault.coalDimBlacklistDefault;
        coalMinY = ConfigOresDefault.coalMinYDefault;
        coalMaxY = ConfigOresDefault.coalMaxYDefault;

        cinnabarSize = ConfigOresDefault.cinnabarSizeDefault;
        cinnabarChance = ConfigOresDefault.cinnabarChanceDefault;
        cinnabarDimBlacklist = ConfigOresDefault.cinnabarDimBlacklistDefault;
        cinnabarMinY = ConfigOresDefault.cinnabarMinYDefault;
        cinnabarMaxY = ConfigOresDefault.cinnabarMaxYDefault;

        goldSize = ConfigOresDefault.goldSizeDefault;
        goldChance = ConfigOresDefault.goldChanceDefault;
        goldDimBlacklist = ConfigOresDefault.goldDimBlacklistDefault;
        goldMinY = ConfigOresDefault.goldMinYDefault;
        goldMaxY = ConfigOresDefault.goldMaxYDefault;

        lapisSize = ConfigOresDefault.lapisSizeDefault;
        lapisChance = ConfigOresDefault.lapisChanceDefault;
        lapisDimBlacklist = ConfigOresDefault.lapisDimBlacklistDefault;
        lapisMinY = ConfigOresDefault.lapisMinYDefault;
        lapisMaxY = ConfigOresDefault.lapisMaxYDefault;

        quartzSize = ConfigOresDefault.quartzSizeDefault;
        quartzChance = ConfigOresDefault.quartzChanceDefault;
        quartzDimBlacklist = ConfigOresDefault.quartzDimBlacklistDefault;
        quartzMinY = ConfigOresDefault.quartzMinYDefault;
        quartzMaxY = ConfigOresDefault.quartzMaxYDefault;

        kimberliteSize = ConfigOresDefault.kimberliteSizeDefault;
        kimberliteChance = ConfigOresDefault.kimberliteChanceDefault;
        kimberliteDimBlacklist = ConfigOresDefault.kimberliteDimBlacklistDefault;
        kimberliteMinY = ConfigOresDefault.kimberliteMinYDefault;
        kimberliteMaxY = ConfigOresDefault.kimberliteMaxYDefault;

        berylSize = ConfigOresDefault.berylSizeDefault;
        berylChance = ConfigOresDefault.berylChanceDefault;
        berylDimBlacklist = ConfigOresDefault.berylDimBlacklistDefault;
        berylMinY = ConfigOresDefault.berylMinYDefault;
        berylMaxY = ConfigOresDefault.berylMaxYDefault;

        hematiteSize = ConfigOresDefault.hematiteSizeDefault;
        hematiteChance = ConfigOresDefault.hematiteChanceDefault;
        hematiteDimBlacklist = ConfigOresDefault.hematiteDimBlacklistDefault;
        hematiteMinY = ConfigOresDefault.hematiteMinYDefault;
        hematiteMaxY = ConfigOresDefault.hematiteMaxYDefault;

        limoniteSize = ConfigOresDefault.limoniteSizeDefault;
        limoniteChance = ConfigOresDefault.limoniteChanceDefault;
        limoniteDimBlacklist = ConfigOresDefault.limoniteDimBlacklistDefault;
        limoniteMinY = ConfigOresDefault.limoniteMinYDefault;
        limoniteMaxY = ConfigOresDefault.limoniteMaxYDefault;

        malachiteSize = ConfigOresDefault.malachiteSizeDefault;
        malachiteChance = ConfigOresDefault.malachiteChanceDefault;
        malachiteDimBlacklist = ConfigOresDefault.malachiteDimBlacklistDefault;
        malachiteMinY = ConfigOresDefault.malachiteMinYDefault;
        malachiteMaxY = ConfigOresDefault.malachiteMaxYDefault;

        azuriteSize = ConfigOresDefault.azuriteSizeDefault;
        azuriteChance = ConfigOresDefault.azuriteChanceDefault;
        azuriteDimBlacklist = ConfigOresDefault.azuriteDimBlacklistDefault;
        azuriteMinY = ConfigOresDefault.azuriteMinYDefault;
        azuriteMaxY = ConfigOresDefault.azuriteMaxYDefault;

        cassiteriteSize = ConfigOresDefault.cassiteriteSizeDefault;
        cassiteriteChance = ConfigOresDefault.cassiteriteChanceDefault;
        cassiteriteDimBlacklist = ConfigOresDefault.cassiteriteDimBlacklistDefault;
        cassiteriteMinY = ConfigOresDefault.cassiteriteMinYDefault;
        cassiteriteMaxY = ConfigOresDefault.cassiteriteMaxYDefault;

        tealliteSize = ConfigOresDefault.tealliteSizeDefault;
        tealliteChance = ConfigOresDefault.tealliteChanceDefault;
        tealliteDimBlacklist = ConfigOresDefault.tealliteDimBlacklistDefault;
        tealliteMinY = ConfigOresDefault.tealliteMinYDefault;
        tealliteMaxY = ConfigOresDefault.tealliteMaxYDefault;

        galenaSize = ConfigOresDefault.galenaSizeDefault;
        galenaChance = ConfigOresDefault.galenaChanceDefault;
        galenaDimBlacklist = ConfigOresDefault.galenaDimBlacklistDefault;
        galenaMinY = ConfigOresDefault.galenaMinYDefault;
        galenaMaxY = ConfigOresDefault.galenaMaxYDefault;

        bauxiteSize = ConfigOresDefault.bauxiteSizeDefault;
        bauxiteChance = ConfigOresDefault.bauxiteChanceDefault;
        bauxiteDimBlacklist = ConfigOresDefault.bauxiteDimBlacklistDefault;
        bauxiteMinY = ConfigOresDefault.bauxiteMinYDefault;
        bauxiteMaxY = ConfigOresDefault.bauxiteMaxYDefault;

        platinumSize = ConfigOresDefault.platinumSizeDefault;
        platinumChance = ConfigOresDefault.platinumChanceDefault;
        platinumDimBlacklist = ConfigOresDefault.platinumDimBlacklistDefault;
        platinumMinY = ConfigOresDefault.platinumMinYDefault;
        platinumMaxY = ConfigOresDefault.platinumMaxYDefault;

        autuniteSize = ConfigOresDefault.autuniteSizeDefault;
        autuniteChance = ConfigOresDefault.autuniteChanceDefault;
        autuniteDimBlacklist = ConfigOresDefault.autuniteDimBlacklistDefault;
        autuniteMinY = ConfigOresDefault.autuniteMinYDefault;
        autuniteMaxY = ConfigOresDefault.autuniteMaxYDefault;

        sphaleriteSize = ConfigOresDefault.sphaleriteSizeDefault;
        sphaleriteChance = ConfigOresDefault.sphaleriteChanceDefault;
        sphaleriteDimBlacklist = ConfigOresDefault.sphaleriteDimBlacklistDefault;
        sphaleriteMinY = ConfigOresDefault.sphaleriteMinYDefault;
        sphaleriteMaxY = ConfigOresDefault.sphaleriteMaxYDefault;
    }
}
