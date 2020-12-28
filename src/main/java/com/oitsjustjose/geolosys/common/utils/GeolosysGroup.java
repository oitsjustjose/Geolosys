package com.oitsjustjose.geolosys.common.utils;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.Types.Ores;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GeolosysGroup extends ItemGroup {
    private static GeolosysGroup instance;

    int counter = 0;
    ItemStack display = new ItemStack(Ores.values()[counter].getBlock());
    private long lastTick = 0L;

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
    public ItemStack createIcon() {
        return new ItemStack(Ores.AZURITE.getBlock());
    }

    @OnlyIn(Dist.CLIENT)
    public ItemStack getIcon() {
        if (System.currentTimeMillis() - lastTick > 1000L) {
            counter = counter == (Ores.values().length - 1) ? 0 : counter + 1;
            display = new ItemStack(Ores.values()[counter].getBlock());
            lastTick = System.currentTimeMillis();
        }
        return display;
    }
}