package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.config.ModConfig;
import com.oitsjustjose.geolosys.items.ItemCluster;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashMap;

public class ModMaterials
{
    public static Item AE_MATERIAL;
    public static Item EXU_MATERIAL;
    public static Item TE_MATERIAL;
    public static Item CERTUS_QUARTZ;
    public static Item BLACK_QUARTZ;
    public static HashMap<String, ItemStack> converter;

    public static void init()
    {
        AE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"));
        EXU_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients"));
        TE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"));
        CERTUS_QUARTZ = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"));
        BLACK_QUARTZ = ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc"));

        converter = new HashMap<>();
        converter.put("oreIron", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_IRON));
        converter.put("oreGold", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_GOLD));
        converter.put("oreCopper", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_COPPER));
        converter.put("oreTin", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_TIN));
        converter.put("oreSilver", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_SILVER));
        converter.put("oreLead", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_LEAD));
        converter.put("oreAluminum", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ALUMINUM));
        converter.put("oreAluminium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ALUMINUM));
        converter.put("oreNickel", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_NICKEL));
        converter.put("orePlatinum", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_PLATINUM));
        converter.put("oreUranium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_URANIUM));
        converter.put("oreZinc", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ZINC));
        converter.put("oreYellorium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_YELLORIUM));
        converter.put("oreOsmium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_OSMIUM));
        if (ModConfig.featureControl.registerAsBauxite)
        {
            converter.put("oreBauxite", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ALUMINUM));
        }
    }
}
