package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.blocks.SampleBlock;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.world.BlockEvent;

public class BoringSamples
{
    /**
     * This event is only registered via {@link com.oitsjustjose.geolosys.Geolosys} Assuming that it's only registered assuming
     * config enables it
     */
    public void registerEvent(BlockEvent.HarvestDropsEvent event)
    {
        if (event.getState().getBlock() instanceof SampleBlock)
        {
            PlayerEntity player = event.getHarvester();

            event.getDrops().clear();
            Geolosys.proxy.sendProspectingMessage(player, Utils.blockStateToStack(event.getState()), null);
        }
    }
}