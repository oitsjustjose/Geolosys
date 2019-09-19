package com.oitsjustjose.geolosys.client;

import java.io.File;

import com.oitsjustjose.geolosys.client.errors.DownloadErrorDisplayException;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.network.HandlerIOreSurfaceClient;
import com.oitsjustjose.geolosys.common.network.HandlerIOreUndergroundClient;
import com.oitsjustjose.geolosys.common.network.HandlerSurfaceClient;
import com.oitsjustjose.geolosys.common.network.HandlerUndergroundClient;
import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.PacketIOreSurface;
import com.oitsjustjose.geolosys.common.network.PacketIOreUnderground;
import com.oitsjustjose.geolosys.common.network.PacketSurface;
import com.oitsjustjose.geolosys.common.network.PacketUnderground;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ClientProxy extends CommonProxy
{
    public NetworkManager networkManager;

    @Override
    public void preInit()
    {
        networkManager = new NetworkManager();
        networkManager.networkWrapper.registerMessage(HandlerIOreSurfaceClient.class,
                PacketIOreSurface.class, CommonProxy.discriminator++, Side.CLIENT);
        networkManager.networkWrapper.registerMessage(HandlerSurfaceClient.class, PacketSurface.class,
                CommonProxy.discriminator++, Side.CLIENT);
        networkManager.networkWrapper.registerMessage(HandlerIOreUndergroundClient.class,
                PacketIOreUnderground.class, CommonProxy.discriminator++, Side.CLIENT);
        networkManager.networkWrapper.registerMessage(HandlerUndergroundClient.class,
                PacketUnderground.class, CommonProxy.discriminator++, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        GuiManual.initPages();
    }

    @Override
    public void throwDownloadError(File jsonFile)
    {
        throw new DownloadErrorDisplayException("Geolosys Download Exception", "File " + jsonFile.getAbsolutePath()
                + " could neither be found nor downloaded. "
                + "You can download the file at https://raw.githubusercontent.com/oitsjustjose/Geolosys/1.12.x/geolosys_ores.json and put it in your config folder manually if you wish (it will need to be renamed \"geolosys.json\").");
    }

    @Override
    public void sendProspectingMessage(EntityPlayer player, ItemStack stack, EnumFacing direction)
    {
        if (direction != null)
        {
            player.sendStatusMessage(
                    new TextComponentTranslation("geolosys.pro_pick.tooltip.found", stack.getDisplayName(), direction),
                    true);
        }
        else
        {
            player.sendStatusMessage(
                    new TextComponentTranslation("geolosys.pro_pick.tooltip.found_surface", stack.getDisplayName()),
                    true);
        }
    }

    @Override
    public void sendProspectingMessage(EntityPlayer player, String friendlyName, EnumFacing direction)
    {
        if (direction != null)
        {
            player.sendStatusMessage(
                    new TextComponentTranslation("geolosys.pro_pick.tooltip.found", friendlyName, direction), true);
        }
        else
        {
            player.sendStatusMessage(
                    new TextComponentTranslation("geolosys.pro_pick.tooltip.found_surface", friendlyName), true);
        }
    }
}
