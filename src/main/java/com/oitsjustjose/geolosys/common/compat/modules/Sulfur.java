package com.oitsjustjose.geolosys.common.compat.modules;

import java.util.Objects;

import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.compat.IDropModule;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;

public class Sulfur implements IDropModule
{
    @Override
    public void process(BlockEvent.BreakEvent event)
    {
        if (!CompatConfig.ENABLE_SULFUR.get())
        {
            return;
        }
        if (!Utils.canMine(event.getState(), event.getPlayer().getHeldItemMainhand()))
        {
            return;
        }
        if (event.getState().getBlock() == BlockInit.getInstance().getModBlocks().get("geolosys:coal_ore"))
        {
            ResourceLocation sulfurTag = new ResourceLocation("forge", "dusts/sulfur");

            if (!ItemTags.getCollection().getTagMap().containsKey(sulfurTag))
            {
                return;
            }

            if (ItemTags.getCollection().get(sulfurTag).getAllElements().size() > 0)
            {
                for (Item i : ItemTags.getCollection().get(sulfurTag).getAllElements())
                {
                    ItemEntity extDrop = new ItemEntity(event.getWorld().getWorld(),
                            (double) event.getPos().getX() + 0.5D, (double) event.getPos().getY(),
                            (double) event.getPos().getZ() + 0.5D, new ItemStack(i));
                    extDrop.setPickupDelay(10);
                    event.getWorld().addEntity(extDrop);
                    break;
                }
            }
        }
    }
}