package com.oitsjustjose.geolosys.compat;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.BlockOreVanilla;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NCCompat {
    @SubscribeEvent
    public void registerEvent(HarvestDropsEvent event) {

        if (event.getState().getBlock() instanceof BlockOreVanilla) {
            if (ModMaterials.NC_DUST == null || ModMaterials.NC_GEM == null || ModMaterials.NC_GEM_DUST == null) {
                return;
            }
            Random rand = new Random();
            int meta = event.getState().getBlock().getMetaFromState(event.getState());
            // Coal
            if (meta == 0) {
                if (rand.nextInt(100) < 18) {
                    event.getDrops().add(new ItemStack(ModMaterials.NC_DUST, 1, 9));
                }
            }
            // Cinnabar
            else if (meta == 1) {
                if (rand.nextInt(4) == 0) {
                    event.getDrops().add(new ItemStack(ModMaterials.NC_GEM, 1, 0));
                }
            }
            // Lapis
            else if (meta == 3) {
                if (rand.nextInt(100) < 95) {
                    event.getDrops().add(new ItemStack(ModMaterials.NC_GEM, rand.nextInt(2) + 1, 2));
                }
            }
            // Quartz
            else if (meta == 4) {
                for (int i = 0; i < 1 + rand.nextInt(event.getFortuneLevel() + 1); i++) {
                    if (rand.nextInt(50) < 9) {
                        event.getDrops().add(new ItemStack(ModMaterials.NC_DUST, 1, 10));
                    }
                }
            }
        }
    }
}