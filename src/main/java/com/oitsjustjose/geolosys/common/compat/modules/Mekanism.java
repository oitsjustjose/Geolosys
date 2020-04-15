package com.oitsjustjose.geolosys.common.compat.modules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.compat.IDropModule;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;

public class Mekanism implements IDropModule
{
    List<Block> blocks = Arrays.asList(BlockInit.getInstance().getModBlocks().get("geolosys:platinum_ore"),
            BlockInit.getInstance().getModBlocks().get("geolosys:platinum_ore_sample"));

    @Override
    public void process(BlockEvent.BreakEvent evt)
    {
        if (!CompatConfig.ENABLE_OSMIUM.get())
        {
            return;
        }
        if (!Utils.canMine(evt.getState(), evt.getPlayer().getHeldItemMainhand()))
        {
            return;
        }
        if (this.blocks.contains(evt.getState().getBlock()))
        {
            Random rand = new Random();

            if (CompatConfig.ENABLE_OSMIUM_EXCLUSIVELY.get() || rand.nextBoolean())
            {
                // Clear drop
                evt.setResult(Result.DENY);
                evt.setCanceled(true);

                evt.getWorld().setBlockState(evt.getPos(), Blocks.AIR.getDefaultState(), 2 | 16);

                // Then drop osmium itself
                ItemEntity extDrop = new ItemEntity(evt.getWorld().getWorld(), (double) evt.getPos().getX() + 0.5D,
                        (double) evt.getPos().getY(), (double) evt.getPos().getZ() + 0.5D,
                        new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:osmium_cluster")));
                extDrop.setPickupDelay(10);
                evt.getWorld().addEntity(extDrop);
            }
        }
    }
}