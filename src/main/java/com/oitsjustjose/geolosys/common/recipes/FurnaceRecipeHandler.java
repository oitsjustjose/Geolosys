package com.oitsjustjose.geolosys.common.recipes;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.oitsjustjose.geolosys.common.items.ItemInit;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;

public class FurnaceRecipeHandler {
    final Field RECIPES = ObfuscationReflectionHelper.findField(RecipeManager.class, "field_199522_d");

    @SubscribeEvent
    public void onServerStart(final FMLServerStartedEvent event) {
        try {
            @SuppressWarnings("unchecked")
            Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> existingRecipes = Maps
                    .newHashMap((Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>>) RECIPES
                            .get(event.getServer().getRecipeManager()));

            BlastingRecipe bRecipe = new BlastingRecipe(
                    new ResourceLocation("geolosys", "gold_nuggets_x_4_from_blasting"),
                    "geolosys:nether_gold_cluster_blasting",
                    Ingredient.fromStacks(
                            new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:nether_gold_cluster"))),
                    new ItemStack(Items.GOLD_NUGGET, 4), 0.15F, 100);
            FurnaceRecipe fRecipe = new FurnaceRecipe(
                    new ResourceLocation("geolosys", "gold_nuggets_x_3_from_smelting"),
                    "geolosys:nether_gold_cluster_smelting",
                    Ingredient.fromStacks(
                            new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:nether_gold_cluster"))),
                    new ItemStack(Items.GOLD_NUGGET, 3), 0.15F, 100);

            existingRecipes = injectRecipe(existingRecipes, IRecipeType.BLASTING,
                    new ResourceLocation("geolosys", "blasting_with_count"), bRecipe);
            existingRecipes = injectRecipe(existingRecipes, IRecipeType.SMELTING,
                    new ResourceLocation("geolosys", "smelting_with_count"), fRecipe);

            RECIPES.set(event.getServer().getRecipeManager(), ImmutableMap.copyOf(existingRecipes));
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Couldn't get recipes map while removing recipes", e);
        }
    }

    Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> injectRecipe(
            Map<IRecipeType<?>, Map<ResourceLocation, IRecipe<?>>> existing, IRecipeType<?> type, ResourceLocation res,
            IRecipe<?> recipe) {

        if (existing.get(type).get(res) == null) {
            // Unlock the map for the given recipe type
            Map<ResourceLocation, IRecipe<?>> existingRecipesForType = Maps.newHashMap(existing.get(type));
            existingRecipesForType.put(res, recipe);
            // Lock the map again, return it
            existing.put(type, ImmutableMap.copyOf(existingRecipesForType));
            return existing;
        }

        return existing;
    }
}
