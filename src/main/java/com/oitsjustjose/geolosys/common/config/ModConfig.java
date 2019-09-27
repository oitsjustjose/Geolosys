package com.oitsjustjose.geolosys.common.config;

import java.nio.file.Path;
import java.util.function.Predicate;

import com.electronwill.nightconfig.core.conversion.SpecStringInArray;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.ArrayInput;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.oitsjustjose.geolosys.client.ConfigClient;
import com.oitsjustjose.geolosys.common.compat.ConfigCompat;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModConfig
{
        public static final ForgeConfigSpec COMMON_CONFIG;
        private static final Builder COMMON_BUILDER = new Builder();
        public static ForgeConfigSpec.BooleanValue REPLACE_STONES;
        public static ForgeConfigSpec.BooleanValue ENABLE_INGOTS;
        public static ForgeConfigSpec.BooleanValue ENABLE_COALS;
        public static ForgeConfigSpec.BooleanValue ENABLE_SMELTING;
        public static ForgeConfigSpec.BooleanValue DEBUG_WORLD_GEN;
        public static ForgeConfigSpec.BooleanValue RETRO_REPLACE;
        public static ForgeConfigSpec.BooleanValue DISABLE_VANILLA_ORE_GEN;
        public static ForgeConfigSpec.BooleanValue ENABLE_FORTUNE_ON_ALL_ORES;
        public static ForgeConfigSpec.IntValue MAX_SAMPLES_PER_CHUNK;
        public static ForgeConfigSpec.BooleanValue BORING_SAMPLES;
        public static ForgeConfigSpec.BooleanValue ENABLE_PRO_PICK;
        public static ForgeConfigSpec.BooleanValue ENABLE_PRO_PICK_DMG;
        public static ForgeConfigSpec.IntValue PRO_PICK_DURABILITY;
        public static ForgeConfigSpec.IntValue PRO_PICK_RANGE;
        public static ForgeConfigSpec.IntValue PRO_PICK_DIAMETER;
        public static ForgeConfigSpec.EnumValue<SURFACE_PROSPECTING_TYPE> PRO_PICK_SURFACE_MODE;
        public static ForgeConfigSpec.IntValue NETHER_ORE_MAXY;
        public static ForgeConfigSpec.ConfigValue<String> DEFAULT_REPLACEMENT_MATS;
        private static String CATEGORY_FEATURE_CONTROL = "feature control";
        private static String CATEGORY_PROSPECTING = "prospecting";
        private static String CATEGORY_USER_ENTRIES = "user entries";

        static
        {
                init();
                ConfigClient.init(COMMON_BUILDER);
                ConfigCompat.init(COMMON_BUILDER);
                COMMON_CONFIG = COMMON_BUILDER.build();
        }

        public static void loadConfig(ForgeConfigSpec spec, Path path)
        {
                final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                                .writingMode(WritingMode.REPLACE).build();

                configData.load();
                spec.setConfig(configData);
        }

        private static void init()
        {
                COMMON_BUILDER.comment("Feature Control").push(CATEGORY_FEATURE_CONTROL);
                REPLACE_STONES = COMMON_BUILDER.comment("Replace Stone Variant Deposits").define("replaceStones", true);
                ENABLE_INGOTS = COMMON_BUILDER.comment("Enable Geolosys's Ingots").define("enableIngots", true);
                ENABLE_COALS = COMMON_BUILDER.comment("Enable Coal Variants").define("enableCoals", true);
                ENABLE_SMELTING = COMMON_BUILDER.comment("Enable Smelting Clusters -> Ingots")
                                .define("enableClusterSmelting", true);
                DEBUG_WORLD_GEN = COMMON_BUILDER.comment("Output info into the logs when generating Geolosys deposits")
                                .define("debugWorldgen", false);
                RETRO_REPLACE = COMMON_BUILDER.comment(
                                "Retroactively replace vanilla / Tagged ores to their Geolosys Counterpart when entering a chunk")
                                .define("retroReplace", true);
                DISABLE_VANILLA_ORE_GEN = COMMON_BUILDER.comment("Disable generation of Vanilla ores")
                                .define("disableVanillaOreGen", true);
                ENABLE_FORTUNE_ON_ALL_ORES = COMMON_BUILDER
                                .comment("Allow fortune to work on ores that drop clusters too")
                                .define("enableFortuneOnAllOres", false);
                DEFAULT_REPLACEMENT_MATS = COMMON_BUILDER.comment(
                                "The fallback materials which a Deposit can replace if they're not specified by the deposit itself")
                                .define("defaultReplacementMaterials",
                                                "minecraft:stone, minecraft:andesite, minecraft:diorite, minecraft:granite, minecraft:netherrack, minecraft:sandstone");
                COMMON_BUILDER.pop();

                COMMON_BUILDER.comment("Prospecting Options").push(CATEGORY_PROSPECTING);
                MAX_SAMPLES_PER_CHUNK = COMMON_BUILDER
                                .comment("Maximum samples that can generate with each pluton within a chunk")
                                .defineInRange("maxSamplesPerChunk", 10, 1, 256);
                BORING_SAMPLES = COMMON_BUILDER
                                .comment("Disable drops from samples, only popping up text of the item instead")
                                .define("boringSamples", false);
                ENABLE_PRO_PICK = COMMON_BUILDER.comment("Enable the prospector's pickaxe").define("enableProPick",
                                true);
                ENABLE_PRO_PICK_DMG = COMMON_BUILDER.comment("Allow the prospector's pick to get damaged")
                                .define("enableProPickDmg", false);
                PRO_PICK_DURABILITY = COMMON_BUILDER
                                .comment("Max durability of a prospector's pick if damage is enabled")
                                .defineInRange("proPickDurability", 1024, 1, Integer.MAX_VALUE);
                PRO_PICK_RANGE = COMMON_BUILDER.comment("The range (depth) of the prospector's pick prospecting cycle")
                                .defineInRange("proPickRange", 5, 1, Integer.MAX_VALUE);
                PRO_PICK_DIAMETER = COMMON_BUILDER.comment("The diameter of the prospector's pick prospecting cycle")
                                .defineInRange("proPickDiameter", 5, 1, Integer.MAX_VALUE);
                PRO_PICK_SURFACE_MODE = COMMON_BUILDER.comment(
                                "What Surface Prospecting Results display; SAMPLES means it's based off of samples in the chunk - OREBLOCKS means it's based on the actual ores in the ground")
                                .defineEnum("surfaceProspectingResults", SURFACE_PROSPECTING_TYPE.OREBLOCKS);
                COMMON_BUILDER.pop();

                COMMON_BUILDER.comment("Custom User Entries").push(CATEGORY_USER_ENTRIES);

                COMMON_BUILDER.pop();
        }

        public enum SURFACE_PROSPECTING_TYPE
        {
                SAMPLES, OREBLOCKS
        }
}
