package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class CompatLoader {
    private Logger logger;

    public static void init() {
        new CompatLoader();
    }

    public CompatLoader() {
        ModMaterials.init();
        this.logger = Geolosys.getInstance().LOGGER;
        this.initCompats();
    }

    private void initCompats() {
        if (Loader.isModLoaded("nuclearcraft")) {
            this.logger.info("Loading nuclearcraft compatibility");
            MinecraftForge.EVENT_BUS.register(new NCCompat());
        }
        if (Loader.isModLoaded("extrautils2")) {
            this.logger.info("Loading Extra Utilities compatibility");
            MinecraftForge.EVENT_BUS.register(new ExUtilsCompat());
        }
        if (Loader.isModLoaded("appliedenergistics2") && ModConfig.compat.enableAE2Compat) {
            this.logger.info("Loading Applied Energistics compatibility");
            MinecraftForge.EVENT_BUS.register(new AppEngCompat());
        }
        if (Loader.isModLoaded("thermalfoundation")) {
            this.logger.info("Loading Thermal Foundation compatibility");
            MinecraftForge.EVENT_BUS.register(new ThermExpCompat());
        }
        if (Loader.isModLoaded("thaumcraft")) {
            this.logger.info("Loading Thaumcraft compatibility");
            MinecraftForge.EVENT_BUS.register(new ThaumcraftCompat());
        }
        if (Loader.isModLoaded("actuallyadditions")) {
            this.logger.info("Loading Actually Additions compatibility");
            MinecraftForge.EVENT_BUS.register(new ActAddCompat());
        }
        if (Loader.isModLoaded("immersiveengineering") && ModConfig.compat.enableIECompat) {
            this.logger.info("Loading Immersive Engineering compatibility");
            IECompat.init();
        }
        if (ModConfig.featureControl.retroReplace) {
            MinecraftForge.EVENT_BUS.register(new OreConverter());
        }
    }

}