package com.oitsjustjose.geolosys.common.util;

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
            if (state.getBlock() == event.getState().getBlock() && state.getBlock().getMetaFromState(state) == event.getState().getBlock().getMetaFromState(event.getState()))
            {
                GeolosysSaveData.get(event.getWorld()).mineralMap.get(new ChunkPos(event.getPos())).remove(event.getPos());
                if (GeolosysSaveData.get(event.getWorld()).mineralMap.get(new ChunkPos(event.getPos())).size() == 0)
                {
                    GeolosysSaveData.get(event.getWorld()).mineralMap.remove(new ChunkPos(event.getPos()));
                }
                GeolosysSaveData.get(event.getWorld()).markDirty();
            }
        }
    }
}
