package com.oitsjustjose.geolosys.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.config.Config;
import com.oitsjustjose.geolosys.config.ConfigOres;
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
        ConfigOres conf = Geolosys.getInstance().configOres;
        if ((conf.hematiteChance > 0 || conf.limoniteChance > 0) && event.getType() == OreGenEvent.GenerateMinable.EventType.IRON)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.goldChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.GOLD)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.kimberliteChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.DIAMOND)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.coalChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.COAL)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.cinnabarChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.REDSTONE)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.lapisChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.LAPIS)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.quartzChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.QUARTZ)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.berylChance > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.EMERALD)
        {
            event.setResult(Event.Result.DENY);
        }
        if (Config.getInstance().modStones)
        {
            if (event.getType() == OreGenEvent.GenerateMinable.EventType.ANDESITE || event.getType() == OreGenEvent.GenerateMinable.EventType.GRANITE || event.getType() == OreGenEvent.GenerateMinable.EventType.DIORITE)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
