package com.oitsjustjose.geolosys.common.items;

import java.util.HashMap;

import com.google.common.collect.Maps;
import com.oitsjustjose.geolosys.common.utils.GeolosysItemGroup;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;

public class ItemInit
{
    private static ItemInit instance;

    private HashMap<String, Item> items;
    private HashMap<Item, Integer> burnTimes;

    private ItemInit()
    {
        items = Maps.newHashMap();
        burnTimes = Maps.newHashMap();

        for (Types.Cluster clusterType : Types.Cluster.values())
        {
            Properties itemProps = new Properties().group(GeolosysItemGroup.getInstance());
            Item item = new Item(itemProps).setRegistryName("geolosys", clusterType.getName() + "_cluster");
            items.put(item.getRegistryName().toString(), item);
        }

        for (Types.Ingot ingotType : Types.Ingot.values())
        {
            Properties itemProps = new Properties().group(GeolosysItemGroup.getInstance());
            Item item = new Item(itemProps).setRegistryName("geolosys", ingotType.getName() + "_ingot");
            items.put(item.getRegistryName().toString(), item);
        }

        for (Types.Coal coalType : Types.Coal.values())
        {
            Properties itemProps = new Properties().group(GeolosysItemGroup.getInstance());
            Item item = new Item(itemProps).setRegistryName("geolosys", coalType.getName() + "_coal");
            items.put(item.getRegistryName().toString(), item);
            burnTimes.put(item, coalType.getBurnTime());
        }

        for (Types.CoalCoke coalCokeType : Types.CoalCoke.values())
        {
            Properties itemProps = new Properties().group(GeolosysItemGroup.getInstance());
            Item item = new Item(itemProps)
            {

            }.setRegistryName("geolosys", coalCokeType.getName() + "_coal_coke");
            items.put(item.getRegistryName().toString(), item);
            burnTimes.put(item, coalCokeType.getBurnTime());
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

    public void register(RegistryEvent.Register<Item> itemRegistryEvent)
    {
        for (Item i : this.getModItems().values())
        {
            itemRegistryEvent.getRegistry().register(i);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<Item, Integer> getModFuels()
    {
        return (HashMap<Item, Integer>) this.burnTimes.clone();
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Item> getModItems()
    {
        return (HashMap<String, Item>) this.items.clone();
    }
}