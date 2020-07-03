package com.oitsjustjose.geolosys.compat;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.BlockOreVanilla;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExUtilsCompat {
    @SubscribeEvent
    public void registerEvent(HarvestDropsEvent event) {
        if (event.getState().getBlock() instanceof BlockOreVanilla) {
            Random rand = new Random();
            int meta = event.getState().getBlock().getMetaFromState(event.getState());
            // Cinnabar
            if (meta == 1) {
                if (ModMaterials.EXU_MATERIAL != null && rand.nextInt(60) < 2) {
                    event.getDrops().add(new ItemStack(ModMaterials.EXU_MATERIAL));
                }
            }
        }
    }
}