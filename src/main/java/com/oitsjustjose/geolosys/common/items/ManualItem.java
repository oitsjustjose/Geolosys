package com.oitsjustjose.geolosys.common.items;

import java.util.List;

import javax.annotation.Nullable;

import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ManualItem extends Item
{
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "field_manual");

    public ManualItem()
    {
        super(new Item.Properties().maxStackSize(1).rarity(Rarity.COMMON));
        this.setRegistryName(REGISTRY_NAME);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
            ITooltipFlag flagIn)
    {
        if (Screen.hasShiftDown())
        {
            tooltip.add(new TranslationTextComponent("geolosys.field_manual.tooltip"));
        }
        tooltip.add(new TranslationTextComponent("geolosys.field_manual.useless"));
    }

}
