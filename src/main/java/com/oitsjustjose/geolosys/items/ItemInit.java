package com.oitsjustjose.geolosys.items;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;

public class ItemInit
{
    private static ItemInit instance;

    private ArrayList<Item> items;
    private HashMap<Item, Integer> burnTimes;

    private ItemInit()
    {
        items = new ArrayList<Item>();
        burnTimes = new HashMap<Item, Integer>();

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
            burnTimes.put(item, coalType.getBurnTime());
        }

        for (Types.CoalCoke coalCokeType : Types.CoalCoke.values())
        {
            Properties itemProps = new Properties().group(ItemGroup.MATERIALS);
            Item item = new Item(itemProps).setRegistryName("geolosys", coalCokeType.getName() + "_coal_coke");
            items.add(item);
            burnTimes.put(item, coalCokeType.getBurnTime());
        }
        // items.add(new ItemProPick());
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
    public HashMap<Item, Integer> getModFuels()
    {
        return (HashMap<Item, Integer>) this.burnTimes.clone();
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Item> getModItems()
    {
        return (ArrayList<Item>) this.items.clone();
    }
}