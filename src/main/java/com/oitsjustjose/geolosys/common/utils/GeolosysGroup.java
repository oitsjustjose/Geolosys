package com.oitsjustjose.geolosys.common.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class GeolosysGroup extends CreativeModeTab {
    private static GeolosysGroup instance;


    private GeolosysGroup() {
        super("geolosys.name");
    }

    public static GeolosysGroup getInstance() {
        if (instance == null) {
            instance = new GeolosysGroup();
        }
        return instance;
    }

    @Override
    public @NotNull ItemStack makeIcon() {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(Constants.MODID, "prospectors_pick")));
    }
}
