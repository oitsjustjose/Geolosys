package com.oitsjustjose.geolosys.common;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.client.ClientGUIProxy;
import com.oitsjustjose.geolosys.common.network.HandlerIOreSurfaceServer;
import com.oitsjustjose.geolosys.common.network.HandlerIOreUndergroundServer;
import com.oitsjustjose.geolosys.common.network.HandlerSurfaceServer;
import com.oitsjustjose.geolosys.common.network.HandlerUndergroundServer;
import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketIOreSurface;
import com.oitsjustjose.geolosys.common.network.PacketIOreUnderground;
import com.oitsjustjose.geolosys.common.network.PacketSurface;
import com.oitsjustjose.geolosys.common.network.PacketUnderground;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

public class CommonProxy {
    public static int discriminator = 0;
    public NetworkManager networkManager;

    public void preInit() {
        networkManager = new NetworkManager();
        networkManager.networkWrapper.registerMessage(HandlerIOreSurfaceServer.class, PacketIOreSurface.class,
                discriminator++, Side.SERVER);
        networkManager.networkWrapper.registerMessage(HandlerSurfaceServer.class, PacketSurface.class, discriminator++,
                Side.SERVER);
        networkManager.networkWrapper.registerMessage(HandlerIOreUndergroundServer.class, PacketIOreUnderground.class,
                discriminator++, Side.SERVER);
        networkManager.networkWrapper.registerMessage(HandlerUndergroundServer.class, PacketUnderground.class,
                discriminator++, Side.SERVER);
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Geolosys.getInstance(), new ClientGUIProxy());
    }

    public void postInit(FMLPostInitializationEvent event) {
    }

    public void throwExtractError(File jsonFile) {
        Geolosys.getInstance().LOGGER.error("File " + jsonFile.getAbsolutePath()
                + " could not be extracted from the Geolosys jar file."
                + " You can download the file at https://raw.githubusercontent.com/oitsjustjose/Geolosys/refs/heads/1.12.x/src/main/resources/assets/geolosys/geolosys.json and put it in your config folder manually if you wish.");
    }

    public void sendProspectingMessage(EntityPlayer player, ItemStack stack, EnumFacing direction) {
        IMessage message;
        if (direction != null) {
            message = new PacketUnderground(stack, direction);
        } else {
            message = new PacketSurface(stack);
        }
        networkManager.sendToClient(message, player);
    }

    public void sendProspectingMessage(EntityPlayer player, String friendlyName, EnumFacing direction) {
        IMessage message;
        if (direction != null) {
            message = new PacketIOreUnderground(friendlyName, direction);
        } else {
            message = new PacketIOreSurface(friendlyName);
        }
        networkManager.sendToClient(message, player);
    }
}
