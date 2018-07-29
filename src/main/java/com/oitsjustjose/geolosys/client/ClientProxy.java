package com.oitsjustjose.geolosys.client;

import com.oitsjustjose.geolosys.common.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);
        GuiManual.initPages();
    }

    @Override
    public void sendProPickMessage(EntityPlayer player, ItemStack stack, String toTranslate)
    {
        if (stack.isEmpty())
        {
            player.sendStatusMessage(new TextComponentString(TranslationManager.getInstance().translate("geolosys.pro_pick.tooltip.nonefound")), true);
        }
        else
        {
            player.sendStatusMessage(new TextComponentString(stack.getDisplayName() + " " + TranslationManager.getInstance().translate(toTranslate)), true);
        }
    }
}
