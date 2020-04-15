package com.oitsjustjose.geolosys.common.compat;

import java.util.HashSet;

import com.oitsjustjose.geolosys.common.compat.modules.BigReactors;
import com.oitsjustjose.geolosys.common.compat.modules.Mekanism;
import com.oitsjustjose.geolosys.common.compat.modules.Sulfur;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CompatLoader
{
    private HashSet<IDropModule> dropModules;

    public CompatLoader()
    {
        this.dropModules = new HashSet<>();

        this.addModule(new Mekanism());
        this.addModule(new BigReactors());
        this.addModule(new Sulfur());
    }

    public void addModule(IDropModule module)
    {
        this.dropModules.add(module);
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent evt)
    {
        this.dropModules.forEach((module) -> {
            module.process(evt);
        });
    }

}