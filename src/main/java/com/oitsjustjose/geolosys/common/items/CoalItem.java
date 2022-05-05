package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.common.items.Types.Coals;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CoalItem extends Item {
    private Coals type;

    public CoalItem(Coals type) {
        super(new Item.Properties().tab(GeolosysGroup.getInstance()).fireResistant());
        this.type = type;
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.type.getBurnTime();
    }
}
