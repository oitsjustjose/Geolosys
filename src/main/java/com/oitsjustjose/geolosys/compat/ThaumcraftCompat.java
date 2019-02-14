package com.oitsjustjose.geolosys.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.util.Random;

public class ThaumcraftCompat
{
    @SubscribeEvent
    public void registerEvent(HarvestDropsEvent event)
    {
        Random rand = new Random();
        if (rand.nextInt(10) == 0)
        {
            event.getDrops().add(new ItemStack(ModMaterials.TC_ITEM, 1, 10));
        }
    }
}