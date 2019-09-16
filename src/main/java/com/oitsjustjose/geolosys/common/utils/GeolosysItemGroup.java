package com.oitsjustjose.geolosys.common.utils;

import com.oitsjustjose.geolosys.common.blocks.BlockInit;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class GeolosysItemGroup extends ItemGroup
{
    private static GeolosysItemGroup instance;

    public GeolosysItemGroup()
    {
        super("geolosys.name");
    }

    @Override
    public ItemStack createIcon()
    {
        return new ItemStack(BlockInit.getInstance().getModBlocks().get("geolosys:azurite_ore"));
    }

    public static GeolosysItemGroup getInstance()
    {
        if (instance == null)
        {
            instance = new GeolosysItemGroup();
        }
        return instance;
    }
}