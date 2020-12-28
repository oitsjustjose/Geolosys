package com.oitsjustjose.geolosys.common.items;

import com.google.common.collect.Maps;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map.Entry;

public class Items {
    private static Items instance;

    private HashMap<String, Item> items;
    private HashMap<Item, Integer> burnTimes;

    private Items() {
        items = Maps.newHashMap();
        burnTimes = Maps.newHashMap();
        if (CommonConfig.ENABLE_COALS.get()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
        for (Types.Cluster clusterType : Types.Cluster.values()) {
            // Skip some entries if they are disabled in the config
            if (clusterType == Types.Cluster.YELLORIUM && !CompatConfig.ENABLE_YELLORIUM.get()) {
                continue;
            }
            if (clusterType == Types.Cluster.OSMIUM && !CompatConfig.ENABLE_OSMIUM.get()) {
                continue;
            }
            Properties itemProps = new Properties().group(GeolosysGroup.getInstance());
            Item item = new Item(itemProps).setRegistryName("geolosys", clusterType.getName() + "_cluster");
            items.put(item.getRegistryName().toString(), item);
        }

        if (CommonConfig.ENABLE_INGOTS.get()) {
            for (Types.Ingot ingotType : Types.Ingot.values()) {
                Properties itemProps = new Properties().group(GeolosysGroup.getInstance());
                Item item = new Item(itemProps).setRegistryName("geolosys", ingotType.getName() + "_ingot");
                items.put(item.getRegistryName().toString(), item);
            }
        }
        if (CommonConfig.ENABLE_COALS.get()) {
            for (Types.Coal coalType : Types.Coal.values()) {
                Properties itemProps = new Properties().group(GeolosysGroup.getInstance());
                Item item = new Item(itemProps).setRegistryName("geolosys", coalType.getName() + "_coal");
                items.put(item.getRegistryName().toString(), item);
                burnTimes.put(item, coalType.getBurnTime());
            }

            for (Types.CoalCoke coalCokeType : Types.CoalCoke.values()) {
                Properties itemProps = new Properties().group(GeolosysGroup.getInstance());
                Item item = new Item(itemProps).setRegistryName("geolosys", coalCokeType.getName() + "_coal_coke");
                items.put(item.getRegistryName().toString(), item);
                burnTimes.put(item, coalCokeType.getBurnTime());
            }
        }
        this.items.put(ProPickItem.REGISTRY_NAME.toString(), new ProPickItem());
    }

    public static Items getInstance() {
        if (instance == null) {
            instance = new Items();
        }
        return instance;
    }

    public void register(RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Item i : this.getModItems().values()) {
            itemRegistryEvent.getRegistry().register(i);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<Item, Integer> getModFuels() {
        return (HashMap<Item, Integer>) this.burnTimes.clone();
    }

    @SubscribeEvent
    public void onFuelRegistry(FurnaceFuelBurnTimeEvent fuelBurnoutEvent) {
        if (CommonConfig.ENABLE_COALS.get()) {
            for (Entry<Item, Integer> fuelPair : Items.getInstance().getModFuels().entrySet()) {
                if (fuelBurnoutEvent.getItemStack().getItem().equals(fuelPair.getKey())) {
                    fuelBurnoutEvent.setBurnTime(fuelPair.getValue() * 200);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Item> getModItems() {
        return (HashMap<String, Item>) this.items.clone();
    }
}