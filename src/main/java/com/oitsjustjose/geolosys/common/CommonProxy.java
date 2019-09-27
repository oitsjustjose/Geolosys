package com.oitsjustjose.geolosys.common;

import java.io.File;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.network.HandlerIOreSurfaceClient;
import com.oitsjustjose.geolosys.common.network.HandlerIOreUndergroundClient;
import com.oitsjustjose.geolosys.common.network.HandlerSurfaceClient;
import com.oitsjustjose.geolosys.common.network.HandlerUndergroundClient;
import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketIOreSurface;
import com.oitsjustjose.geolosys.common.network.PacketIOreUnderground;
import com.oitsjustjose.geolosys.common.network.PacketSurface;
import com.oitsjustjose.geolosys.common.network.PacketUnderground;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

public class CommonProxy
{
    public NetworkManager networkManager;

    public void init()
    {
        networkManager = new NetworkManager();
        networkManager.networkWrapper.registerMessage(HandlerIOreSurfaceClient.class, PacketIOreSurface.class,
                CommonProxy.discriminator++, Side.CLIENT);
        networkManager.networkWrapper.registerMessage(HandlerSurfaceClient.class, PacketSurface.class,
                CommonProxy.discriminator++, Side.CLIENT);
        networkManager.networkWrapper.registerMessage(HandlerIOreUndergroundClient.class, PacketIOreUnderground.class,
                CommonProxy.discriminator++, Side.CLIENT);
        networkManager.networkWrapper.registerMessage(HandlerUndergroundClient.class, PacketUnderground.class,
                CommonProxy.discriminator++, Side.CLIENT);
    }

    public void throwDownloadError(File jsonFile)
    {
        Geolosys.getInstance().LOGGER.error("File " + jsonFile.getAbsolutePath()
                + " could neither be found nor downloaded. "
                + "You can download the file at https://raw.githubusercontent.com/oitsjustjose/Geolosys/1.12.x/geolosys_ores.json "
                + "and put it in your config folder manually if you wish (it will need to be renamed \"geolosys.json\").");
    }

    public void sendProspectingMessage(PlayerEntity player, ItemStack stack, Direction direction)
    {
    }
}
