package com.oitsjustjose.geolosys.common.compat;

import net.minecraftforge.event.world.BlockEvent;

public interface IDropModule
{
    void process(BlockEvent.BreakEvent evt);
}