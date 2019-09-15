package com.oitsjustjose.geolosys.common;

import java.io.File;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.client.ClientGUIProxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

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
}
