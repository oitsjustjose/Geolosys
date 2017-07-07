package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by jose on 7/7/17.
 */
public class WorldGenOverride
{
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onOrePopulate(OreGenEvent.GenerateMinable event)
    {
        if (Geolosys.config.disableIron && event.getType() == OreGenEvent.GenerateMinable.EventType.IRON)
        {
            event.setResult(Event.Result.DENY);
        }
        if (event.getType() == OreGenEvent.GenerateMinable.EventType.COAL || event.getType() == OreGenEvent.GenerateMinable.EventType.DIAMOND || event.getType() == OreGenEvent.GenerateMinable.EventType.GOLD || event.getType() == OreGenEvent.GenerateMinable.EventType.REDSTONE || event.getType() == OreGenEvent.GenerateMinable.EventType.LAPIS || event.getType() == OreGenEvent.GenerateMinable.EventType.ANDESITE || event.getType() == OreGenEvent.GenerateMinable.EventType.GRANITE || event.getType() == OreGenEvent.GenerateMinable.EventType.DIORITE)
        {
            event.setResult(Event.Result.DENY);
        }
    }
}
