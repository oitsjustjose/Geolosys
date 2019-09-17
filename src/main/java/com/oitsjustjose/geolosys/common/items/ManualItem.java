package com.oitsjustjose.geolosys.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.Rarity;

public class ManualItem extends Item
{
    public ManualItem()
    {
        super(new Item.Properties().maxStackSize(1).rarity(Rarity.COMMON));
    }

}
