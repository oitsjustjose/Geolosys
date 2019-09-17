package com.oitsjustjose.geolosys.common.utils;

import com.oitsjustjose.geolosys.common.blocks.BlockInit;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class GeolosysGroup extends ItemGroup
{
    private static GeolosysGroup instance;

    private GeolosysGroup()
    {
        super("geolosys.name");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(BlockInit.getInstance().getModBlocks().get("geolosys:azurite_ore"));
    }

    public static GeolosysGroup getInstance()
    {
        if (instance == null)
        {
            instance = new GeolosysGroup();
        }
        return instance;
    }
}