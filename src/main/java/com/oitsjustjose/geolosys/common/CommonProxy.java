package com.oitsjustjose.geolosys.common;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.client.ClientGUIProxy;

import net.minecraft.util.text.translation.I18n;
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

    @SuppressWarnings("deprecation")
    public String translate(String unlocalized, Object... args)
    {
        return I18n.translateToLocalFormatted(unlocalized, args);
    }
}
