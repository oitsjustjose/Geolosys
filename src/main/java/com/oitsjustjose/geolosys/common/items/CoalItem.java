package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CoalItem extends Item {

    private int burnTime;

    public CoalItem(int burnTime) {
        super(new Item.Properties().tab(GeolosysGroup.getInstance()).fireResistant());
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.getBurnTime();
    }

    public int getBurnTime() {
        return this.burnTime;
    }
}
