package com.oitsjustjose.geolosys.common.util;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class Recipes {
    public static void init(ConfigOres configOres, final Item CLUSTER) {
        boolean bwm = Loader.isModLoaded("betterwithmods") && ModConfig.compat.enableBWMCompat;

        smeltSafely(new ItemStack(CLUSTER, 1, 0), bwm ? "nuggetIron" : "ingotIron");
        smeltSafely(new ItemStack(CLUSTER, 1, 1), bwm ? "nuggetGold" : "ingotGold");
        smeltSafely(new ItemStack(CLUSTER, 1, 2), bwm ? "nuggetCopper" : "ingotCopper");
        smeltSafely(new ItemStack(CLUSTER, 1, 3), bwm ? "nuggetTin" : "ingotTin");
        smeltSafely(new ItemStack(CLUSTER, 1, 4), bwm ? "nuggetSilver" : "ingotSilver");
        smeltSafely(new ItemStack(CLUSTER, 1, 5), bwm ? "nuggetLead" : "ingotLead");
        smeltSafely(new ItemStack(CLUSTER, 1, 6), bwm ? "nuggetAluminum" : "ingotAluminum");
        smeltSafely(new ItemStack(CLUSTER, 1, 7), bwm ? "nuggetNickel" : "ingotNickel");
        smeltSafely(new ItemStack(CLUSTER, 1, 8), bwm ? "nuggetPlatinum" : "ingotPlatinum");
        smeltSafely(new ItemStack(CLUSTER, 1, 10), bwm ? "nuggetZinc" : "ingotZinc");
        smeltSafely(new ItemStack(CLUSTER, 1, 11), bwm ? "nuggetYellorium" : "ingotYellorium");
        smeltSafely(new ItemStack(CLUSTER, 1, 12), bwm ? "nuggetOsmium" : "ingotOsmium");
    }

    private static void smeltSafely(ItemStack input, String oreDictName) {
        try {
            GameRegistry.addSmelting(input, OreDictionary.getOres(oreDictName).get(0), 0.7F);
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            Geolosys.getInstance().LOGGER.info(oreDictName + " has not been added already. Smelting has been skipped.");
        }
    }
}
