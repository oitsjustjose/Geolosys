package com.oitsjustjose.geolosys.common.items;

import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;

public class ManualItem extends Item
{
    public static final ResourceLocation REGISTRY_NAME = new ResourceLocation(Constants.MODID, "field_manual");

    public ManualItem()
    {
        super(new Item.Properties().maxStackSize(1).rarity(Rarity.COMMON));
        this.setRegistryName(REGISTRY_NAME);
    }

    // @Override
    // @MethodsReturnNonnullByDefault
    // public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn)
    // {
    // if (playerIn instanceof ClientPlayerEntity)
    // {
    // ClientPlayerEntity player = (ClientPlayerEntity)playerIn;
    // player.open
    // }
    //// playerIn.openGui(Geolosys.getInstance(), ClientGUIProxy.MANUAL_GUI_ID, worldIn, playerIn.getPosition().getX(),
    //// playerIn.getPosition().getY(), playerIn.getPosition().getZ());
    // return new ActionResult<>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    // }
}
