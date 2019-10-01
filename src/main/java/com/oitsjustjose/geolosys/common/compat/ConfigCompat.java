package com.oitsjustjose.geolosys.common.compat;

import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigCompat
{

        public static final String CATEGORY_COMPAT = "compat";

        public static ForgeConfigSpec.BooleanValue ENABLE_OSMIUM;
        public static ForgeConfigSpec.BooleanValue ENABLE_OSMIUM_EXCLUSIVELY;
        public static ForgeConfigSpec.BooleanValue ENABLE_YELLORIUM;
        public static ForgeConfigSpec.BooleanValue ENABLE_SULFUR;

        public static void init(ForgeConfigSpec.Builder COMMON_BUILDER)
        {
                COMMON_BUILDER.comment("Mod-Compat Settings").push(CATEGORY_COMPAT);

                ENABLE_OSMIUM = COMMON_BUILDER
                                .comment("This will make it so that Platinum will drop Platinum AND Osmium")
                                .define("enableOsmium", true);
                ENABLE_OSMIUM_EXCLUSIVELY = COMMON_BUILDER
                                .comment("This will make it so that Platinum will ONLY drop Osmium")
                                .define("enableOsmiumExclusively", true);
                ENABLE_YELLORIUM = COMMON_BUILDER
                                .comment("This will make it so that Autunite will drop Uranium AND Yellorium")
                                .define("enableYellorium", true);
                ENABLE_SULFUR = COMMON_BUILDER
                                .comment("This will make is so that Coal has a rare chance of dropping sulfur")
                                .define("enableSulfur", true);

                COMMON_BUILDER.pop();
        }
}