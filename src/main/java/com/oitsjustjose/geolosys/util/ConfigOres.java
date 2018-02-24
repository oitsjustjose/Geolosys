package com.oitsjustjose.geolosys.util;

public class ConfigOres
{
    public int coalSize;
    public int coalChance;
    public int[] coalDimBlacklist;
    public int coalMinY;
    public int coalMaxY;

    public int cinnabarSize;
    public int cinnabarChance;
    public int[] cinnabarDimBlacklist;
    public int cinnabarMinY;
    public int cinnabarMaxY;

    public int goldSize;
    public int goldChance;
    public int[] goldDimBlacklist;
    public int goldMinY;
    public int goldMaxY;

    public int lapisSize;
    public int lapisChance;
    public int[] lapisDimBlacklist;
    public int lapisMinY;
    public int lapisMaxY;

    public int quartzSize;
    public int quartzChance;
    public int[] quartzDimBlacklist;
    public int quartzMinY;
    public int quartzMaxY;

    public int kimberliteSize;
    public int kimberliteChance;
    public int[] kimberliteDimBlacklist;
    public int kimberliteMinY;
    public int kimberliteMaxY;

    public int berylSize;
    public int berylChance;
    public int[] berylDimBlacklist;
    public int berylMinY;
    public int berylMaxY;

    public int hematiteSize;
    public int hematiteChance;
    public int[] hematiteDimBlacklist;
    public int hematiteMinY;
    public int hematiteMaxY;

    public int limoniteSize;
    public int limoniteChance;
    public int[] limoniteDimBlacklist;
    public int limoniteMinY;
    public int limoniteMaxY;

    public int malachiteSize;
    public int malachiteChance;
    public int[] malachiteDimBlacklist;
    public int malachiteMinY;
    public int malachiteMaxY;

    public int azuriteSize;
    public int azuriteChance;
    public int[] azuriteDimBlacklist;
    public int azuriteMinY;
    public int azuriteMaxY;

    public int cassiteriteSize;
    public int cassiteriteChance;
    public int[] cassiteriteDimBlacklist;
    public int cassiteriteMinY;
    public int cassiteriteMaxY;

    public int tealliteSize;
    public int tealliteChance;
    public int[] tealliteDimBlacklist;
    public int tealliteMinY;
    public int tealliteMaxY;

    public int galenaSize;
    public int galenaChance;
    public int[] galenaDimBlacklist;
    public int galenaMinY;
    public int galenaMaxY;

    public int bauxiteSize;
    public int bauxiteChance;
    public int[] bauxiteDimBlacklist;
    public int bauxiteMinY;
    public int bauxiteMaxY;

    public int platinumSize;
    public int platinumChance;
    public int[] platinumDimBlacklist;
    public int platinumMinY;
    public int platinumMaxY;

    public int autuniteSize;
    public int autuniteChance;
    public int[] autuniteDimBlacklist;
    public int autuniteMinY;
    public int autuniteMaxY;

    public int sphaleriteSize;
    public int sphaleriteChance;
    public int[] sphaleriteDimBlacklist;
    public int sphaleriteMinY;
    public int sphaleriteMaxY;

    public void populateConfigs()
    {
        coalSize = 64;
        coalChance = 8;
        coalDimBlacklist = new int[]{-1, 1};
        coalMinY = 48;
        coalMaxY = 78;

        cinnabarSize = 56;
        cinnabarChance = 3;
        cinnabarDimBlacklist = new int[]{-1, 1};
        cinnabarMinY = 5;
        cinnabarMaxY = 12;

        goldSize = 40;
        goldChance = 3;
        goldDimBlacklist = new int[]{-1, 1};
        goldMinY = 5;
        goldMaxY = 30;

        lapisSize = 32;
        lapisChance = 4;
        lapisDimBlacklist = new int[]{-1, 1};
        lapisMinY = 10;
        lapisMaxY = 24;

        quartzSize = 40;
        quartzChance = 6;
        quartzDimBlacklist = new int[]{-1, 1};
        quartzMinY = 6;
        quartzMaxY = 29;

        kimberliteSize = 20;
        kimberliteChance = 4;
        kimberliteDimBlacklist = new int[]{-1, 1};
        kimberliteMinY = 2;
        kimberliteMaxY = 15;

        berylSize = 16;
        berylChance = 3;
        berylDimBlacklist = new int[]{-1, 1};
        berylMinY = 4;
        berylMaxY = 32;

        hematiteSize = 24;
        hematiteChance = 7;
        hematiteDimBlacklist = new int[]{-1, 1};
        hematiteMinY = 32;
        hematiteMaxY = 60;

        limoniteSize = 80;
        limoniteChance = 6;
        limoniteDimBlacklist = new int[]{-1, 1};
        limoniteMinY = 6;
        limoniteMaxY = 32;

        malachiteSize = 24;
        malachiteChance = 4;
        malachiteDimBlacklist = new int[]{-1, 1};
        malachiteMinY = 39;
        malachiteMaxY = 44;

        azuriteSize = 80;
        azuriteChance = 7;
        azuriteDimBlacklist = new int[]{-1, 1};
        azuriteMinY = 12;
        azuriteMaxY = 44;

        cassiteriteSize = 24;
        cassiteriteChance = 3;
        cassiteriteDimBlacklist = new int[]{-1, 1};
        cassiteriteMinY = 44;
        cassiteriteMaxY = 68;

        tealliteSize = 80;
        tealliteChance = 7;
        tealliteDimBlacklist = new int[]{-1, 1};
        tealliteMinY = 8;
        tealliteMaxY 43;

        galenaSize = 72;
        galenaChance = 9;
        galenaDimBlacklist = new int[]{-1, 1};
        galenaMinY = 16;
        galenaMaxY = 50;

        bauxiteSize = 64;
        bauxiteChance = 8;
        bauxiteDimBlacklist = new int[]{-1, 1};
        bauxiteMinY = 45;
        bauxiteMaxY = 70;

        platinumSize = 32;
        platinumChance = 4;
        platinumDimBlacklist = new int[]{-1, 1};
        platinumMinY = 3;
        platinumMaxY = 25;

        autuniteSize = 24;
        autuniteChance = 5;
        autuniteDimBlacklist = new int[]{-1, 1};
        autuniteMinY = 8;
        autuniteMaxY = 33;

        sphaleriteSize = 24;
        sphaleriteChance = 7;
        sphaleriteDimBlacklist = new int[]{-1, 1};
        sphaleriteMinY = 35;
        sphaleriteMaxY = 55;
    }
}
