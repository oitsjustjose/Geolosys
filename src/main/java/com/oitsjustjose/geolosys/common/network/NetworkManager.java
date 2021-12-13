package com.oitsjustjose.geolosys.common.network;

import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkManager {
    public SimpleChannel networkWrapper;
    private static final String PROTOCOL_VERSION = "1";

    public NetworkManager() {
        networkWrapper = NetworkRegistry.newSimpleChannel(new ResourceLocation(Constants.MODID, "main"),
                () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    }
}
