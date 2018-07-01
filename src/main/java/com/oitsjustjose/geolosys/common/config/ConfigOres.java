package com.oitsjustjose.geolosys.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

public class ConfigOres
{
    public Ore coal;
    public Ore cinnabar;
    public Ore gold;
    public Ore lapis;
    public Ore quartz;
    public Ore kimberlite;
    public Ore beryl;
    public Ore hematite;
    public Ore limonite;
    public Ore malachite;
    public Ore azurite;
    public Ore cassiterite;
    public Ore teallite;
    public Ore galena;
    public Ore bauxite;
    public Ore platinum;
    public Ore autunite;
    public Ore sphalerite;

    public void populateConfigs()
    {
        coal = new ConfigOres.Ore(8, 78, 8, 64, new int[]{-1, 1});
        cinnabar = new ConfigOres.Ore(5, 12, 6, 40, new int[]{-1, 1});
        gold = new ConfigOres.Ore(5, 30, 4, 40, new int[]{-1, 1});
        lapis = new ConfigOres.Ore(10, 24, 5, 32, new int[]{-1, 1});
        quartz = new ConfigOres.Ore(6, 29, 6, 40, new int[]{-1, 1});
        kimberlite = new ConfigOres.Ore(2, 15, 3, 20, new int[]{-1, 1});
        beryl = new ConfigOres.Ore(4, 32, 3, 16, new int[]{-1, 1});
        hematite = new ConfigOres.Ore(32, 60, 6, 24, new int[]{-1, 1});
        limonite = new ConfigOres.Ore(6, 32, 5, 80, new int[]{-1, 1});
        malachite = new ConfigOres.Ore(39, 44, 6, 24, new int[]{-1, 1});
        azurite = new ConfigOres.Ore(12, 44, 5, 80, new int[]{-1, 1});
        cassiterite = new ConfigOres.Ore(44, 68, 6, 24, new int[]{-1, 1});
        teallite = new ConfigOres.Ore(8, 43, 5, 80, new int[]{-1, 1});
        galena = new ConfigOres.Ore(16, 50, 5, 72, new int[]{-1, 1});
        bauxite = new ConfigOres.Ore(45, 70, 4, 64, new int[]{-1, 1});
        platinum = new ConfigOres.Ore(3, 25, 4, 32, new int[]{-1, 1});
        autunite = new ConfigOres.Ore(8, 33, 5, 24, new int[]{-1, 1});
        sphalerite = new ConfigOres.Ore(35, 55, 4, 24, new int[]{-1, 1});
    }

    public void validate(File configDir)
    {
        boolean dirty = false;
        for (Field f : this.getClass().getFields())
        {
            try
            {
                Field field = this.getClass().getField(f.getName());
                Ore val = (Ore) field.get(this);
                // If the value hasn't been initialized....
                if (val == null)
                {
                    f.set(this, new ConfigOres.Ore(0, 0, 0, 0, new int[]{}));
                    dirty = true;
                }
            }
            catch (IllegalAccessException | NoSuchFieldException ignored)
            {
            }
        }
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

    public static class Ore
    {
        private int minY;
        private int maxY;
        private int chance;
        private int size;
        private int[] blacklist;

        public Ore(int minY, int maxY, int chance, int size, int[] blacklist)
        {
            this.minY = minY;
            this.maxY = maxY;
            this.chance = chance;
            this.size = size;
            this.blacklist = blacklist;
        }

        public int getMinY()
        {
            return this.minY;
        }

        public int getMaxY()
        {
            return this.maxY;
        }

        public int getChance()
        {
            return this.chance;
        }

        public int getSize()
        {
            return this.size;
        }

        public int[] getBlacklist()
        {
            return this.blacklist;
        }
    }
}
