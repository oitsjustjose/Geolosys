package com.oitsjustjose.geolosys.common.utils;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class Constants {
    public static final String MODID = "geolosys";
    public static final ResourceLocation DEPOSIT_CAPABILITY_NAME = new ResourceLocation(MODID, "deposits");
    public static final ResourceLocation PLAYER_CAPABILITY_NAME = new ResourceLocation(MODID, "player_data");
    public static final ResourceLocation CHUNKGEN_CAPABILITY_NAME = new ResourceLocation(MODID, "chunks_generated");
    public static final TagKey<Block> SUPPORTS_SAMPLE = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(MODID, "supports_sample"));
}
