package com.oitsjustjose.geolosys.items;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.util.HelperFunctions;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemFieldManual extends Item
{
    public ItemFieldManual()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.TOOLS);
        this.setRegistryName(new ResourceLocation(Geolosys.MODID, "FIELD_MANUAL"));
        this.setUnlocalizedName(this.getRegistryName().toString().replaceAll(":", "."));
        ForgeRegistries.ITEMS.register(this);
        this.registerModel();
    }

    private void registerModel()
    {
        Geolosys.getInstance().clientRegistry.register(new ItemStack(this), new ResourceLocation(this.getRegistryName().toString()), "inventory");
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().replaceAll(":", ".");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack)
    {
        return HelperFunctions.getTranslation(getUnlocalizedName(stack));
    }


    @Override
    @MethodsReturnNonnullByDefault
    @SuppressWarnings("unchecked")
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn)
    {
        playerIn.openGui(Geolosys.getInstance(), 0, worldIn, playerIn.getPosition().getX(), playerIn.getPosition().getY(), playerIn.getPosition().getZ());
        return new ActionResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
