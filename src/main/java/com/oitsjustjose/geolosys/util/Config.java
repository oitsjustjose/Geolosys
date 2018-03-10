package com.oitsjustjose.geolosys.util;

import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.Geolosys;
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
    public boolean enableOsmiumExclusively;
    public boolean enableYellorium;
    public boolean enableSulfur;
    public boolean enableIngots;
    public boolean enableCoals;
    public boolean enableProPick;
    public int proPickRange;
    public boolean enableSmelting;
    public boolean registerAsBauxite;
    public String[] replacementMatsRaw;

    // Sample Stuff
    public int maxSamples;
    public boolean generateSamplesInWater;
    public boolean boringSamples;

    // User Entries
    public String[] userOreEntriesRaw;
    public String[] userStoneEntriesRaw;

    private static Config instance;

    public Config(File configFile)
    {
        if (config == null)
        {
            config = new Configuration(configFile, null, true);
            loadConfiguration();
        }
        instance = this;
    }

    void loadConfiguration()
    {
        Property property;

        String category = "Feature Control";
        List<String> propertyOrder = Lists.newArrayList();
        FeatureControl = config.getCategory(category);
        FeatureControl.setComment("Control which features are enabled:");

        property = config.get(category, "Replace Stone Variant Deposits", true).setRequiresMcRestart(true);
        modStones = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Yellorium", true);
        enableYellorium = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Osmium", true);
        enableOsmium = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Osmium Exclusively (without platinum)", false);
        enableOsmiumExclusively = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Sulfur from Coal", true);
        enableSulfur = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Ingots", true).setRequiresMcRestart(true);
        enableIngots = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Coals", true).setRequiresMcRestart(true);
        enableCoals = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Prospector's Pick", true).setRequiresMcRestart(true);
        enableProPick = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Prospector's Pick Range", 5).setRequiresMcRestart(false);
        property.setMinValue(1);
        property.setMaxValue(255);
        proPickRange = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Cluster Smelting", true).setRequiresMcRestart(true);
        enableSmelting = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Register Aluminum Cluster as oreBauxite", false).setRequiresMcRestart(true);
        registerAsBauxite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Blocks mineral deposits can replace", new String[]{"minecraft:stone:0", "minecraft:stone:1", "minecraft:stone:2", "minecraft:stone:3", "minecraft:dirt:0"});
        replacementMatsRaw = property.getStringList();
        propertyOrder.add(property.getName());

        FeatureControl.setPropertyOrder(propertyOrder);

        // Sample settings
        category = "Ore Samples";
        propertyOrder = Lists.newArrayList();
        Samples = config.getCategory(category);
        Samples.setComment("Settings strictly regarding samples");

        property = config.get(category, "Maximum Number of Samples per Chunk", 10);
        property.setMinValue(1);
        property.setMaxValue(16);
        maxSamples = property.getInt();
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
            Geolosys.getInstance().configParser.parseOres();
            Geolosys.getInstance().configParser.parseStones();
            Geolosys.getInstance().configParser.parsePredicates();
        }
    }

    public static Config getInstance()
    {
        return instance;
    }
}