package com.oitsjustjose.geolosys.common.utils.recipes;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.function.Consumer;

public class GeolosysRecipeProvider extends RecipeProvider
{
    public GeolosysRecipeProvider(DataGenerator generatorIn)
    {
        super(generatorIn);
        System.out.println("GeolosysRecipeProvider instantiated");
    }


    @Override
    @ParametersAreNonnullByDefault
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer)
    {
        System.out.println("GeolosysRecipeProvider instantiated");
        Geolosys.getInstance().LOGGER.info("Registering RecipeProvider for Geolosys");
        for (String clusterName : ItemInit.getInstance().getModItems().keySet())
        {
            if (clusterName.toLowerCase().contains("cluster"))
            {
                Item cluster = ItemInit.getInstance().getModItems().get(clusterName);
                String ingotName = clusterName.replace("_cluster", "_ingot");
                Item ingot = ItemInit.getInstance().getModItems().get(ingotName);
                if (ingot == null)
                {
                    //TODO: Register smelting via tag system
                }
                else
                {
                    ResourceLocation id = new ResourceLocation("geolosys",
                            "smelting_" + clusterName.toString().replace("geolosys:", "") + "_to_" + ingotName.toString().replace("geolosys:", ""));
                    String group = id.getPath();
                    Ingredient ingredient = Ingredient.fromItems(cluster);
                    Advancement.Builder advBuilderFurnace = Advancement.Builder.builder().withCriterion("has_" + clusterName.replace("geolosys:", ""), hasItem(Blocks.FURNACE.asItem()));
                    Advancement.Builder advBuilderBlastFurnace = Advancement.Builder.builder().withCriterion("has_" + clusterName.replace("geolosys:", ""), hasItem(Blocks.BLAST_FURNACE.asItem()));

                    CookingRecipeBuilder.smeltingRecipe(ingredient, ingot, 0.7F, 200).addCriterion("has_" + clusterName.replace("geolosys:", ""), hasItem(Blocks.FURNACE.asItem())).build(consumer);
                    CookingRecipeBuilder.blastingRecipe(ingredient, ingot, 0.7F, 100).addCriterion("has_" + clusterName.replace("geolosys:", ""), hasItem(Blocks.BLAST_FURNACE.asItem())).build(consumer);
                    Geolosys.getInstance().LOGGER.info("Should've been able to smelt {} into {}", cluster.getName(), ingot.getName());
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path pathIn)
    {
    }
}
