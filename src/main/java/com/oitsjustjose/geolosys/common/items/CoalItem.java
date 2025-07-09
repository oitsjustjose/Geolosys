package com.oitsjustjose.geolosys.common.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CoalItem extends Item {

    private final int burnTime;

    public CoalItem(int burnTime) {
        super(new Item.Properties().fireResistant());
        this.burnTime = burnTime;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.getBurnTime();
    }

    public int getBurnTime() {
        return this.burnTime;
    }
}
