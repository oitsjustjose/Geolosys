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
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modGold && event.getType() == OreGenEvent.GenerateMinable.EventType.GOLD)
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modDiamond && event.getType() == OreGenEvent.GenerateMinable.EventType.DIAMOND)
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modCoal && event.getType() == OreGenEvent.GenerateMinable.EventType.COAL)
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modRedstone && event.getType() == OreGenEvent.GenerateMinable.EventType.REDSTONE)
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modLapis && event.getType() == OreGenEvent.GenerateMinable.EventType.LAPIS)
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modQuartz && event.getType() == OreGenEvent.GenerateMinable.EventType.QUARTZ)
            event.setResult(Event.Result.DENY);
        if (Geolosys.config.modStones)
            if (event.getType() == OreGenEvent.GenerateMinable.EventType.ANDESITE || event.getType() == OreGenEvent.GenerateMinable.EventType.GRANITE || event.getType() == OreGenEvent.GenerateMinable.EventType.DIORITE)
                event.setResult(Event.Result.DENY);
    }
}
