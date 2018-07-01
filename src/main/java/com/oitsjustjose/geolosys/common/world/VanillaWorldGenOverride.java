package com.oitsjustjose.geolosys.common.world;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.ConfigOres;
import com.oitsjustjose.geolosys.common.config.ModConfig;
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
        if ((conf.hematite.getChance() > 0 || conf.limonite.getChance() > 0) && event.getType() == OreGenEvent.GenerateMinable.EventType.IRON)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.gold.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.GOLD)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.kimberlite.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.DIAMOND)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.coal.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.COAL)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.cinnabar.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.REDSTONE)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.lapis.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.LAPIS)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.quartz.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.QUARTZ)
        {
            event.setResult(Event.Result.DENY);
        }
        if (conf.beryl.getChance() > 0 && event.getType() == OreGenEvent.GenerateMinable.EventType.EMERALD)
        {
            event.setResult(Event.Result.DENY);
        }
        if (ModConfig.featureControl.modStones)
        {
            if (event.getType() == OreGenEvent.GenerateMinable.EventType.ANDESITE || event.getType() == OreGenEvent.GenerateMinable.EventType.GRANITE || event.getType() == OreGenEvent.GenerateMinable.EventType.DIORITE)
            {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
