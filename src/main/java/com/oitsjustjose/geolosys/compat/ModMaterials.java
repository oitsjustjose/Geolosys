package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemCluster;
import net.minecraft.block.state.IBlockState;
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
    public static Item BLACK_QUARTZ;
    public static Item NC_GEM;
    public static Item NC_DUST;
    public static Item NC_GEM_DUST;

    public static HashMap<String, ItemStack> clusterConverter;
    public static HashMap<String, IBlockState> blockConverter;

    public static void init()
    {
        AE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("appliedenergistics2", "material"));
        EXU_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("extrautils2", "ingredients"));
        TE_MATERIAL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("thermalfoundation", "material"));
        BLACK_QUARTZ = ForgeRegistries.ITEMS.getValue(new ResourceLocation("actuallyadditions", "item_misc"));
        NC_GEM = ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "gem"));
        NC_DUST = ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "dust"));
        NC_GEM_DUST = ForgeRegistries.ITEMS.getValue(new ResourceLocation("nuclearcraft", "gem_dust"));


        clusterConverter = new HashMap<>();
        clusterConverter.put("oreIron", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_IRON));
        clusterConverter.put("oreGold", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_GOLD));
        clusterConverter.put("oreCopper", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_COPPER));
        clusterConverter.put("oreTin", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_TIN));
        clusterConverter.put("oreSilver", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_SILVER));
        clusterConverter.put("oreLead", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_LEAD));
        clusterConverter.put("oreAluminum", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ALUMINUM));
        clusterConverter.put("oreAluminium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ALUMINUM));
        clusterConverter.put("oreNickel", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_NICKEL));
        clusterConverter.put("orePlatinum", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_PLATINUM));
        clusterConverter.put("oreUranium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_URANIUM));
        clusterConverter.put("oreZinc", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ZINC));
        clusterConverter.put("oreYellorium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_YELLORIUM));
        clusterConverter.put("oreOsmium", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_OSMIUM));
        clusterConverter.put("oreSphalerite", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_SPHALERITE));
        if (ModConfig.featureControl.registerAsBauxite)
        {
            clusterConverter.put("oreBauxite", new ItemStack(Geolosys.getInstance().CLUSTER, 1, ItemCluster.META_ALUMINUM));
        }
        blockConverter = new HashMap<>();
        blockConverter.put("oreCoal", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(0));
        blockConverter.put("oreRedstone", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(1));
        blockConverter.put("oreGold", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(2));
        blockConverter.put("oreLapis", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(3));
        blockConverter.put("oreQuartz", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(4));
        blockConverter.put("oreQuartzBlack", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(4));
        blockConverter.put("oreCertusQuartz", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(4));
        blockConverter.put("oreDiamond", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(5));
        blockConverter.put("oreEmerald", Geolosys.getInstance().ORE_VANILLA.getStateFromMeta(6));
        blockConverter.put("oreIron", Geolosys.getInstance().ORE.getStateFromMeta(0));
        blockConverter.put("oreCopper", Geolosys.getInstance().ORE.getStateFromMeta(2));
        blockConverter.put("oreTin", Geolosys.getInstance().ORE.getStateFromMeta(4));
        blockConverter.put("oreSilver", Geolosys.getInstance().ORE.getStateFromMeta(6));
        blockConverter.put("oreLead", Geolosys.getInstance().ORE.getStateFromMeta(6));
        blockConverter.put("oreAluminum", Geolosys.getInstance().ORE.getStateFromMeta(7));
        blockConverter.put("oreAluminium", Geolosys.getInstance().ORE.getStateFromMeta(7));
        blockConverter.put("oreNickel", Geolosys.getInstance().ORE.getStateFromMeta(1));
        blockConverter.put("orePlatinum", Geolosys.getInstance().ORE.getStateFromMeta(8));
        blockConverter.put("oreUranium", Geolosys.getInstance().ORE.getStateFromMeta(9));
        blockConverter.put("oreZinc", Geolosys.getInstance().ORE.getStateFromMeta(10));
        blockConverter.put("oreYellorium", Geolosys.getInstance().ORE.getStateFromMeta(9));
        blockConverter.put("oreOsmium", Geolosys.getInstance().ORE.getStateFromMeta(9));
        blockConverter.put("oreSphalerite", Geolosys.getInstance().ORE.getStateFromMeta(10));
        if (ModConfig.featureControl.registerAsBauxite)
        {
            blockConverter.put("oreBauxite", Geolosys.getInstance().ORE.getStateFromMeta(7));
        }
    }
}
