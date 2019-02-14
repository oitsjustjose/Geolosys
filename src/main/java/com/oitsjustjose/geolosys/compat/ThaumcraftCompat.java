package com.oitsjustjose.geolosys.compat;

import com.oitsjustjose.geolosys.common.blocks.BlockOre;
import com.oitsjustjose.geolosys.common.blocks.BlockOreVanilla;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.util.Random;

public class ThaumcraftCompat
{
    @SubscribeEvent
    public void registerEvent(HarvestDropsEvent event)
    {
        if (!(event.getState().getBlock() instanceof BlockOre
                || event.getState().getBlock() instanceof BlockOreVanilla))
        {
            return;
        }
        Random rand = new Random();
        if (rand.nextInt(20) == 0)
        {
            event.getDrops().add(new ItemStack(ModMaterials.TC_ITEM, 1, 10));
        }
    }
}