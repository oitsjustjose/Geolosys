package com.oitsjustjose.geolosys.manual;

import net.minecraft.item.ItemStack;

public class BookPageItemDisplay extends BookPage
{
    private final String description;
    private ItemStack displayStack;

    public BookPageItemDisplay(String title, String desc, ItemStack stack)
    {
        super(title);
        this.description = desc;
        this.displayStack = stack;
    }

    public ItemStack getDisplayStack()
    {
        return displayStack.copy();
    }

    public String getDescription()
    {
        return description;
    }
}
