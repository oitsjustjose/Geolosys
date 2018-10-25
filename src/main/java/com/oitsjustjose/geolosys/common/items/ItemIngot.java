package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.Geolosys;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class ItemIngot extends Item
{
    public ItemIngot()
    {
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "INGOT"));
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
            for (int i = 0; i < Types.Ingot.values().length; ++i)
            {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().replaceAll(":", ".") + "."
                + Types.Ingot.byMetadata(stack.getMetadata()).getName();
    }

    private void registerModels()
    {
        for (int i = 0; i < Types.Ingot.values().length; i++)
        {
            Geolosys.getInstance().clientRegistry.register(new ItemStack(this, 1, i),
                    new ResourceLocation(this.getRegistryName().toString() + "_" + Types.Ingot.byMetadata(i).name()),
                    "inventory");
        }
    }

    private void registerOreDict()
    {
        for (int i = 0; i < Types.Ingot.values().length; i++)
        {
            OreDictionary.registerOre("ingot" + Types.Ingot.byMetadata(i).getName().substring(0, 1).toUpperCase()
                    + Types.Ingot.byMetadata(i).getName().substring(1), new ItemStack(this, 1, i));
        }
    }
}
