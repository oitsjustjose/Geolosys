package com.oitsjustjose.geolosys.compat;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.BlockOreVanilla;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ActAddCompat
{
    @SubscribeEvent
    public void registerEvent(HarvestDropsEvent event)
    {
        if (event.getState().getBlock() instanceof BlockOreVanilla)
        {
            Random rand = new Random();
            int meta = event.getState().getBlock().getMetaFromState(event.getState());
            // Quartz
            if (meta == 4)
            {
                for (int i = 0; i < 1 + rand.nextInt(event.getFortuneLevel() + 1); i++)
                {
                    if (ModMaterials.BLACK_QUARTZ != null)
                    {
                        int rng = rand.nextInt(25);
                        if (rng >= 6 && rng < 10)
                        {
                            event.getDrops().add(new ItemStack(ModMaterials.BLACK_QUARTZ, 1, 5));
                        }
                    }
                }
            }
        }
    }
}