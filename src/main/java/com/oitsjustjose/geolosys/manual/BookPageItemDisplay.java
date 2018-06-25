package com.oitsjustjose.geolosys.manual;
/**
 * @credits: Mangoose /  https://github.com/the-realest-stu/
 * Code taken directly from:
 * https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

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
