package com.oitsjustjose.geolosys.common.config;

import java.nio.file.Path;
import java.util.List;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.google.common.collect.Lists;
import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.utils.Utils;
import com.oitsjustjose.geolosys.common.world.SampleUtils;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class CommonConfig {
    public static final ForgeConfigSpec COMMON_CONFIG;
    private static final Builder COMMON_BUILDER = new Builder();
    public static ForgeConfigSpec.BooleanValue ENABLE_INGOTS;
    public static ForgeConfigSpec.BooleanValue ENABLE_COALS;
    public static ForgeConfigSpec.BooleanValue DEBUG_WORLD_GEN;
    public static ForgeConfigSpec.BooleanValue REMOVE_VANILLA_ORES;
    public static ForgeConfigSpec.IntValue MAX_SAMPLES_PER_CHUNK;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> SAMPLE_PLACEMENT_BLACKLIST;
    public static ForgeConfigSpec.BooleanValue ENABLE_PRO_PICK;
    public static ForgeConfigSpec.BooleanValue ENABLE_PRO_PICK_DMG;
    public static ForgeConfigSpec.IntValue PRO_PICK_DURABILITY;
    public static ForgeConfigSpec.IntValue PRO_PICK_RANGE;
    public static ForgeConfigSpec.IntValue PRO_PICK_DIAMETER;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> PRO_PICK_EXTRAS;
    public static ForgeConfigSpec.EnumValue<SURFACE_PROSPECTING_TYPE> PRO_PICK_SURFACE_MODE;
    public static ForgeConfigSpec.BooleanValue GIVE_MANUAL_TO_NEW;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> DEFAULT_REPLACEMENT_MATS;
    private static String CATEGORY_FEATURE_CONTROL = "feature control";
    private static String CATEGORY_PROSPECTING = "prospecting";

    static {
        init();
        CompatConfig.init(COMMON_BUILDER);
        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    public static void loadConfig(ForgeConfigSpec spec, Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();

        configData.load();
        spec.setConfig(configData);
    }

    @SuppressWarnings("deprecation")
    private static void init() {
        COMMON_BUILDER.comment("Feature Control").push(CATEGORY_FEATURE_CONTROL);
        ENABLE_INGOTS = COMMON_BUILDER.comment("Enable Geolosys's Ingots").define("enableIngots", true);
        ENABLE_COALS = COMMON_BUILDER.comment("Enable Coal Variants").define("enableCoals", true);

        DEBUG_WORLD_GEN = COMMON_BUILDER.comment("Output info into the logs when generating Geolosys deposits")
                .define("debugWorldgen", false);
        REMOVE_VANILLA_ORES = COMMON_BUILDER.comment("Disable generation of Vanilla ores")
                .define("disableVanillaOreGen", true);
        DEFAULT_REPLACEMENT_MATS = COMMON_BUILDER.comment(
                "The fallback materials which a Deposit can replace if they're not specified by the deposit itself\n"
                        + "Format: Comma-delimited set of <modid:block> (see default for example)")
                .defineList("defaultReplacementMaterials", Lists.newArrayList("minecraft:stone", "minecraft:andesite",
                        "minecraft:diorite", "minecraft:granite", "minecraft:netherrack", "minecraft:sandstone"),
                        rawName -> {
                            if (rawName instanceof String) {
                                String name = (String) rawName;
                                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
                                if (block == null) {
                                    return false;
                                }
                                return Utils.addDefaultMatcher(block);
                            }
                            return false;
                        });
        COMMON_BUILDER.pop();

        COMMON_BUILDER.comment("Prospecting Options").push(CATEGORY_PROSPECTING);
        MAX_SAMPLES_PER_CHUNK = COMMON_BUILDER
                .comment("Maximum samples that can generate with each pluton within a chunk")
                .defineInRange("maxSamplesPerChunk", 10, 1, 256);

        SAMPLE_PLACEMENT_BLACKLIST = COMMON_BUILDER.comment("A list of <modid:block> that samples may not be placed on")
                .defineList("samplePlacementBlacklist", Lists.newArrayList("minecraft:ice", "minecraft:packed_ice"),
                        rawName -> {
                            if (rawName instanceof String) {
                                String name = (String) rawName;
                                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
                                if (block == null || block.getDefaultState().isAir()) {
                                    return false;
                                }
                                SampleUtils.addSamplePlacementBlacklist(block);
                                return true;
                            }
                            return false;
                        });
        ENABLE_PRO_PICK = COMMON_BUILDER.comment("Enable the prospector's pickaxe").define("enableProPick", true);
        ENABLE_PRO_PICK_DMG = COMMON_BUILDER.comment("Allow the prospector's pick to get damaged")
                .define("enableProPickDmg", false);
        PRO_PICK_DURABILITY = COMMON_BUILDER.comment("Max durability of a prospector's pick if damage is enabled")
                .defineInRange("proPickDurability", 1024, 1, Integer.MAX_VALUE);
        PRO_PICK_RANGE = COMMON_BUILDER.comment("The range (depth) of the prospector's pick prospecting cycle")
                .defineInRange("proPickRange", 5, 1, Integer.MAX_VALUE);
        PRO_PICK_DIAMETER = COMMON_BUILDER.comment("The diameter of the prospector's pick prospecting cycle")
                .defineInRange("proPickDiameter", 5, 1, Integer.MAX_VALUE);
        PRO_PICK_EXTRAS = COMMON_BUILDER
                .comment("A list of blocks which the prospector's pick should also detect.\n"
                        + "Format: Comma-delimited set of <modid:block> (see default for example)")
                .defineList("proPickExtras", Lists.newArrayList(), rawName -> {
                    if (rawName instanceof String) {
                        String name = (String) rawName;
                        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
                        if (block == null || block.getDefaultState().isAir()) {
                            return false;
                        }
                        GeolosysAPI.proPickExtras.add(block.getDefaultState());
                        return true;
                    }
                    return false;
                });
        PRO_PICK_SURFACE_MODE = COMMON_BUILDER.comment(
                "What Surface Prospecting Results display; SAMPLES means it's based off of samples in the chunk - OREBLOCKS means it's based on the actual ores in the ground")
                .defineEnum("surfaceProspectingResults", SURFACE_PROSPECTING_TYPE.OREBLOCKS);
        GIVE_MANUAL_TO_NEW = COMMON_BUILDER.comment("Give players a Field Manual if they haven't gotten one")
                .define("giveManual", true);
        COMMON_BUILDER.pop();
    }

    public enum SURFACE_PROSPECTING_TYPE {
        SAMPLES, OREBLOCKS
    }
}
