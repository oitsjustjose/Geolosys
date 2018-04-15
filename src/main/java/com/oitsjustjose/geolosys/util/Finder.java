package com.oitsjustjose.geolosys.util;

import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
                StringBuilder status = new StringBuilder();
                if (event.getEntityPlayer().posY < event.getEntityPlayer().getEntityWorld().getTopSolidOrLiquidBlock(event.getEntityPlayer().getPosition()).getY())
                {
                    status.append("Depth: ").append((int) event.getEntityPlayer().posY).append(" | ");
                }
                status.append("Chunk: ").append((int) Math.floor(event.getEntityPlayer().posX) & 15).append(", ").append((int) Math.floor(event.getEntityPlayer().posZ) & 15);
                event.getEntityPlayer().sendStatusMessage(new TextComponentString(status.toString()), true);
            }
            catch (NullPointerException ignored)
            {
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
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
