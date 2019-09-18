package com.oitsjustjose.geolosys.common;

import java.io.File;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.client.ClientGUIProxy;
import com.oitsjustjose.geolosys.common.network.NetworkManager;
import com.oitsjustjose.geolosys.common.network.ProPickSurfacePacket;
import com.oitsjustjose.geolosys.common.network.ProPickSurfacePacketIOre;
import com.oitsjustjose.geolosys.common.network.ProPickUndergroundPacket;
import com.oitsjustjose.geolosys.common.network.ProPickUndergroundPacketIOre;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CommonProxy
{
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(Geolosys.getInstance(), new ClientGUIProxy());
    }

    public void postInit(FMLPostInitializationEvent event)
    {
    }

    public void throwDownloadError(File jsonFile)
    {
        Geolosys.getInstance().LOGGER.error("File " + jsonFile.getAbsolutePath()
                + " could neither be found nor downloaded. "
                + "You can download the file at https://raw.githubusercontent.com/oitsjustjose/Geolosys/1.12.x/geolosys_ores.json and put it in your config folder manually if you wish (it will need to be renamed \"geolosys.json\").");
    }

    public void sendProspectingMessage(EntityPlayer player, ItemStack stack, EnumFacing direction)
    {
        IMessage message;
        if (direction != null)
        {
            message = new ProPickUndergroundPacket(stack, direction);
        }
        else
        {
            message = new ProPickSurfacePacket(stack);
        }
        NetworkManager.getInstance().sendToClient(message, player);
    }

    public void sendProspectingMessage(EntityPlayer player, String friendlyName, EnumFacing direction)
    {
        IMessage message;
        if (direction != null)
        {
            message = new ProPickUndergroundPacketIOre(friendlyName, direction);
        }
        else
        {
            message = new ProPickSurfacePacketIOre(friendlyName);
        }
        NetworkManager.getInstance().sendToClient(message, player);
    }
}
