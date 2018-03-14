package com.oitsjustjose.geolosys.util;

import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Finder
{
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerEvent(PlayerEvent event)
    {
        if (event.getEntityPlayer() == null)
        {
            return;
        }

        if (event.getEntityPlayer().getHeldItemMainhand().getItem() == Items.COMPASS)
        {
            try
            {
                event.getEntityPlayer().sendStatusMessage(new TextComponentString("Altitude: " + (int) event.getEntityPlayer().posY + " | Chunk: " + ((int) event.getEntityPlayer().posX & 15) + ", " + ((int) event.getEntityPlayer().posZ & 15)), true);
            }
            catch (NullPointerException ignored)
            {
            }
        }
    }

    @SubscribeEvent
    public void registerEvent(ItemTooltipEvent event)
    {
        if (event.getItemStack().getItem() == Items.COMPASS)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            {
                event.getToolTip().add(HelperFunctions.getTranslation("compass.tooltip"));
            }
            else
            {
                event.getToolTip().add(HelperFunctions.getTranslation("shift.tooltip"));
            }
        }
    }
}
