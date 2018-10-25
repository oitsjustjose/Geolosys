package com.oitsjustjose.geolosys.common.manual;

import net.minecraft.item.ItemStack;

/**
 * @author oitsjustjose,
 * @author Mangoose / https://github.com/the-realest-stu/ Code inspired directly
 *         from:
 *         https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

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
