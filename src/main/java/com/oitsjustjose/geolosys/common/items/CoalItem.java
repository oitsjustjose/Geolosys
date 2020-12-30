package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.common.items.Types.Coals;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CoalItem extends Item {
    private Coals type;

    public CoalItem(Coals type) {
        super(new Properties().group(GeolosysGroup.getInstance()).isImmuneToFire());
        this.type = type;
    }

    @Override
    public int getBurnTime(ItemStack stack) {
        return this.type.getBurnTime();
    }
}
