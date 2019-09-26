package com.oitsjustjose.geolosys.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigClient
{

        public static final String CATEGORY_CLIENT = "client";

        public static ForgeConfigSpec.DoubleValue MANUAL_FONT_SCALE;
        public static ForgeConfigSpec.IntValue PROPICK_HUD_X;
        public static ForgeConfigSpec.IntValue PROPICK_HUD_Y;
        public static ForgeConfigSpec.BooleanValue ENABLE_TAG_DEBUG;

        public static void init(ForgeConfigSpec.Builder COMMON_BUILDER)
        {
                COMMON_BUILDER.comment("Client-Side Settings").push(CATEGORY_CLIENT);

                MANUAL_FONT_SCALE = COMMON_BUILDER.comment("Defines the scale factor of the font for the Field Manual")
                                .defineInRange("manualFontScale", 0.75D, 0.1D, 3.0D);
                PROPICK_HUD_X = COMMON_BUILDER.comment("The X-position of the Prospector's Pickaxe Depth HUD")
                                .defineInRange("propickHUDX", 2, 0, Integer.MAX_VALUE);
                PROPICK_HUD_Y = COMMON_BUILDER.comment("The Y-position of the Prospector's Pickaxe Depth HUD")
                                .defineInRange("propickHUDY", 2, 0, Integer.MAX_VALUE);
                ENABLE_TAG_DEBUG = COMMON_BUILDER
                                .comment("Enable Minecraft object tag tooltip with Advanced Tooltips on (F3+\"H\")")
                                .define("enableTagDebug", true);

                COMMON_BUILDER.pop();
        }
}