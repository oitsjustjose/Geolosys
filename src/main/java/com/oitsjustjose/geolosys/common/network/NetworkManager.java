package com.oitsjustjose.geolosys.common.network;

import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Predicate;

public class NetworkManager
{
    public SimpleChannel networkWrapper;
    private static final String PROTOCOL_VERSION = "1";

    public NetworkManager()
    {
        networkWrapper = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Constants.MODID, "pro_pick_messages"))
                .clientAcceptedVersions(new Predicate<String>()
                {
                    @Override
                    public boolean test(String s)
                    {
                        return PROTOCOL_VERSION.equalsIgnoreCase(s);
                    }
                }).serverAcceptedVersions(new Predicate<String>()
                {
                    @Override
                    public boolean test(String s)
                    {
                        return PROTOCOL_VERSION.equalsIgnoreCase(s);
                    }
                }).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    }

}