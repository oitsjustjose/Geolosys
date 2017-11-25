package com.oitsjustjose.geolosys.util;

import com.google.common.collect.Lists;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.List;

public class Config
{
    public Configuration config;
    public ConfigCategory FeatureControl;
    public ConfigCategory Chances;
    public ConfigCategory Sizes;
    public ConfigCategory Samples;
    public ConfigCategory UserEntries;

    // Feature Control
    public boolean modIron;
    public boolean modGold;
    public boolean modDiamond;
    public boolean modCoal;
    public boolean modRedstone;
    public boolean modLapis;
    public boolean modQuartz;
    public boolean modStones;
    public boolean enableHematite;
    public boolean enableLimonite;
    public boolean enableNickel;
    public boolean enableMalachite;
    public boolean enableAzurite;
    public boolean enableCassiterite;
    public boolean enableTeallite;
    public boolean enableGalena;
    public boolean enableBauxite;
    public boolean enableAutunite;
    public boolean enablePlatinum;
    public boolean enableSphalerite;
    public boolean enableOsmium;
    public boolean enableYellorium;
    public boolean enableIngots;
    public boolean enableProPick;
    public boolean enableSmelting;
    public boolean registerAsBauxite;

    // Chances (vanilla)
    public int chanceCoal;
    public int chanceCinnabar;
    public int chanceGold;
    public int chanceLapis;
    public int chanceQuartz;
    public int chanceKimberlite;

    // Chances (modded)
    public int chanceHematite;
    public int chanceLimonite;
    public int chanceMalachite;
    public int chanceAzurite;
    public int chanceCassiterite;
    public int chanceTeallite;
    public int chanceGalena;
    public int chanceBauxite;
    public int chancePlatinum;
    public int chanceAutunite;
    public int chanceSphalerite;

    // Sample Stuff
    public int chanceSample;
    public boolean generateSamplesInWater;
    public boolean boringSamples;

    // Sizes
    public int clusterSizeCoal;
    public int clusterSizeCinnabar;
    public int clusterSizeGold;
    public int clusterSizeLapis;
    public int clusterSizeQuartz;
    public int clusterSizeKimberlite;

    public int clusterSizeHematite;
    public int clusterSizeLimonite;
    public int clusterSizeMalachite;
    public int clusterSizeAzurite;
    public int clusterSizeCassiterite;
    public int clusterSizeTeallite;
    public int clusterSizeGalena;
    public int clusterSizeBauxite;
    public int clusterSizePlatinum;
    public int clusterSizeAutunite;
    public int clusterSizeSphalerite;
    // User Entries
    public String[] userOreEntriesRaw;
    public String[] userStoneEntriesRaw;

    public int[] blacklistedDIMs;

    public Config(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile, null, true);
            loadConfiguration();
        }
    }

    void loadConfiguration()
    {
        Property property;

        String category = "Feature Control";
        List<String> propertyOrder = Lists.newArrayList();
        FeatureControl = config.getCategory(category).setRequiresMcRestart(true);
        FeatureControl.setComment("Control which features are enabled:");

        property = config.get(category, "Replace Vanilla Iron Ore Gen", true);
        modIron = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Gold Deposits", true);
        modGold = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Diamond Deposits with Kimberlite", true);
        modDiamond = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Coal Deposits", true);
        modCoal = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Redstone Deposits with Cinnabar", true);
        modRedstone = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Lapis Deposits", true);
        modLapis = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Quartz Deposits", true);
        property.setComment("Also adds Certus and Black Quartz drops");
        modQuartz = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Replace Stone Variant Deposits", true);
        modStones = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Hematite", true);
        enableHematite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Limonite", true);
        enableLimonite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Nickel Drops from Limonite", true);
        enableNickel = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Malachite", true);
        enableMalachite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Azurite", true);
        enableAzurite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Cassiterite", true);
        enableCassiterite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Teallite", true);
        enableTeallite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Galena", true);
        enableGalena = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Bauxite", true);
        enableBauxite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Autunite", true);
        enableAutunite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Platinum", true);
        enablePlatinum = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Sphalerite", true);
        enableSphalerite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Yellorium", true);
        enableYellorium = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Osmium", true);
        enableOsmium = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Ingot", true);
        property.setComment("Set to \"False\" if other mods already provide all necessary ORE variants.");
        enableIngots = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Prospector's Pick", true);
        property.setComment("Set to \"False\" if you don't wish to have this feature, or have another mod doing it");
        enableProPick = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Cluster Smelting", true);
        property.setComment("Set to \"False\" if you don't want smelting automatically initialized");
        enableSmelting = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Also register Aluminum Cluster as oreBauxite", false);
        property.setComment("Meant as a layer of compatibility for mods like TechReborn. Adds \"oreBauxite\" as one of the entries for the Aluminum Cluster");
        registerAsBauxite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Blacklisted Dimensions", new int[]{-1, 1}, "Dimensions that ores CAN'T generate in");
        blacklistedDIMs = property.getIntList();
        propertyOrder.add(property.getName());

        FeatureControl.setPropertyOrder(propertyOrder);

        category = "Ore Gen Chances";
        propertyOrder = Lists.newArrayList();
        Chances = config.getCategory(category).setRequiresMcRestart(true);
        Chances.setComment("The % chance for a pluton to generate in a chunk");

        property = config.get(category, "Coal Pluton Chance Per Chunk", 8);
        chanceCoal = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Cinnabar Pluton Chance Per Chunk", 3);
        chanceCinnabar = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Gold Pluton Chance Per Chunk", 3);
        chanceGold = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Lapis Pluton Chance Per Chunk", 4);
        chanceLapis = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Quartz Pluton Chance Per Chunk", 6);
        chanceQuartz = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Kimberlite Pluton Chance Per Chunk", 4);
        chanceKimberlite = property.getInt();
        propertyOrder.add(property.getName());


        property = config.get(category, "Hematite Pluton Chance Per Chunk", 4);
        chanceHematite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Limonite Pluton Chance Per Chunk", 6);
        chanceLimonite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Malachite Pluton Chance Per Chunk", 4);
        chanceMalachite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Azurite Pluton Chance Per Chunk", 7);
        chanceAzurite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Cassiterite Pluton Chance Per Chunk", 3);
        chanceCassiterite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Teallite Pluton Chance Per Chunk", 7);
        chanceTeallite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Galena Pluton Chance Per Chunk", 10);
        chanceGalena = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Bauxite Pluton Chance Per Chunk", 9);
        chanceBauxite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Platinum Pluton Chance Per Chunk", 4);
        chancePlatinum = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Autunite Pluton Chance Per Chunk", 5);
        chanceAutunite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Sphalerite Pluton Chance Per Chunk", 7);
        chanceSphalerite = property.getInt();
        propertyOrder.add(property.getName());

        Chances.setPropertyOrder(propertyOrder);

        // Cluster Sizes
        category = "Ore Cluster Sizes";
        propertyOrder = Lists.newArrayList();
        Sizes = config.getCategory(category).setRequiresMcRestart(true);
        Sizes.setComment("The number of ores found in each CLUSTER");

        property = config.get(category, "Coal Cluster Size", 64);
        clusterSizeCoal = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Cinnabar Cluster Size", 56);
        clusterSizeCinnabar = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Gold Cluster Size", 40);
        clusterSizeGold = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Lapis Cluster Size", 32);
        clusterSizeLapis = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Quartz Cluster Size", 40);
        clusterSizeQuartz = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Kimberlite Cluster Size", 20);
        clusterSizeKimberlite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Hematite Cluster Size", 24);
        clusterSizeHematite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Limonite Cluster Size", 80);
        clusterSizeLimonite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Malachite Cluster Size", 24);
        clusterSizeMalachite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Azurite Cluster Size", 80);
        clusterSizeAzurite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Cassiterite Cluster Size", 24);
        clusterSizeCassiterite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Teallite Cluster Size", 80);
        clusterSizeTeallite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Galena Cluster Size", 72);
        clusterSizeGalena = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Bauxite Cluster Size", 64);
        clusterSizeBauxite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Platinum Cluster Size", 32);
        clusterSizePlatinum = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Autunite Cluster Size", 24);
        clusterSizeAutunite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Sphalerite Cluster Size", 24);
        clusterSizeSphalerite = property.getInt();
        propertyOrder.add(property.getName());

        Sizes.setPropertyOrder(propertyOrder);

        // Sample settings
        category = "Ore Samples";
        propertyOrder = Lists.newArrayList();
        Samples = config.getCategory(category).setRequiresMcRestart(true);
        Samples.setComment("Settings strictly regarding samples");

        property = config.get(category, "Random Chance of Samples per Chunk", 4, "The maximum number of samples that can generate per chunk", 1, 16);
        chanceSample = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Allow samples to spawn in water (shallow or deep)", false);
        generateSamplesInWater = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Samples drop nothing, instead reveal their contents via chat", false);
        boringSamples = property.getBoolean();
        propertyOrder.add(property.getName());

        Samples.setPropertyOrder(propertyOrder);

        category = "User Entries";
        propertyOrder = Lists.newArrayList();
        UserEntries = config.getCategory(category).setRequiresMcRestart(true);
        UserEntries.setComment("It is STRONGLY suggested you use the ConfigGUI for this.");

        property = config.get(category, "Custom Ore Entries", new String[]{});
        property.setComment("Format is:\n" +
                "modid:block:meta, clusterSize, min Y, max Y, chance to gen in chunk. Example:\n" +
                "actuallyadditions:block_misc:3, 32, 13, 42, 20\n" +
                "Optionally you can declare your own \"sample\" block by appending an extra modid:block:meta to the end. Example:\n" +
                "actuallyadditions:block_misc:3, 32, 13, 42, 20, actuallyadditions:block_misc:1\n" +
                "META, COLONS AND COMMAS ARE REQUIRED.");
        userOreEntriesRaw = property.getStringList();
        propertyOrder.add(property.getName());

        property = config.get(category, "Custom Stone Entries", new String[]{});
        property.setComment("Format is:\n" +
                "modid:block:meta, min Y, max Y, chance to gen in chunk\n" +
                "ALL CLUSTERS ARE APPROX. THE SAME SIZE & AREN'T CONFIGURABLE.\n" +
                "META, COLONS AND COMMAS ARE REQUIRED. Example:\n" +
                "rustic:slate:0, 27, 54, 10");
        userStoneEntriesRaw = property.getStringList();
        propertyOrder.add(property.getName());

        UserEntries.setPropertyOrder(propertyOrder);

        if (config.hasChanged())
        {
            config.save();
        }
    }

    @SubscribeEvent
    public void update(OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Lib.MODID))
        {
            loadConfiguration();
        }
    }
}