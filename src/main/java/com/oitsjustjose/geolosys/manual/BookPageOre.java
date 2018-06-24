package com.oitsjustjose.geolosys.manual;

import net.minecraft.item.ItemStack;

public class BookPageOre extends BookPage
{
    private final String description;
    private ItemStack displayStack;
    private String oreType;

    public BookPageOre(String title, String desc, ItemStack stack, String type)
    {
        super(title);
        this.description = desc;
        this.displayStack = stack;
        this.oreType = type;
    }


    public ItemStack getDisplayStack()
    {
        return displayStack.copy();
    }

    public String getDescription()
    {
        return description;
    }

    public String getOreType()
    {
        return this.oreType;
    }

}
