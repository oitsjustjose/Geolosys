package com.oitsjustjose.geolosys.common.world;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraftforge.registries.ForgeRegistries;

public class WorldGenOverrides
{
    public static void init()
    {
        OreFeature ore = (OreFeature) ForgeRegistries.FEATURES.getValue(new ResourceLocation("minecraft", "ore"));
        // ore.
    }
}