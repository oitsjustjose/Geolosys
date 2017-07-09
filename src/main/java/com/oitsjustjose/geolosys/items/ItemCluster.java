package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.util.Lib;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemCluster extends Item
{
    public static final int META_IRON = 0;
    public static final int META_COPPER = 1;
    public static final int META_TIN = 2;
    public static final int META_SILVER = 3;
    public static final int META_LEAD = 4;
    public static final int META_ALUMINUM = 5;
    public static final int META_NICKEL = 6;
    public static final int META_PLATINUM = 7;
    public static final int META_URANIUM = 8;

    public ItemCluster()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName(new ResourceLocation(Lib.MODID, "cluster"));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.ITEMS.register(this);
        this.registerModels();
        this.registerOreDict();
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
            OreDictionary.registerOre("ore" + EnumType.byMetadata(i).getName().substring(0, 1).toUpperCase() + EnumType.byMetadata(i).getName().substring(1), new ItemStack(this, 1, i));
        if (Geolosys.config.registerAsBauxite)
            OreDictionary.registerOre("oreBauxite", new ItemStack(this, 1, META_ALUMINUM));
    }

    public enum EnumType implements IStringSerializable
    {
        IRON(META_IRON, "iron"),
        COPPER(META_COPPER, "copper"),
        TIN(META_TIN, "tin"),
        SILVER(META_SILVER, "silver"),
        LEAD(META_LEAD, "lead"),
        ALUMINUM(META_ALUMINUM, "aluminum"),
        NICKEL(META_NICKEL, "nickel"),
        PLATINUM(META_PLATINUM, "platinum"),
        URANIUM(META_URANIUM, "uranium");


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
