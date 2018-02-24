package com.oitsjustjose.geolosys.util;

public class ConfigOres
{
    public int coalSize;
    public int coalChance;
    public int[] coalDimBlacklist;

    public int cinnabarSize;
    public int cinnabarChance;
    public int[] cinnabarDimBlacklist;

    public int goldSize;
    public int goldChance;
    public int[] goldDimBlacklist;

    public int lapisSize;
    public int lapisChance;
    public int[] lapisDimBlacklist;

    public int quartzSize;
    public int quartzChance;
    public int[] quartzDimBlacklist;

    public int kimberliteSize;
    public int kimberliteChance;
    public int[] kimberliteDimBlacklist;

    public int berylSize;
    public int berylChance;
    public int[] berylDimBlacklist;

    public int hematiteSize;
    public int hematiteChance;
    public int[] hematiteDimBlacklist;

    public int limoniteSize;
    public int limoniteChance;
    public int[] limoniteDimBlacklist;

    public int malachiteSize;
    public int malachiteChance;
    public int[] malachiteDimBlacklist;

    public int azuriteSize;
    public int azuriteChance;
    public int[] azuriteDimBlacklist;

    public int cassiteriteSize;
    public int cassiteriteChance;
    public int[] cassiteriteDimBlacklist;

    public int tealliteSize;
    public int tealliteChance;
    public int[] tealliteDimBlacklist;

    public int galenaSize;
    public int galenaChance;
    public int[] galentaDimBlacklist;

    public int bauxiteSize;
    public int bauxiteChance;
    public int[] bauxiteDimBlacklist;

    public int platinumSize;
    public int platinumChance;
    public int[] platinumDimBlacklist;

    public int autuniteSize;
    public int autuniteChance;
    public int[] autuniteDimBlacklist;

    public int sphaleriteSize;
    public int sphaleriteChance;
    public int[] sphaleriteDimBlacklist;

    public void populateConfigs()
    {
        coalSize = 64;
        coalChance = 8;
        coalDimBlacklist = new int[]{-1, 1};

        cinnabarSize = 56;
        cinnabarChance = 3;
        cinnabarDimBlacklist = new int[]{-1, 1};

        goldSize = 40;
        goldChance = 3;
        goldDimBlacklist = new int[]{-1, 1};

        lapisSize = 32;
        lapisChance = 4;
        lapisDimBlacklist = new int[]{-1, 1};

        quartzSize = 40;
        quartzChance = 6;
        quartzDimBlacklist = new int[]{-1, 1};

        kimberliteSize = 20;
        kimberliteChance = 4;
        kimberliteDimBlacklist = new int[]{-1, 1};

        berylSize = 16;
        berylChance = 3;
        berylDimBlacklist = new int[]{-1, 1};

        hematiteSize = 24;
        hematiteChance = 7;
        hematiteDimBlacklist = new int[]{-1, 1};

        limoniteSize = 80;
        limoniteChance = 6;
        limoniteDimBlacklist = new int[]{-1, 1};

        malachiteSize = 24;
        malachiteChance = 4;
        malachiteDimBlacklist = new int[]{-1, 1};

        azuriteSize = 80;
        azuriteChance = 7;
        azuriteDimBlacklist = new int[]{-1, 1};

        cassiteriteSize = 24;
        cassiteriteChance = 3;
        cassiteriteDimBlacklist = new int[]{-1, 1};

        tealliteSize = 80;
        tealliteChance = 7;
        tealliteDimBlacklist = new int[]{-1, 1};

        galenaSize = 72;
        galenaChance = 9;
        galentaDimBlacklist = new int[]{-1, 1};

        bauxiteSize = 64;
        bauxiteChance = 8;
        bauxiteDimBlacklist = new int[]{-1, 1};

        platinumSize = 32;
        platinumChance = 4;
        platinumDimBlacklist = new int[]{-1, 1};

        autuniteSize = 24;
        autuniteChance = 5;
        autuniteDimBlacklist = new int[]{-1, 1};

        sphaleriteSize = 24;
        sphaleriteChance = 7;
        sphaleriteDimBlacklist = new int[]{-1, 1};
    }
}
