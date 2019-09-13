package com.oitsjustjose.geolosys.items;

import java.util.ArrayList;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;

public class ItemInit
{
    private static ItemInit instance;

    private ArrayList<Item> items;

    private ItemInit()
    {
        items = new ArrayList<>();

        for (Types.Cluster clusterType : Types.Cluster.values())
        {
            Properties itemProps = new Properties().group(ItemGroup.MATERIALS);
            Item item = new Item(itemProps).setRegistryName("geolosys", clusterType.getName() + "_cluster");
            items.add(item);
        }

        for (Types.Ingot ingotType : Types.Ingot.values())
        {
            Properties itemProps = new Properties().group(ItemGroup.MATERIALS);
            Item item = new Item(itemProps).setRegistryName("geolosys", ingotType.getName() + "_ingot");
            items.add(item);
        }

        for (Types.Coal coalType : Types.Coal.values())
        {
            Properties itemProps = new Properties().group(ItemGroup.MATERIALS);
            Item item = new Item(itemProps).setRegistryName("geolosys", coalType.getName() + "_coal");
            items.add(item);
        }

        for (Types.CoalCoke coalCokeType : Types.CoalCoke.values())
        {
            Properties itemProps = new Properties().group(ItemGroup.MATERIALS);
            Item item = new Item(itemProps).setRegistryName("geolosys", coalCokeType.getName() + "_coal_coke");
            items.add(item);
        }
    }

    public static ItemInit getInstance()
    {
        if (instance == null)
        {
            instance = new ItemInit();
        }
        return instance;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Item> getModItems()
    {
        return (ArrayList<Item>) this.items.clone();
    }
}