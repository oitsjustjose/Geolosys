package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.util.Lib;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemIngot extends Item
{
    public ItemIngot()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName(new ResourceLocation(Lib.MODID, "ingot"));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.ITEMS.register(this);
        this.registerModels();
        this.registerOreDict();
        this.registerSmelting();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> list)
    {
        if (this.isInCreativeTab(tab))
            for (int i = 0; i < EnumType.values().length; ++i)
                list.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "." + EnumType.byMetadata(stack.getMetadata()).getName();
    }

    private void registerModels()
    {
        for (int i = 0; i < EnumType.values().length; i++)
            Geolosys.clientRegistry.register(new ItemStack(this, 1, i), new ResourceLocation(this.getRegistryName().toString() + "_" + EnumType.byMetadata(i).name()), "inventory");
    }

    private void registerOreDict()
    {
        for (int i = 0; i < EnumType.values().length; i++)
            OreDictionary.registerOre("ingot" + EnumType.byMetadata(i).getName().substring(0, 1).toUpperCase() + EnumType.byMetadata(i).getName().substring(1), new ItemStack(this, 1, i));
    }

    private void registerSmelting()
    {
        GameRegistry.addSmelting(new ItemStack(Geolosys.cluster, 1, 0), new ItemStack(Items.IRON_INGOT, 1, 0), 0.7F);
        for (int i = 0; i < EnumType.values().length; i++)
            GameRegistry.addSmelting(new ItemStack(Geolosys.cluster, 1, i + 1), new ItemStack(this, 1, i), 0.7F);
    }


    public enum EnumType implements IStringSerializable
    {
        COPPER(0, "copper"),
        TIN(1, "tin"),
        SILVER(2, "silver"),
        LEAD(3, "lead"),
        ALUMINUM(4, "aluminum");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final String serializedName;
        private final String unlocalizedName;

        EnumType(int meta, String name)
        {
            this.meta = meta;
            this.serializedName = name;
            this.unlocalizedName = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.unlocalizedName;
        }

        public static EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.serializedName;
        }

        static
        {
            for (EnumType type : values())
            {
                META_LOOKUP[type.getMetadata()] = type;
            }
        }
    }

}
