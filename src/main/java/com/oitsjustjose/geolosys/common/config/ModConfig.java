package com.oitsjustjose.geolosys.common.config;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.client.GuiManual;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Geolosys.MODID)
public class ModConfig
{
    @Config.Name("Feature Control")
    @Config.Comment("Enable or disable Geolosys features entirely")
    public static FeatureControl featureControl = new FeatureControl();

    @Config.Name("Prospecting")
    @Config.Comment("Adjust settings specific to prospecting")
    public static Prospecting prospecting = new Prospecting();

    @Config.Name("User Entries")
    @Config.Comment("Custom user entries")
    public static UserEntries userEntries = new UserEntries();

    @Config.Name("Client")
    @Config.Comment("Client-side settings")
    public static Client client = new Client();

    public static class FeatureControl
    {
        @Config.Name("Replace Stone Variant Deposits")
        public boolean modStones = true;

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

        @Config.Name("Enable Cluster Smelting")
        public boolean enableSmelting = true;

        @Config.Name("Register Aluminum as oreBauxite")
        public boolean registerAsBauxite = false;

        @Config.Name("Enable debug print statements for generation")
        public boolean debugGeneration = false;

        @Config.Name("Enable IE Integration")
        public boolean enableIECompat = true;

        @Config.Name("Retroactively replace existing ores in world")
        @Config.Comment("Happens when a player enters a chunk; changes other mod ores into Geolosys's")
        public boolean retroReplace = true;

        @Config.Name("Disable Vanilla Ore Generation")
        public boolean disableVanillaGeneration = true;

        @Config.Name("Vanilla Mode")
        @Config.Comment("When enabled, instead of using Geolosys's replacements for vanilla ores it just uses Vanilla blocks")
        @Config.RequiresMcRestart
        public boolean vanillaMode = false;
    }

    public static class Prospecting
    {
        @Config.Name("Maximum Number of samples per Chunk")
        @Config.RangeInt(min = 1, max = 16)
        public int maxSamples = 10;

        @Config.Name("Allow samples to generate in any water")
        public boolean generateInWater = false;

        @Config.Name("Samples drop nothing (contents revealed in chat)")
        public boolean boringSamples = false;

        @Config.Name("Enable Prospector's Pickaxe")
        public boolean enableProPick = true;

        @Config.Name("Prospector's Pick Takes Damage")
        public boolean enableProPickDamage = false;

        @Config.Name("Prospector's Pick Durability")
        @Config.RangeInt(min = 0)
        public int proPickDurability = 256;

        @Config.Name("Prospector's Pickaxe Range")
        @Config.RangeInt(min = 0, max = 255)
        public int proPickRange = 5;

        @Config.Name("Prospector's Pickaxe Diameter")
        @Config.RangeInt(min = 0, max = 255)
        public int proPickDiameter = 5;

        @Config.Name("Prospector's Pick Sea Levels per Dimension")
        @Config.Comment("The prospector's pick will show what's in a direction only when below this level, per dimension. Add custom dimension ID's and their corresponding Y depth below, format: <dim_id>:<y_level>")
        public String[] proPickDimensionSeaLevels = getDefaultSeaLevels();

        @Config.Name("Surface Prospecting Results")
        @Config.Comment("SAMPLES means prospecting on the surface returns the samples found\n"
                + "OREBLOCKS means prospecting on the surface returns the first Geolosys-registered Ore Block it finds")
        public SURFACE_PROSPECTING_TYPE surfaceProspectingResults = SURFACE_PROSPECTING_TYPE.SAMPLES;

        @Config.Name("Blocks Samples can Generate On")
        @Config.Comment("Formatted <modid:block:meta>; this list contains blocks that samples should not generate on.")
        public String[] samplePlaceBlacklist = new String[]
        { "minecraft:ice:0", "minecraft:packed_ice:0", "minecraft:frosted_ice" };

        public enum SURFACE_PROSPECTING_TYPE
        {
            SAMPLES, OREBLOCKS;
        }
    }

    public static class UserEntries
    {
        @Config.Name("Custom Ore Entries")
        @Config.Comment("Format is:\n"
                + "modid:block:meta, deposit size, min Y, max Y, chance to gen in chunk, block to use as sample, [dimension blacklist] Example:\n"
                + "actuallyadditions:block_misc:3, 32, 13, 42, 20, actuallyadditions:block_misc:1, [1, -1]")
        public String[] userOreEntriesRaw = new String[]
        {};

        @Config.Name("Custom Stone Entries")
        @Config.Comment("Format is:\n" + "modid:block:meta, min Y, max Y, chance to gen in chunk\n"
                + "ALL DEPOSITS ARE APPROX. THE SAME SIZE & AREN'T CONFIGURABLE.\n" + "rustic:slate:0, 27, 54, 10")
        public String[] userStoneEntriesRaw = new String[]
        {};

        @Config.Name("Blocks mineral deposits can replace")
        @Config.Comment("Format is:\n" + "modid:block OR modid:block:meta")
        public String[] replacementMatsRaw = new String[]
        { "minecraft:stone:0", "minecraft:stone:1", "minecraft:stone:3", "minecraft:stone:5", "minecraft:dirt:0",
                "minecraft:netherrack:0" };

        @Config.Name("Blocks that the OreConverter feature should ignore")
        @Config.Comment("Format is:\n" + "modid:block OR modid:block:meta")
        public String[] convertBlacklistRaw = getConvertBlacklist();
    }

    public static class Client
    {
        @Config.Name("Field Manual Font Scale")
        @Config.RangeDouble(min = 0.1, max = 3.0)
        public float manualFontScale = 0.85F;

        @Config.Name("Prospector's Pick Depth HUD X")
        @Config.Comment("The X coordinate that the Depth overlay displays at while holding a prospector's pick")
        @Config.RangeInt(min = 0)
        public int hudX = 2;

        @Config.Name("Prospector's Pick Depth HUD Y")
        @Config.Comment("The Y coordinate that the Depth overlay displays at while holding a prospector's pick")
        @Config.RangeInt(min = 0)
        public int hudY = 2;
    }

    @Mod.EventBusSubscriber(modid = Geolosys.MODID)
    public static class EventHandler
    {
        @SubscribeEvent
        public void onChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equalsIgnoreCase(Geolosys.MODID))
            {
                ConfigManager.sync(Geolosys.MODID, Config.Type.INSTANCE);
                ConfigParser.reinit();
                GuiManual.initPages();
            }
        }
    }

    private static String[] getDefaultSeaLevels()
    {
        String[] ret = new String[]
        { "-1:128", "0:64", "1:128" };

        return ret;
    }

    private static String[] getConvertBlacklist()
    {
        if (Loader.isModLoaded("nex"))
        {
            if (Loader.isModLoaded("gravelores"))
            {
                return new String[]
                { "gravelores:coal_gravel_ore", "gravelores:iron_gravel_ore", "gravelores:lapis_gravel_ore",
                        "gravelores:gold_gravel_ore", "gravelores:redstone_gravel_ore", "gravelores:diamond_gravel_ore",
                        "gravelores:emerald_gravel_ore", "gravelores:tin_gravel_ore", "gravelores:nickel_gravel_ore",
                        "gravelores:silver_gravel_ore", "gravelores:lead_gravel_ore", "gravelores:copper_gravel_ore",
                        "gravelores:aluminum_gravel_ore", "nex:ore_quartz:0", "nex:ore_quartz:1", "nex:ore_quartz:2",
                        "nex:ore_quartz:3" };
            }
            else
            {
                return new String[]
                { "nex:ore_quartz:0", "nex:ore_quartz:1", "nex:ore_quartz:2", "nex:ore_quartz:3" };
            }
        }
        else if (Loader.isModLoaded("gravelores"))
        {
            return new String[]
            { "gravelores:coal_gravel_ore", "gravelores:iron_gravel_ore", "gravelores:lapis_gravel_ore",
                    "gravelores:gold_gravel_ore", "gravelores:redstone_gravel_ore", "gravelores:diamond_gravel_ore",
                    "gravelores:emerald_gravel_ore", "gravelores:tin_gravel_ore", "gravelores:nickel_gravel_ore",
                    "gravelores:silver_gravel_ore", "gravelores:lead_gravel_ore", "gravelores:copper_gravel_ore",
                    "gravelores:aluminum_gravel_ore",

            };
        }
        return new String[]
        {};
    }
}
