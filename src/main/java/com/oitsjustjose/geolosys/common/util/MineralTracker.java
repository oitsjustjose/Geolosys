package com.oitsjustjose.geolosys.common.util;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MineralTracker
{
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent event)
    {
        long start = System.currentTimeMillis();
        for (IBlockState state : GeolosysAPI.oreBlocks.keySet())
        {
            if (Utils.doStatesMatch(state, event.getState()))
            {
                GeolosysAPI.removeMineralFromMap(event.getPos(), event.getWorld());
            }
        }
        Geolosys.getInstance().LOGGER.info("Processing for " + event.getState() + " took " + (System.currentTimeMillis() - start) + "ms to process");
    }
}
