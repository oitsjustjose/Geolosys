package com.oitsjustjose.geolosys.common.util;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.api.GeolosysSaveData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MineralTracker
{
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent event)
    {
        for (IBlockState state : GeolosysAPI.oreBlocks.keySet())
        {
            if (Utils.doStatesMatch(state, event.getState()))
            {
                if(GeolosysAPI.mineralMap.get(new ChunkPos(event.getPos())) != null)
                {
                    GeolosysAPI.mineralMap.get(new ChunkPos(event.getPos())).remove(event.getPos());
                    if (GeolosysAPI.mineralMap.get(new ChunkPos(event.getPos())).size() == 0)
                    {
                        GeolosysAPI.mineralMap.remove(new ChunkPos(event.getPos()));
                    }
                    else
                    {
                        Geolosys.getInstance().LOGGER.info("Didn't remove it because the size was " + GeolosysAPI.mineralMap.get(new ChunkPos(event.getPos())).size() + " expecting:");
                        Geolosys.getInstance().LOGGER.info(GeolosysAPI.mineralMap.get(new ChunkPos(event.getPos())));
                    }
                    GeolosysSaveData.get(event.getWorld()).markDirty();
                }
            }
        }
    }
}
