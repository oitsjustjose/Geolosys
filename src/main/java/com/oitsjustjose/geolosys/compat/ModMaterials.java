package com.oitsjustjose.geolosys.compat;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModMaterials
{
    public static Item AE_MATERIAL;
    public static Item EXU_MATERIAL;
    public static Item TE_MATERIAL;
    public static Item CERTUS_QUARTZ;
    public static Item BLACK_QUARTZ;

    public static void init()
    {
        AE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"));
        EXU_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients"));
        TE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"));
        CERTUS_QUARTZ = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"));
        BLACK_QUARTZ = ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc"));
    }
}
