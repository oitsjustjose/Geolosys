package com.oitsjustjose.geolosys.proxy;

import com.oitsjustjose.geolosys.Geolosys;
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
}
