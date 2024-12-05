package com.oitsjustjose.geolosys.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class Constants {
    public static final String MOD_ID = "geolosys";
    public static final ResourceLocation DEPOSIT_CAPABILITY_NAME = new ResourceLocation(MOD_ID, "deposits");
    public static final ResourceLocation PLAYER_CAPABILITY_NAME = new ResourceLocation(MOD_ID, "player_data");
    public static final ResourceLocation CHUNKGEN_CAPABILITY_NAME = new ResourceLocation(MOD_ID, "chunks_generated");
    public static final TagKey<Block> SUPPORTS_SAMPLE = BlockTags.create(new ResourceLocation(MOD_ID, "supports_sample"));
}
