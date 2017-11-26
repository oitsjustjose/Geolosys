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
    public ConfigCategory Samples;
    public ConfigCategory UserEntries;

    // Feature Control
    public boolean modStones;
    public boolean enableOsmium;
    public boolean enableYellorium;
    public boolean enableIngots;
    public boolean enableProPick;
    public boolean enableSmelting;
    public boolean registerAsBauxite;

    // Sample Stuff
    public int chanceSample;
    public boolean generateSamplesInWater;
    public boolean boringSamples;

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

        property = config.get(category, "Replace Stone Variant Deposits", true);
        modStones = property.getBoolean();
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
            loadConfiguration();
    }
}