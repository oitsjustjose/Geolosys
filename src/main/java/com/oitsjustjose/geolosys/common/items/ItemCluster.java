package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemCluster extends Item
{
    public static final int META_IRON = 0;
    public static final int META_GOLD = 1;
    public static final int META_COPPER = 2;
    public static final int META_TIN = 3;
    public static final int META_SILVER = 4;
    public static final int META_LEAD = 5;
    public static final int META_ALUMINUM = 6;
    public static final int META_NICKEL = 7;
    public static final int META_PLATINUM = 8;
    public static final int META_URANIUM = 9;
    public static final int META_ZINC = 10;
    public static final int META_YELLORIUM = 11;
    public static final int META_OSMIUM = 12;
    public static final int META_SPHALERITE = 13;

    public ItemCluster()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "CLUSTER"));
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
        {
            for (int i = 0; i < Types.Cluster.values().length; ++i)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "."
                + Types.Cluster.byMetadata(stack.getMetadata()).getName();
    }

    private void registerModels()
    {
        for (int i = 0; i < Types.Cluster.values().length; i++)
        {
            Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i),
                    new ResourceLocation(this.getRegistryName().toString() + "_" + Types.Cluster.byMetadata(i).name()),
                    "inventory");
        }
    }

    private void registerOreDict()
    {
        for (int i = 0; i < Types.Cluster.values().length; i++)
        {
            OreDictionary.registerOre("ore" + Types.Cluster.byMetadata(i).getName().substring(0, 1).toUpperCase()
                    + Types.Cluster.byMetadata(i).getName().substring(1), new ItemStack(this, 1, i));
        }
        if (ModConfig.featureControl.registerAsBauxite)
        {
            OreDictionary.registerOre("oreBauxite", new ItemStack(this, 1, META_ALUMINUM));
        }
    }
}
