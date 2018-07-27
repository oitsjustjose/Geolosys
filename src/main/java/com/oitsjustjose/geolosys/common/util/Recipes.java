package com.oitsjustjose.geolosys.common.util;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class Recipes
{
    public static void init(ConfigOres configOres, final Item CLUSTER)
    {
        boolean bwm = Loader.isModLoaded("betterwithmods");

        if (configOres.hematite.getChance() > 0 || configOres.limonite.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 0), bwm ? "nuggetIron" : "ingotIron");
        }
        if (configOres.gold.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 1), bwm ? "nuggetGold" : "ingotGold");
        }
        if (configOres.malachite.getChance() > 0 || configOres.azurite.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 2), bwm ? "nuggetCopper" : "ingotCopper");
        }
        if (configOres.cassiterite.getChance() > 0 || configOres.teallite.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 3), bwm ? "nuggetTin" : "ingotTin");
        }
        if (configOres.galena.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 4), bwm ? "nuggetSilver" : "ingotSilver");
            smeltSafely(new ItemStack(CLUSTER, 1, 5), bwm ? "nuggetLead" : "ingotLead");
        }
        if (configOres.bauxite.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 6), bwm ? "nuggetAluminum" : "ingotAluminum");
        }
        if (configOres.limonite.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 7), bwm ? "nuggetNickel" : "ingotNickel");
        }
        if (configOres.platinum.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 8), bwm ? "nuggetPlatinum" : "ingotPlatinum");
        }
        if (configOres.sphalerite.getChance() > 0)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 10), bwm ? "nuggetZinc" : "ingotZinc");
        }
        if (ModConfig.featureControl.enableYellorium)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 11), bwm ? "nuggetYellorium" : "ingotYellorium");
        }
        if (ModConfig.featureControl.enableOsmium)
        {
            smeltSafely(new ItemStack(CLUSTER, 1, 12), bwm ? "nuggetOsmium" : "ingotOsmium");
        }
    }

    private static void smeltSafely(ItemStack input, String oreDictName)
    {
        try
        {
            GameRegistry.addSmelting(input, OreDictionary.getOres(oreDictName).get(0), 0.7F);
        }
        catch (IndexOutOfBoundsException | NullPointerException ex)
        {
            Geolosys.getInstance().LOGGER.info(oreDictName + " has not been added already. Smelting has been skipped.");
        }
    }
}
