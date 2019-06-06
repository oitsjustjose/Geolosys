package com.oitsjustjose.geolosys.compat;

import java.util.LinkedHashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.compat.ModMaterials;

import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe.BlastFurnaceFuel;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class IECompat
{
    public static void init()
    {
        ConfigOres conf = Geolosys.getInstance().configOres;
        OreDictionary.registerOre("clumpCoal", Items.COAL);
        if (ModMaterials.AE_MATERIAL != null)
        {
            OreDictionary.registerOre("crystalCertusQuartzCharged", new ItemStack(ModMaterials.AE_MATERIAL, 1, 1));
        }
        initExcavatorCompat(conf);
        initCrusherCompat(conf);
        initBlastCompat();
        initCokeCompat();
    }

    private static void initExcavatorCompat(ConfigOres conf)
    {
        // Remove the vanilla ones
        // LinkedHashMap<ExcavatorHandler.MineralMix, Integer> list = (LinkedHashMap<ExcavatorHandler.MineralMix, Integer>) ExcavatorHandler.mineralList
        //         .clone();
        // for (ExcavatorHandler.MineralMix mix : list.keySet())
        // {
        //     if (mix.name.equalsIgnoreCase("Iron") && (conf.hematite.getChance() > 0 || conf.limonite.getChance() > 0))
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Bauxite") && conf.bauxite.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Cassiterite")
        //             && (conf.cassiterite.getChance() > 0 || conf.teallite.getChance() > 0))
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Coal") && conf.coal.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Copper") && (conf.malachite.getChance() > 0 || conf.azurite.getChance() > 0))
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Galena") && conf.galena.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Gold") && conf.gold.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Lapis") && conf.lapis.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Lead") && conf.galena.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Magnetite")
        //             && (conf.hematite.getChance() > 0 || conf.limonite.getChance() > 0))
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Nickel") && conf.limonite.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Platinum") && conf.platinum.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Pyrite") && (conf.gold.getChance() > 0 || conf.hematite.getChance() > 0))
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Quartzite") && conf.quartz.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Silver") && conf.galena.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //         continue;
        //     }
        //     if (mix.name.equalsIgnoreCase("Uranium") && conf.autunite.getChance() > 0)
        //     {
        //         ExcavatorHandler.mineralList.remove(mix);
        //     }
        // }
    }

    private static void initCrusherCompat(ConfigOres conf)
    {
        // Add custom Geolosys entries
        // ConfigOres oreConf = Geolosys.getInstance().configOres;
        // CrusherRecipe crusherRecipe;
        // if (oreConf.coal.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Coal", Geolosys.getInstance().configOres.coal.getChance(), 0.1F, new String[]
        //     { "oreBlockCoal" }, new float[]
        //     { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Items.COAL, 3, 0),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 0), 1000);
        //     // The next 4 secondary drops are to add some randomization to output number of coal
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);

        //     if (OreDictionary.doesOreNameExist("dustSulfur") && OreDictionary.getOres("dustSulfur").size() > 0)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(OreDictionary.getOres("dustSulfur").get(0), .02F);
        //     }
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.cinnabar.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Cinnabar", Geolosys.getInstance().configOres.cinnabar.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockCinnabar" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Items.REDSTONE, 5),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 1), 1000);
        //     // The next 6 secondary drops are to add some randomization to output number of redstone
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);

        //     if (ModMaterials.EXU_MATERIAL != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.EXU_MATERIAL), .033F);
        //     }
        //     if (ModMaterials.TE_MATERIAL != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.TE_MATERIAL, 1, 866),
        //                 .0166F);
        //     }
        //     if (ModMaterials.NC_GEM != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.NC_GEM, 1, 0), 0.25F);
        //     }

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.gold.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Gold", Geolosys.getInstance().configOres.gold.getChance(), 0.05F, new String[]
        //     { "oreBlockGold" }, new float[]
        //     { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 1),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 2), 1000);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.lapis.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Lapis", Geolosys.getInstance().configOres.lapis.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockLapis" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Items.DYE, 6, 4),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 3), 1000);
        //     // The next 5 secondary drops are to add some randomization to output number of lapis
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);

        //     if (ModMaterials.NC_GEM != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.NC_GEM, 1, 2), .95F);
        //     }
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.quartz.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Quartz", Geolosys.getInstance().configOres.quartz.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockQuartz" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Items.QUARTZ, 3, 0),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 4), 1000);
        //     // The next 5 secondary drops are to add some randomization to output number of quartz
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);

        //     if (ModMaterials.AE_MATERIAL != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.AE_MATERIAL, 1, 0), .3F);
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.AE_MATERIAL, 1, 1), .12F);
        //     }
        //     if (ModMaterials.BLACK_QUARTZ != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.BLACK_QUARTZ, 1, 5),
        //                 .32F);
        //     }
        //     if (ModMaterials.NC_GEM != null)
        //     {
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.NC_DUST, 1, 10), .24F);
        //     }
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.kimberlite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Kimberlite", Geolosys.getInstance().configOres.kimberlite.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockKimberlite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Items.DIAMOND, 1, 0),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 5), 1000);
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.beryl.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Beryl", Geolosys.getInstance().configOres.beryl.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockBeryl" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Items.EMERALD, 1, 0),
        //             new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 6), 1000);
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.hematite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Hematite", Geolosys.getInstance().configOres.hematite.getChance(), 0.25F,
        //             new String[]
        //             { "oreBlockHematite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 0),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 0), 1000);
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.limonite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Limonite", Geolosys.getInstance().configOres.limonite.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockLimonite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 0),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 1), 1000);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 7),
        //             .4F);

        //     CrusherRecipe.recipeList.add(crusherRecipe);

        // }
        // if (oreConf.malachite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Malachite", Geolosys.getInstance().configOres.malachite.getChance(), 0.25F,
        //             new String[]
        //             { "oreBlockMalachite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 2),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 2), 1000);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.azurite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Azurite", Geolosys.getInstance().configOres.azurite.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockAzurite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 2),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 3), 1000);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 2),
        //             .4F);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.cassiterite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Cassiterite", Geolosys.getInstance().configOres.cassiterite.getChance(), 0.25F,
        //             new String[]
        //             { "oreBlockCassiterite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 3),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 4), 1000);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.teallite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Teallite", Geolosys.getInstance().configOres.teallite.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockTeallite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 3),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 5), 1000);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 3),
        //             .4F);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.galena.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Galena", Geolosys.getInstance().configOres.galena.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockGalena" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 4),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 6), 1000);
        //     crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 5), 1F);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.bauxite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Bauxite", Geolosys.getInstance().configOres.bauxite.getChance(), 0.15F,
        //             new String[]
        //             { "oreBlockBauxite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 6),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 7), 1000);

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.platinum.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Platinum", Geolosys.getInstance().configOres.platinum.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockPlatinum" }, new float[]
        //             { 1.0F });

        //     if (ModConfig.featureControl.enableOsmiumExclusively)
        //     {
        //         crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 12),
        //                 new ItemStack(Geolosys.getInstance().ORE, 1, 8), 1000);
        //     }
        //     else if (ModConfig.featureControl.enableOsmium)
        //     {
        //         crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 8),
        //                 new ItemStack(Geolosys.getInstance().ORE, 1, 8), 1000);
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 12),
        //                 1F);
        //     }
        //     else
        //     {
        //         crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 8),
        //                 new ItemStack(Geolosys.getInstance().ORE, 1, 8), 1000);
        //     }

        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.autunite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Autunite", Geolosys.getInstance().configOres.autunite.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockAutunite" }, new float[]
        //             { 1.0F });

        //     if (ModConfig.featureControl.enableYellorium)
        //     {
        //         crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 9),
        //                 new ItemStack(Geolosys.getInstance().ORE, 1, 9), 1000);
        //         crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 11),
        //                 1F);
        //     }
        //     else
        //     {
        //         crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 9),
        //                 new ItemStack(Geolosys.getInstance().ORE, 1, 9), 1000);
        //     }
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }
        // if (oreConf.sphalerite.getChance() > 0)
        // {
        //     ExcavatorHandler.addMineral("Sphalerite", Geolosys.getInstance().configOres.sphalerite.getChance(), 0.05F,
        //             new String[]
        //             { "oreBlockSphalerite" }, new float[]
        //             { 1.0F });

        //     crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 10),
        //             new ItemStack(Geolosys.getInstance().ORE, 1, 10), 1000);
        //     CrusherRecipe.recipeList.add(crusherRecipe);
        // }

    }

    private static void initBlastCompat()
    {
        if (ModConfig.featureControl.enableCoals)
        {
            BlastFurnaceRecipe.blastFuels.add(
                    new BlastFurnaceFuel(new IngredientStack(new ItemStack(Geolosys.getInstance().COAL, 1, 3)), 2400));
            if (Geolosys.getInstance().COAL_COKE != null)
            {
                BlastFurnaceRecipe.blastFuels.add(new BlastFurnaceFuel(
                        new IngredientStack(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 0)), 1600));
                BlastFurnaceRecipe.blastFuels.add(new BlastFurnaceFuel(
                        new IngredientStack(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 1)), 2400));
            }
        }
    }

    private static void initCokeCompat()
    {
        if (ModConfig.featureControl.enableCoals)
        {
            if (Geolosys.getInstance().COAL_COKE != null)
            {
                CokeOvenRecipe.addRecipe(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 0),
                        new ItemStack(Geolosys.getInstance().COAL, 1, 1), 1500, 750);
                CokeOvenRecipe.addRecipe(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 1),
                        new ItemStack(Geolosys.getInstance().COAL, 1, 2), 3000, 1000);
            }
        }
    }
}
