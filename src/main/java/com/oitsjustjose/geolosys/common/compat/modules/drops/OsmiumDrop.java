package com.oitsjustjose.geolosys.common.compat.modules.drops;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.Types.Ores;
import com.oitsjustjose.geolosys.common.compat.CompatLoader;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import com.oitsjustjose.geolosys.common.items.Types.Clusters;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class OsmiumDrop {
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent evt) {
        if (!CompatConfig.ENABLE_OSMIUM.get() || evt.getPlayer().isCreative()) {
            return;
        }

        if (evt.getState().getBlock() != Ores.PLATINUM.getBlock()) {
            return;
        }

        // Check that the block can legit be broken
        if (!evt.getState().canHarvestBlock(evt.getWorld(), evt.getPos(), evt.getPlayer())) {
            return;
        }

        Random rand = evt.getWorld().getRandom();
        if (CompatConfig.ENABLE_OSMIUM_EXCLUSIVELY.get() || rand.nextInt(100) < 50) {
            CompatLoader.injectDrop(evt.getPlayer().getEntityWorld(), evt.getPos(), rand,
                    new ItemStack(Clusters.OSMIUM.getItem()), true);
        }
    }
}