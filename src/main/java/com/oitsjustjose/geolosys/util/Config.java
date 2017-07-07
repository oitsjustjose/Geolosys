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
    public ConfigCategory Weights;
    public ConfigCategory Sizes;

    public boolean disableIron;
    public boolean enableHematite;
    public boolean enableLimonite;
    public boolean enableMalachite;
    public boolean enableAzurite;
    public boolean enableCassiterite;
    public boolean enableSphalerite;
    public boolean enableGalena;
    public boolean enableBauxite;

    public int frequencyHematite;
    public int frequencyLimonite;
    public int frequencyMalachite;
    public int frequencyAzurite;
    public int frequencyCassiterite;
    public int frequencySphalerite;
    public int frequencyGalena;
    public int frequencyBauxite;

    public int clusterSizeHematite;
    public int clusterSizeLimonite;
    public int clusterSizeMalachite;
    public int clusterSizeAzurite;
    public int clusterSizeCassiterite;
    public int clusterSizeSphalerite;
    public int clusterSizeGalena;
    public int clusterSizeBauxite;

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
        FeatureControl = config.getCategory(category);
        FeatureControl.setComment("Control which features are enabled:");

        property = config.get(category, "Disable Vanilla Iron Ore Gen", true);
        disableIron = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Hematite", true);
        enableHematite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Limonite", true);
        enableLimonite = property.getBoolean();
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

        property = config.get(category, "Enable Sphalerite", true);
        enableSphalerite = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Galena", true);
        enableGalena = property.getBoolean();
        propertyOrder.add(property.getName());

        property = config.get(category, "Enable Bauxite", true);
        enableBauxite = property.getBoolean();
        propertyOrder.add(property.getName());

        FeatureControl.setPropertyOrder(propertyOrder);

        // Weights
        category = "Ore Gen Weights";
        propertyOrder = Lists.newArrayList();
        Weights = config.getCategory(category);
        Weights.setComment("Fine-tuned adjustments for ore generation rates.");

        property = config.get(category, "Blacklisted Dimensions", new int[]{-1, 1}, "Dimensions that ores CAN'T generate in");
        blacklistedDIMs = property.getIntList();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Hematite veins per chunk", 1);
        frequencyHematite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Limonite veins per chunk", 1);
        frequencyLimonite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Malachite veins per chunk", 1);
        frequencyMalachite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Azurite veins per chunk", 1);
        frequencyAzurite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Cassiterite veins per chunk", 1);
        frequencyCassiterite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Sphalerite veins per chunk", 1);
        frequencySphalerite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Galena veins per chunk", 1);
        frequencyGalena = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "# of Bauxite veins per chunk", 1);
        frequencyBauxite = property.getInt();
        propertyOrder.add(property.getName());



        Weights.setPropertyOrder(propertyOrder);

        // Cluster Sizes
        category = "Ore Cluster Sizes";
        propertyOrder = Lists.newArrayList();
        Sizes = config.getCategory(category);
        Sizes.setComment("The number of ores found in each cluster");

        property = config.get(category, "Hematite Cluster Size", 48);
        clusterSizeHematite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Limonite Cluster Size", 40);
        clusterSizeLimonite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Malachite Cluster Size", 48);
        clusterSizeMalachite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Azurite Cluster Size", 40);
        clusterSizeAzurite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Cassiterite Cluster Size", 48);
        clusterSizeCassiterite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Sphalerite Cluster Size", 40);
        clusterSizeSphalerite = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Galena Cluster Size", 40);
        clusterSizeGalena = property.getInt();
        propertyOrder.add(property.getName());

        property = config.get(category, "Bauxite Cluster Size", 28);
        clusterSizeBauxite = property.getInt();
        propertyOrder.add(property.getName());

        Sizes.setPropertyOrder(propertyOrder);

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