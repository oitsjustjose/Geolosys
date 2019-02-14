package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.common.config.ModConfig;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class CompatLoader
{
    public static void init()
    {
        new CompatLoader();
    }

    public CompatLoader()
    {
        this.initCompats();
    }

    private void initCompats()
    {
        if (Loader.isModLoaded("nuclearcraft"))
        {
            MinecraftForge.EVENT_BUS.register(new NCCompat());
        }
        if (Loader.isModLoaded("extrautils2"))
        {
            MinecraftForge.EVENT_BUS.register(new ExUtilsCompat());
        }
        if (Loader.isModLoaded("appliedenergistics2"))
        {
            MinecraftForge.EVENT_BUS.register(new AppEngCompat());
        }
        if (Loader.isModLoaded("thermalfoundation"))
        {
            MinecraftForge.EVENT_BUS.register(new ThermExpCompat());
        }
        if (Loader.isModLoaded("thaumcraft"))
        {
            MinecraftForge.EVENT_BUS.register(new ThaumcraftCompat());
        }
        if (Loader.isModLoaded("actuallyadditions"))
        {
            MinecraftForge.EVENT_BUS.register(new ActAddCompat());
        }
        if (Loader.isModLoaded("immersiveengineering") && ModConfig.featureControl.enableIECompat)
        {
            IECompat.init();
        }
        if (ModConfig.featureControl.retroReplace)
        {
            MinecraftForge.EVENT_BUS.register(new OreConverter());
        }
    }

}