package com.oitsjustjose.geolosys.client.manual.pages;

import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * @author Mangoose / https://github.com/the-realest-stu/ Code taken directly
 * from:
 * https://github.com/the-realest-stu/Adventurers-Toolbox/tree/master/src/main/java/api/guide
 */

public class BookPageItemDisplay extends BookPage
{
    private final String description;
    private ItemStack displayStack;
    private ItemStack[] displayStacks;
    private long lastUpdate;


    public BookPageItemDisplay(String title, String desc, ItemStack stack)
    {
        super(title);
        this.description = desc;
        this.displayStack = stack;
        this.displayStacks = null;
    }

    public BookPageItemDisplay(String title, String desc, ItemStack[] stacks)
    {
        super(title);
        this.description = desc;
        this.displayStack = null;
        this.displayStacks = stacks;
    }

    public ItemStack getDisplayStack()
    {
        if (this.displayStacks != null)
        {
            if (this.displayStacks.length == 1)
            {
                this.displayStack = this.displayStacks[0];
            }
            if (System.currentTimeMillis() - this.lastUpdate >= 1000)
            {
                Random rand = new Random();
                this.lastUpdate = System.currentTimeMillis();
                this.displayStack = this.displayStacks[rand.nextInt(this.displayStacks.length)];
            }
        }
        return displayStack.copy();
    }


    public String getDescription()
    {
        return description;
    }
}
