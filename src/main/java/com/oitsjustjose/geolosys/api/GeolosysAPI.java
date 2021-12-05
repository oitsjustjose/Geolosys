package com.oitsjustjose.geolosys.api;

import com.oitsjustjose.geolosys.common.world.PlutonRegistry;

import net.minecraftforge.common.MinecraftForge;

/**
 * The Geolosys API is intended for use by anyone who wants to tap into all the
 * locations that deposits exist Access is pretty easy, just reference this
 * class's currentWorldDeposits HashMap
 */
public class GeolosysAPI {
    // An instance of the registry for all generatable plutons
    public static PlutonRegistry plutonRegistry = new PlutonRegistry();

    public static void init() {
        MinecraftForge.EVENT_BUS.register(plutonRegistry);
    }
}
