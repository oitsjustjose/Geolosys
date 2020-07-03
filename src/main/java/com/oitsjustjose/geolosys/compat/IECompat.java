package com.oitsjustjose.geolosys.compat;

import java.util.LinkedHashMap;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.api.world.DepositMultiOre;
import com.oitsjustjose.geolosys.common.api.world.IOre;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe;
import blusunrize.immersiveengineering.api.crafting.BlastFurnaceRecipe.BlastFurnaceFuel;
import blusunrize.immersiveengineering.api.crafting.CokeOvenRecipe;
import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.tool.ExcavatorHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class IECompat {
    public static void init() {
        OreDictionary.registerOre("clumpCoal", Items.COAL);
        if (ModConfig.compat.enableAE2Compat && ModMaterials.AE_MATERIAL != null) {
            OreDictionary.registerOre("crystalCertusQuartzCharged", new ItemStack(ModMaterials.AE_MATERIAL, 1, 1));
        }
        initExcavatorCompat();
        initCrusherCompat();
        initBlastCompat();
        initCokeCompat();
    }

    @SuppressWarnings("unchecked")
    private static void initExcavatorCompat() {
        // Remove the vanilla ones
        LinkedHashMap<ExcavatorHandler.MineralMix, Integer> list = (LinkedHashMap<ExcavatorHandler.MineralMix, Integer>) ExcavatorHandler.mineralList
                .clone();
        for (ExcavatorHandler.MineralMix mix : list.keySet()) {
            for (String name : ModConfig.compat.ieExcavatorRecipesToRemove) {
                if (mix.name.equalsIgnoreCase(name)) {
                    ExcavatorHandler.mineralList.remove(mix);
                }
            }
        }
        for (IOre ore : GeolosysAPI.oreBlocks) {
            if (ore instanceof DepositMultiOre) {
                DepositMultiOre tmp = (DepositMultiOre) ore;
                String oreNames[] = new String[tmp.oreBlocks.size()];
                float oreChances[] = new float[tmp.oreBlocks.size()];
                int tally = 0;
                for (IBlockState state : tmp.oreBlocks.keySet()) {
                    ItemStack tempStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
                    String oreName = "depositOre" + tempStack.getDisplayName();
                    OreDictionary.registerOre(oreName, tempStack);
                    oreNames[tally] = oreName;
                    oreChances[tally] = tmp.oreBlocks.get(state);
                    tally++;
                }
                ExcavatorHandler.addMineral(ore.getFriendlyName(), ore.getChance(), .05F, oreNames, oreChances);
            } else {
                ItemStack tempStack = new ItemStack(ore.getOre().getBlock(), 1,
                        ore.getOre().getBlock().getMetaFromState(ore.getOre()));
                String oreName = "deposit" + tempStack.getDisplayName();
                ExcavatorHandler.addMineral(ore.getFriendlyName(), ore.getChance(), .05F, new String[] { oreName },
                        new float[] { 1.0F });
            }
        }
    }

    private static void initCrusherCompat() {
        // Add custom Geolosys entries
        CrusherRecipe crusherRecipe;

        crusherRecipe = new CrusherRecipe(new ItemStack(Items.COAL, 3, 0),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 0), 1000);
        // The next 4 secondary drops are to add some randomization to output number of
        // coal
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.COAL), 0.33F);

        if (OreDictionary.doesOreNameExist("dustSulfur") && OreDictionary.getOres("dustSulfur").size() > 0) {
            ItemStack sulfur = OreDictionary.getOres("dustSulfur").get(0).copy();
            sulfur.setCount(1);
            crusherRecipe = crusherRecipe.addToSecondaryOutput(sulfur, .02F);
        }

        CrusherRecipe.recipeList.add(crusherRecipe);
        crusherRecipe = new CrusherRecipe(new ItemStack(Items.REDSTONE, 5),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 1), 1000);
        // The next 6 secondary drops are to add some randomization to output number of
        // redstone
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.REDSTONE), 0.4F);

        if (ModMaterials.EXU_MATERIAL != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.EXU_MATERIAL), .033F);
        }
        if (ModMaterials.TE_MATERIAL != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.TE_MATERIAL, 1, 866), .0166F);
        }
        if (ModMaterials.NC_GEM != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.NC_GEM, 1, 0), 0.25F);
        }

        CrusherRecipe.recipeList.add(crusherRecipe);
        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 1),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 2), 1000);

        CrusherRecipe.recipeList.add(crusherRecipe);
        crusherRecipe = new CrusherRecipe(new ItemStack(Items.DYE, 6, 4),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 3), 1000);
        // The next 5 secondary drops are to add some randomization to output number of
        // lapis
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.DYE, 1, 4), 0.25F);

        if (ModMaterials.NC_GEM != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.NC_GEM, 1, 2), .95F);
        }
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Items.QUARTZ, 3, 0),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 4), 1000);
        // The next 5 secondary drops are to add some randomization to output number of
        // quartz
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Items.QUARTZ), .2F);

        if (ModConfig.compat.enableAE2Compat && ModMaterials.AE_MATERIAL != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.AE_MATERIAL, 1, 0), .3F);
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.AE_MATERIAL, 1, 1), .12F);
        }
        if (ModMaterials.BLACK_QUARTZ != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.BLACK_QUARTZ, 1, 5), .32F);
        }
        if (ModMaterials.NC_GEM != null) {
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(ModMaterials.NC_DUST, 1, 10), .24F);
        }
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Items.DIAMOND, 1, 0),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 5), 1000);
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Items.EMERALD, 1, 0),
                new ItemStack(Geolosys.getInstance().ORE_VANILLA, 1, 6), 1000);
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 0),
                new ItemStack(Geolosys.getInstance().ORE, 1, 0), 1000);
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 0),
                new ItemStack(Geolosys.getInstance().ORE, 1, 1), 1000);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 7), .4F);

        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 2),
                new ItemStack(Geolosys.getInstance().ORE, 1, 2), 1000);

        CrusherRecipe.recipeList.add(crusherRecipe);
        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 2),
                new ItemStack(Geolosys.getInstance().ORE, 1, 3), 1000);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 2), .4F);
        CrusherRecipe.recipeList.add(crusherRecipe);
        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 3),
                new ItemStack(Geolosys.getInstance().ORE, 1, 4), 1000);
        CrusherRecipe.recipeList.add(crusherRecipe);
        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 3),
                new ItemStack(Geolosys.getInstance().ORE, 1, 5), 1000);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 3), .4F);
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 4),
                new ItemStack(Geolosys.getInstance().ORE, 1, 6), 1000);
        crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 5), 1F);

        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 6),
                new ItemStack(Geolosys.getInstance().ORE, 1, 7), 1000);

        CrusherRecipe.recipeList.add(crusherRecipe);

        if (ModConfig.compat.enableOsmiumExclusively) {
            crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 12),
                    new ItemStack(Geolosys.getInstance().ORE, 1, 8), 1000);
        } else if (ModConfig.compat.enableOsmium) {
            crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 8),
                    new ItemStack(Geolosys.getInstance().ORE, 1, 8), 1000);
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 12),
                    1F);
        } else {
            crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 8),
                    new ItemStack(Geolosys.getInstance().ORE, 1, 8), 1000);
        }

        CrusherRecipe.recipeList.add(crusherRecipe);
        if (ModConfig.compat.enableYellorium) {
            crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 9),
                    new ItemStack(Geolosys.getInstance().ORE, 1, 9), 1000);
            crusherRecipe = crusherRecipe.addToSecondaryOutput(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 11),
                    1F);
        } else {
            crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 9),
                    new ItemStack(Geolosys.getInstance().ORE, 1, 9), 1000);
        }
        CrusherRecipe.recipeList.add(crusherRecipe);

        crusherRecipe = new CrusherRecipe(new ItemStack(Geolosys.getInstance().CLUSTER, 1, 10),
                new ItemStack(Geolosys.getInstance().ORE, 1, 10), 1000);
        CrusherRecipe.recipeList.add(crusherRecipe);
    }

    private static void initBlastCompat() {
        if (ModConfig.featureControl.enableCoals) {
            BlastFurnaceRecipe.blastFuels.add(
                    new BlastFurnaceFuel(new IngredientStack(new ItemStack(Geolosys.getInstance().COAL, 1, 3)), 2400));
            if (Geolosys.getInstance().COAL_COKE != null) {
                BlastFurnaceRecipe.blastFuels.add(new BlastFurnaceFuel(
                        new IngredientStack(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 0)), 1600));
                BlastFurnaceRecipe.blastFuels.add(new BlastFurnaceFuel(
                        new IngredientStack(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 1)), 2400));
            }
        }
    }

    private static void initCokeCompat() {
        if (ModConfig.featureControl.enableCoals) {
            if (Geolosys.getInstance().COAL_COKE != null) {
                CokeOvenRecipe.addRecipe(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 0),
                        new ItemStack(Geolosys.getInstance().COAL, 1, 1), 1500, 750);
                CokeOvenRecipe.addRecipe(new ItemStack(Geolosys.getInstance().COAL_COKE, 1, 1),
                        new ItemStack(Geolosys.getInstance().COAL, 1, 2), 3000, 1000);
            }
        }
    }
}
