package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.util.Config;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by jose on 7/7/17.
 */
public class VanillaWorldGenOverride
{
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onOrePopulate(OreGenEvent.GenerateMinable event)
    {
        Config conf = Geolosys.getInstance().config;
        if (conf.modIron && event.getType() == OreGenEvent.GenerateMinable.EventType.IRON)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modGold && event.getType() == OreGenEvent.GenerateMinable.EventType.GOLD)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modDiamond && event.getType() == OreGenEvent.GenerateMinable.EventType.DIAMOND)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modCoal && event.getType() == OreGenEvent.GenerateMinable.EventType.COAL)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modRedstone && event.getType() == OreGenEvent.GenerateMinable.EventType.REDSTONE)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modLapis && event.getType() == OreGenEvent.GenerateMinable.EventType.LAPIS)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modQuartz && event.getType() == OreGenEvent.GenerateMinable.EventType.QUARTZ)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.modStones)
        {
            if (event.getType() == OreGenEvent.GenerateMinable.EventType.ANDESITE || event.getType() == OreGenEvent.GenerateMinable.EventType.GRANITE || event.getType() == OreGenEvent.GenerateMinable.EventType.DIORITE)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
