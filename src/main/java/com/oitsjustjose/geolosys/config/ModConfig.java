package com.oitsjustjose.geolosys.config;


import com.oitsjustjose.geolosys.Geolosys;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Geolosys.MODID)
public class ModConfig
{
    @Config.Name("Feature Control")
    @Config.Comment("Enable or disable Geolosys features entirely")
    public static FeatureControl featureControl = new FeatureControl();

    @Config.Name("Samples")
    @Config.Comment("Adjust settings specific to samples")
    public static Samples samples = new Samples();

    @Config.Name("User Entries")
    @Config.Comment("Custom user entries")
    public static UserEntries userEntries = new UserEntries();

    public static class FeatureControl
    {
        @Config.Name("Replace Stone Variant Deposits")
        public boolean modStones = true;

        @Config.Name("Replace non-Geolosys ore drops")
        public boolean modDrops = true;

        @Config.Name("Enable Osmium")
        public boolean enableOsmium = true;

        @Config.Name("Enable Osmium Exclusively")
        @Config.Comment("Allows Osmium to be enabled, without enabling Platinum")
        public boolean enableOsmiumExclusively = false;

        @Config.Name("Enable Yellorium")
        public boolean enableYellorium = true;

        @Config.Name("Enable Sulfur")
        public boolean enableSulfur = true;

        @Config.Name("Enable Ingots")
        public boolean enableIngots = true;

        @Config.Name("Enable Coals")
        public boolean enableCoals = true;

        @Config.Name("Enable Prospector's Pickaxe")
        public boolean enableProPick = true;

        @Config.Name("Prospector's Pickaxe Range")
        @Config.RangeInt(min = 0, max = 255)
        public int proPickRange = 5;

        @Config.Name("Prospector's Pickaxe Diameter")
        @Config.RangeInt(min = 0, max = 255)
        public int proPickDiameter = 5;

        @Config.Name("Enable Extra Compass Features")
        public boolean enableEnhancedCompass = true;

        @Config.Name("Enable Cluster Smelting")
        public boolean enableSmelting = true;

        @Config.Name("Register Aluminum as oreBauxite")
        public boolean registerAsBauxite = false;

        @Config.Name("Enable debug print statements for generation")
        public boolean debugGeneration = false;

        @Config.Name("Enable IE Integration")
        public boolean enableIECompat = true;
    }

    public static class Samples
    {
        @Config.Name("Maximum Number of Samples per Chunk")
        @Config.RangeInt(min = 1, max = 16)
        public int maxSamples = 10;

        @Config.Name("Allow samples to generate in any water")
        public boolean generateInWater = false;

        @Config.Name("Samples drop nothing (contents revealed in chat)")
        public boolean boringSamples = false;
    }

    public static class UserEntries
    {
        @Config.Name("Custom Ore Entries")
        @Config.Comment("Format is:\n" +
                "modid:block:meta, clusterSize, min Y, max Y, chance to gen in chunk. Example:\n" +
                "actuallyadditions:block_misc:3, 32, 13, 42, 20\n" +
                "Optionally you can declare your own \"sample\" block by appending an extra modid:block:meta to the end. Example:\n" +
                "actuallyadditions:block_misc:3, 32, 13, 42, 20, actuallyadditions:block_misc:1\n" +
                "META, COLONS AND COMMAS ARE REQUIRED.")
        public String[] userOreEntriesRaw = new String[]{};

        @Config.Name("Custom Stone Entries")
        @Config.Comment("Format is:\n" +
                "modid:block:meta, min Y, max Y, chance to gen in chunk\n" +
                "ALL CLUSTERS ARE APPROX. THE SAME SIZE & AREN'T CONFIGURABLE.\n" +
                "META, COLONS AND COMMAS ARE REQUIRED. Example:\n" +
                "rustic:slate:0, 27, 54, 10")
        public String[] userStoneEntriesRaw = new String[]{};

        @Config.Name("Blocks mineral deposits can replace")
        public String[] replacementMatsRaw = new String[]{"minecraft:stone:0", "minecraft:stone:1", "minecraft:stone:2", "minecraft:stone:3", "minecraft:dirt:0"};
    }

    @SubscribeEvent
    public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(Geolosys.MODID))
        {
            ConfigManager.sync(Geolosys.MODID, Config.Type.INSTANCE);
        }
    }
}
