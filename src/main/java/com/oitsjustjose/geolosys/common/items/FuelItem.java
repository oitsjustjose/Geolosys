package com.oitsjustjose.geolosys.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class FuelItem extends Item {

    private final int smeltCount;

    public FuelItem(int smeltCount) {
        super(new Item.Properties().fireResistant());
        this.smeltCount = smeltCount;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.getBurnTime();
    }

    public int getBurnTime() {
        return this.smeltCount * 200;
    }
}
