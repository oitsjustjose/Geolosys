package com.oitsjustjose.geolosys.util;

import com.oitsjustjose.geolosys.items.ItemProPick;
import net.minecraft.init.Items;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class DepthFinder
{
    private long lastCheckedTime = System.currentTimeMillis();
    private boolean hasCompass = false;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void registerEvent(PlayerEvent event)
    {
        if (event.getEntityPlayer() == null)
        {
            return;
        }
        if (event.getEntityPlayer().getHeldItemMainhand().getItem() instanceof ItemProPick || event.getEntityPlayer().getHeldItemOffhand().getItem() instanceof ItemProPick)
        {
            return;
        }
        // Update every 1 second
        if (System.currentTimeMillis() - lastCheckedTime >= 1000)
        {
            for (int i = 0; i < 10; i++)
            {
                if (event.getEntityPlayer().inventory.getStackInSlot(i).getItem() == Items.COMPASS)
                {
                    hasCompass = true;
                    break;
                }
                else
                {
                    hasCompass = false;
                }
            }
            lastCheckedTime = System.currentTimeMillis();
        }
        if (hasCompass)
        {
            try
            {
                String status = "Depth: " + (int) event.getEntityPlayer().posY + " | " +
                        "Chunk: " + ((int) Math.floor(event.getEntityPlayer().posX) & 15) + ", " + ((int) Math.floor(event.getEntityPlayer().posZ) & 15);
                event.getEntityPlayer().sendStatusMessage(new TextComponentString(status), true);
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
