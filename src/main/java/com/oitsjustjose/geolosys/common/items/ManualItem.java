package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.client.manual.ManualScreen;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.utils.GeolosysGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ManualItem extends Item
{
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "field_manual");

    public ManualItem()
    {
        super(new Item.Properties().maxStackSize(1).rarity(Rarity.COMMON).group(GeolosysGroup.getInstance()));
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn)
    {
        Minecraft.getInstance().displayGuiScreen(new ManualScreen());
        return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
    }
}
