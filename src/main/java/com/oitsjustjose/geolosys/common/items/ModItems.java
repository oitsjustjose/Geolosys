package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.items.Types.Clusters;
import com.oitsjustjose.geolosys.common.items.Types.Coals;
import com.oitsjustjose.geolosys.common.items.Types.Ingots;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;

import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ModItems {
    private static ModItems instance;

    public static final Item PRO_PICK = new ProPickItem();

    private final String CLUSTER_POSTFIX = "_cluster";
    private final String INGOT_POSTFIX = "_ingot";
    private final String COAL_POSTFIX = "_coal";
    private final String COAL_COKE_POSTFIX = "_coal_coke";

    private final Properties genericItemProps = new Properties().group(GeolosysGroup.getInstance());

    private ModItems() {
        // Init Clusters
        for (Clusters cluster : Clusters.values()) {
            Item item = new Item(genericItemProps).setRegistryName("geolosys", cluster.getName() + CLUSTER_POSTFIX);
            cluster.setItem(item);
        }
        // Init Ingots
        if (CommonConfig.ENABLE_INGOTS.get()) {
            for (Ingots ingot : Ingots.values()) {
                Item item = new Item(genericItemProps).setRegistryName("geolosys", ingot.getName() + INGOT_POSTFIX);
                ingot.setItem(item);
            }
        }
        // Init Coals
        if (CommonConfig.ENABLE_COALS.get()) {
            for (Coals coal : Coals.values()) {
                Item item = new CoalItem(coal).setRegistryName("geolosys",
                        coal.getName() + (coal.isCoalCoke() ? COAL_COKE_POSTFIX : COAL_POSTFIX));
                coal.setItem(item);
            }
        }
    }

    public static ModItems getInstance() {
        if (instance == null) {
            instance = new ModItems();
        }
        return instance;
    }

    public void register(RegistryEvent.Register<Item> itemRegistryEvent) {
        for (Clusters cluster : Clusters.values()) {
            itemRegistryEvent.getRegistry().register(cluster.getItem());
        }
        if (CommonConfig.ENABLE_INGOTS.get()) {
            for (Ingots ingot : Ingots.values()) {
                itemRegistryEvent.getRegistry().register(ingot.getItem());
            }
        }
        if (CommonConfig.ENABLE_COALS.get()) {
            for (Coals coal : Coals.values()) {
                itemRegistryEvent.getRegistry().register(coal.getItem());
            }
        }

        itemRegistryEvent.getRegistry().register(PRO_PICK);
    }

    @SubscribeEvent
    public void onFuelRegistry(FurnaceFuelBurnTimeEvent fuelBurnoutEvent) {
        if (CommonConfig.ENABLE_COALS.get()) {
            for (Coals c : Coals.values()) {
                if (fuelBurnoutEvent.getItemStack().getItem().equals(c.getItem())) {
                    fuelBurnoutEvent.setBurnTime(c.getBurnTime() * 200);
                }
            }
        }
    }
}