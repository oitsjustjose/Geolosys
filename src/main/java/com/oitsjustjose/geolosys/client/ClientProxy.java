package com.oitsjustjose.geolosys.client;

import com.oitsjustjose.geolosys.client.manual.GUIManual;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.File;
import java.util.Objects;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        GUIManual.initPages();
    }

    @Override
    public void throwDownloadError(File jsonFile)
    {
        ModInfo geolosysModInfo = null;
        for (ModInfo info : ModList.get().getMods())
        {
            if (info.getModId().equalsIgnoreCase(Constants.MODID))
            {
                geolosysModInfo = info;
                break;
            }
        }
        throw new ModLoadingException(Objects.requireNonNull(geolosysModInfo), ModLoadingStage.COMMON_SETUP,
                "geolosys.download.error.string", new RuntimeException());
    }
}