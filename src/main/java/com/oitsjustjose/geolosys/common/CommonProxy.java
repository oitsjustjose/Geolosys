package com.oitsjustjose.geolosys.common;

import com.oitsjustjose.geolosys.Geolosys;

import java.io.File;

public class CommonProxy
{
    public void init()
    {

    }

    public void throwDownloadError(File jsonFile)
    {
        Geolosys.getInstance().LOGGER.error("File " + jsonFile.getAbsolutePath()
                + " could neither be found nor downloaded. "
                + "You can download the file at https://raw.githubusercontent.com/oitsjustjose/Geolosys/1.12.x/geolosys_ores.json "
                + "and put it in your config folder manually if you wish (it will need to be renamed \"geolosys.json\").");
    }
}
