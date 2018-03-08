package com.oitsjustjose.geolosys.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ConfigOres
{
    public int coalSize = -1;
    public int coalChance = -1;
    public int[] coalDimBlacklist = {};
    public int coalMinY = -1;
    public int coalMaxY = -1;

    public int cinnabarSize = -1;
    public int cinnabarChance = -1;
    public int[] cinnabarDimBlacklist = {};
    public int cinnabarMinY = -1;
    public int cinnabarMaxY = -1;

    public int goldSize = -1;
    public int goldChance = -1;
    public int[] goldDimBlacklist = {};
    public int goldMinY = -1;
    public int goldMaxY = -1;

    public int lapisSize = -1;
    public int lapisChance = -1;
    public int[] lapisDimBlacklist = {};
    public int lapisMinY = -1;
    public int lapisMaxY = -1;

    public int quartzSize = -1;
    public int quartzChance = -1;
    public int[] quartzDimBlacklist = {};
    public int quartzMinY = -1;
    public int quartzMaxY = -1;

    public int kimberliteSize = -1;
    public int kimberliteChance = -1;
    public int[] kimberliteDimBlacklist = {};
    public int kimberliteMinY = -1;
    public int kimberliteMaxY = -1;

    public int berylSize = -1;
    public int berylChance = -1;
    public int[] berylDimBlacklist = {};
    public int berylMinY = -1;
    public int berylMaxY = -1;

    public int hematiteSize = -1;
    public int hematiteChance = -1;
    public int[] hematiteDimBlacklist = {};
    public int hematiteMinY = -1;
    public int hematiteMaxY = -1;

    public int limoniteSize = -1;
    public int limoniteChance = -1;
    public int[] limoniteDimBlacklist = {};
    public int limoniteMinY = -1;
    public int limoniteMaxY = -1;

    public int malachiteSize = -1;
    public int malachiteChance = -1;
    public int[] malachiteDimBlacklist = {};
    public int malachiteMinY = -1;
    public int malachiteMaxY = -1;

    public int azuriteSize = -1;
    public int azuriteChance = -1;
    public int[] azuriteDimBlacklist = {};
    public int azuriteMinY = -1;
    public int azuriteMaxY = -1;

    public int cassiteriteSize = -1;
    public int cassiteriteChance = -1;
    public int[] cassiteriteDimBlacklist = {};
    public int cassiteriteMinY = -1;
    public int cassiteriteMaxY = -1;

    public int tealliteSize = -1;
    public int tealliteChance = -1;
    public int[] tealliteDimBlacklist = {};
    public int tealliteMinY = -1;
    public int tealliteMaxY = -1;

    public int galenaSize = -1;
    public int galenaChance = -1;
    public int[] galenaDimBlacklist = {};
    public int galenaMinY = -1;
    public int galenaMaxY = -1;

    public int bauxiteSize = -1;
    public int bauxiteChance = -1;
    public int[] bauxiteDimBlacklist = {};
    public int bauxiteMinY = -1;
    public int bauxiteMaxY = -1;

    public int platinumSize = -1;
    public int platinumChance = -1;
    public int[] platinumDimBlacklist = {};
    public int platinumMinY = -1;
    public int platinumMaxY = -1;

    public int autuniteSize = -1;
    public int autuniteChance = -1;
    public int[] autuniteDimBlacklist = {};
    public int autuniteMinY = -1;
    public int autuniteMaxY = -1;

    public int sphaleriteSize = -1;
    public int sphaleriteChance = -1;
    public int[] sphaleriteDimBlacklist = {};
    public int sphaleriteMinY = -1;
    public int sphaleriteMaxY = -1;

    public final int coalSizeDefault = 64;
    public final int coalChanceDefault = 8;
    public final int[] coalDimBlacklistDefault = new int[]{-1, 1};
    public final int coalMinYDefault = 48;
    public final int coalMaxYDefault = 78;

    public final int cinnabarSizeDefault = 40;
    public final int cinnabarChanceDefault = 5;
    public final int[] cinnabarDimBlacklistDefault = new int[]{-1, 1};
    public final int cinnabarMinYDefault = 5;
    public final int cinnabarMaxYDefault = 12;

    public final int goldSizeDefault = 40;
    public final int goldChanceDefault = 3;
    public final int[] goldDimBlacklistDefault = new int[]{-1, 1};
    public final int goldMinYDefault = 5;
    public final int goldMaxYDefault = 30;

    public final int lapisSizeDefault = 32;
    public final int lapisChanceDefault = 4;
    public final int[] lapisDimBlacklistDefault = new int[]{-1, 1};
    public final int lapisMinYDefault = 10;
    public final int lapisMaxYDefault = 24;

    public final int quartzSizeDefault = 40;
    public final int quartzChanceDefault = 6;
    public final int[] quartzDimBlacklistDefault = new int[]{-1, 1};
    public final int quartzMinYDefault = 6;
    public final int quartzMaxYDefault = 29;

    public final int kimberliteSizeDefault = 20;
    public final int kimberliteChanceDefault = 4;
    public final int[] kimberliteDimBlacklistDefault = new int[]{-1, 1};
    public final int kimberliteMinYDefault = 2;
    public final int kimberliteMaxYDefault = 15;

    public final int berylSizeDefault = 16;
    public final int berylChanceDefault = 3;
    public final int[] berylDimBlacklistDefault = new int[]{-1, 1};
    public final int berylMinYDefault = 4;
    public final int berylMaxYDefault = 32;

    public final int hematiteSizeDefault = 24;
    public final int hematiteChanceDefault = 8;
    public final int[] hematiteDimBlacklistDefault = new int[]{-1, 1};
    public final int hematiteMinYDefault = 32;
    public final int hematiteMaxYDefault = 60;

    public final int limoniteSizeDefault = 80;
    public final int limoniteChanceDefault = 6;
    public final int[] limoniteDimBlacklistDefault = new int[]{-1, 1};
    public final int limoniteMinYDefault = 6;
    public final int limoniteMaxYDefault = 32;

    public final int malachiteSizeDefault = 24;
    public final int malachiteChanceDefault = 6;
    public final int[] malachiteDimBlacklistDefault = new int[]{-1, 1};
    public final int malachiteMinYDefault = 39;
    public final int malachiteMaxYDefault = 44;

    public final int azuriteSizeDefault = 80;
    public final int azuriteChanceDefault = 7;
    public final int[] azuriteDimBlacklistDefault = new int[]{-1, 1};
    public final int azuriteMinYDefault = 12;
    public final int azuriteMaxYDefault = 44;

    public final int cassiteriteSizeDefault = 24;
    public final int cassiteriteChanceDefault = 6;
    public final int[] cassiteriteDimBlacklistDefault = new int[]{-1, 1};
    public final int cassiteriteMinYDefault = 44;
    public final int cassiteriteMaxYDefault = 68;

    public final int tealliteSizeDefault = 80;
    public final int tealliteChanceDefault = 7;
    public final int[] tealliteDimBlacklistDefault = new int[]{-1, 1};
    public final int tealliteMinYDefault = 8;
    public final int tealliteMaxYDefault = 43;

    public final int galenaSizeDefault = 72;
    public final int galenaChanceDefault = 6;
    public final int[] galenaDimBlacklistDefault = new int[]{-1, 1};
    public final int galenaMinYDefault = 16;
    public final int galenaMaxYDefault = 50;

    public final int bauxiteSizeDefault = 64;
    public final int bauxiteChanceDefault = 6;
    public final int[] bauxiteDimBlacklistDefault = new int[]{-1, 1};
    public final int bauxiteMinYDefault = 45;
    public final int bauxiteMaxYDefault = 70;

    public final int platinumSizeDefault = 32;
    public final int platinumChanceDefault = 4;
    public final int[] platinumDimBlacklistDefault = new int[]{-1, 1};
    public final int platinumMinYDefault = 3;
    public final int platinumMaxYDefault = 25;

    public final int autuniteSizeDefault = 24;
    public final int autuniteChanceDefault = 5;
    public final int[] autuniteDimBlacklistDefault = new int[]{-1, 1};
    public final int autuniteMinYDefault = 8;
    public final int autuniteMaxYDefault = 33;

    public final int sphaleriteSizeDefault = 24;
    public final int sphaleriteChanceDefault = 5;
    public final int[] sphaleriteDimBlacklistDefault = new int[]{-1, 1};
    public final int sphaleriteMinYDefault = 35;
    public final int sphaleriteMaxYDefault = 55;

    public void validate()
    {
        for (Field f : this.getClass().getFields())
        {
            if (f.getModifiers() == Modifier.PUBLIC + Modifier.FINAL)
            {
                continue;
            }
            if (f.getType() == int.class)
            {
                try
                {
                    Field field = this.getClass().getField(f.getName());
                    int val = field.getInt(this);
                    if (val == -1)
                    {
                        int defValue = -1;
                        // Dynamically find the default value using reflection
                        for (Field searchField : this.getClass().getFields())
                        {
                            if (searchField.getName().equalsIgnoreCase(f.getName() + "Default"))
                            {
                                defValue = searchField.getInt(this);
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
            else if (f.getType() == Array.class)
            {
                try
                {
                    int[] val = (int[]) f.get(this);
                    if (val.length == 0)
                    {
                        int[] defValue = new int[]{};
                        // Dynamically find the default value using reflection
                        for (Field searchField : this.getClass().getFields())
                        {
                            if (searchField.getName().equalsIgnoreCase(f.getName() + "Default"))
                            {
                                defValue = (int[]) searchField.get(this);
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
    }


    public void populateConfigs()
    {
        coalSize = coalSizeDefault;
        coalChance = coalChanceDefault;
        coalDimBlacklist = coalDimBlacklistDefault;
        coalMinY = coalMinYDefault;
        coalMaxY = coalMaxYDefault;

        cinnabarSize = cinnabarSizeDefault;
        cinnabarChance = cinnabarChanceDefault;
        cinnabarDimBlacklist = cinnabarDimBlacklistDefault;
        cinnabarMinY = cinnabarMinYDefault;
        cinnabarMaxY = cinnabarMaxYDefault;

        goldSize = goldSizeDefault;
        goldChance = goldChanceDefault;
        goldDimBlacklist = goldDimBlacklistDefault;
        goldMinY = goldMinYDefault;
        goldMaxY = goldMaxYDefault;

        lapisSize = lapisSizeDefault;
        lapisChance = lapisChanceDefault;
        lapisDimBlacklist = lapisDimBlacklistDefault;
        lapisMinY = lapisMinYDefault;
        lapisMaxY = lapisMaxYDefault;

        quartzSize = quartzSizeDefault;
        quartzChance = quartzChanceDefault;
        quartzDimBlacklist = quartzDimBlacklistDefault;
        quartzMinY = quartzMinYDefault;
        quartzMaxY = quartzMaxYDefault;

        kimberliteSize = kimberliteSizeDefault;
        kimberliteChance = kimberliteChanceDefault;
        kimberliteDimBlacklist = kimberliteDimBlacklistDefault;
        kimberliteMinY = kimberliteMinYDefault;
        kimberliteMaxY = kimberliteMaxYDefault;

        berylSize = berylSizeDefault;
        berylChance = berylChanceDefault;
        berylDimBlacklist = berylDimBlacklistDefault;
        berylMinY = berylMinYDefault;
        berylMaxY = berylMaxYDefault;

        hematiteSize = hematiteSizeDefault;
        hematiteChance = hematiteChanceDefault;
        hematiteDimBlacklist = hematiteDimBlacklistDefault;
        hematiteMinY = hematiteMinYDefault;
        hematiteMaxY = hematiteMaxYDefault;

        limoniteSize = limoniteSizeDefault;
        limoniteChance = limoniteChanceDefault;
        limoniteDimBlacklist = limoniteDimBlacklistDefault;
        limoniteMinY = limoniteMinYDefault;
        limoniteMaxY = limoniteMaxYDefault;

        malachiteSize = malachiteSizeDefault;
        malachiteChance = malachiteChanceDefault;
        malachiteDimBlacklist = malachiteDimBlacklistDefault;
        malachiteMinY = malachiteMinYDefault;
        malachiteMaxY = malachiteMaxYDefault;

        azuriteSize = azuriteSizeDefault;
        azuriteChance = azuriteChanceDefault;
        azuriteDimBlacklist = azuriteDimBlacklistDefault;
        azuriteMinY = azuriteMinYDefault;
        azuriteMaxY = azuriteMaxYDefault;

        cassiteriteSize = cassiteriteSizeDefault;
        cassiteriteChance = cassiteriteChanceDefault;
        cassiteriteDimBlacklist = cassiteriteDimBlacklistDefault;
        cassiteriteMinY = cassiteriteMinYDefault;
        cassiteriteMaxY = cassiteriteMaxYDefault;

        tealliteSize = tealliteSizeDefault;
        tealliteChance = tealliteChanceDefault;
        tealliteDimBlacklist = tealliteDimBlacklistDefault;
        tealliteMinY = tealliteMinYDefault;
        tealliteMaxY = tealliteMaxYDefault;

        galenaSize = galenaSizeDefault;
        galenaChance = galenaChanceDefault;
        galenaDimBlacklist = galenaDimBlacklistDefault;
        galenaMinY = galenaMinYDefault;
        galenaMaxY = galenaMaxYDefault;

        bauxiteSize = bauxiteSizeDefault;
        bauxiteChance = bauxiteChanceDefault;
        bauxiteDimBlacklist = bauxiteDimBlacklistDefault;
        bauxiteMinY = bauxiteMinYDefault;
        bauxiteMaxY = bauxiteMaxYDefault;

        platinumSize = platinumSizeDefault;
        platinumChance = platinumChanceDefault;
        platinumDimBlacklist = platinumDimBlacklistDefault;
        platinumMinY = platinumMinYDefault;
        platinumMaxY = platinumMaxYDefault;

        autuniteSize = autuniteSizeDefault;
        autuniteChance = autuniteChanceDefault;
        autuniteDimBlacklist = autuniteDimBlacklistDefault;
        autuniteMinY = autuniteMinYDefault;
        autuniteMaxY = autuniteMaxYDefault;

        sphaleriteSize = sphaleriteSizeDefault;
        sphaleriteChance = sphaleriteChanceDefault;
        sphaleriteDimBlacklist = sphaleriteDimBlacklistDefault;
        sphaleriteMinY = sphaleriteMinYDefault;
        sphaleriteMaxY = sphaleriteMaxYDefault;
    }
}
