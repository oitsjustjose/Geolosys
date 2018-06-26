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
        if (Loader.isModLoaded("betterwithmods"))
        {
            if (configOres.hematiteChance > 0 || configOres.limoniteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 0), "nuggetIron");
            }
            if (configOres.goldChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 1), "nuggetGold");
            }
            if (configOres.malachiteChance > 0 || configOres.azuriteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 2), "nuggetCopper");
            }
            if (configOres.cassiteriteChance > 0 || configOres.tealliteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 3), "nuggetTin");
            }
            if (configOres.galenaChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 4), "nuggetSilver");
                smeltSafely(new ItemStack(CLUSTER, 1, 5), "nuggetLead");
            }
            if (configOres.bauxiteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 6), "nuggetAluminum");
            }
            if (configOres.limoniteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 7), "nuggetNickel");
            }
            if (configOres.platinumChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 8), "nuggetPlatinum");
            }
            if (configOres.sphaleriteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 10), "nuggetZinc");
            }
            if (ModConfig.featureControl.enableYellorium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 11), "nuggetYellorium");
            }
            if (ModConfig.featureControl.enableOsmium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 12), "nuggetOsmium");
            }
        }
        else
        {
            if (configOres.hematiteChance > 0 || configOres.limoniteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 0), "ingotIron");
            }
            if (configOres.goldChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 1), "ingotGold");
            }
            if (configOres.malachiteChance > 0 || configOres.azuriteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 2), "ingotCopper");
            }
            if (configOres.cassiteriteChance > 0 || configOres.tealliteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 3), "ingotTin");
            }
            if (configOres.galenaChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 4), "ingotSilver");
                smeltSafely(new ItemStack(CLUSTER, 1, 5), "ingotLead");
            }
            if (configOres.bauxiteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 6), "ingotAluminum");
            }
            if (configOres.limoniteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 7), "ingotNickel");
            }
            if (configOres.platinumChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 8), "ingotPlatinum");
            }
            if (configOres.sphaleriteChance > 0)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 10), "ingotZinc");
            }
            if (ModConfig.featureControl.enableYellorium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 11), "ingotYellorium");
            }
            if (ModConfig.featureControl.enableOsmium)
            {
                smeltSafely(new ItemStack(CLUSTER, 1, 12), "ingotOsmium");
            }
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
