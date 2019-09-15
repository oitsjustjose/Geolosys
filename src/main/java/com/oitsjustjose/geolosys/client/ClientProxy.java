package com.oitsjustjose.geolosys.client;

import java.io.File;

import com.oitsjustjose.geolosys.client.errors.DownloadErrorDisplayException;
import com.oitsjustjose.geolosys.common.CommonProxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy
{
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

}
