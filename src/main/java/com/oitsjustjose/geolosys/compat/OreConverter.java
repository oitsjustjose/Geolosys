package com.oitsjustjose.geolosys.compat;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class OreConverter
{
    @SubscribeEvent
    public void registerEvent(BlockEvent.HarvestDropsEvent event)
    {
        String ore = getOreDictOre(new ItemStack(Item.getItemFromBlock(event.getState().getBlock()), 1, event.getState().getBlock().getMetaFromState(event.getState())));
        if (ore != null)
        {
            if (ModMaterials.converter.containsKey(ore))
            {
                event.getDrops().clear();
                event.getDrops().add(ModMaterials.converter.get(ore));
            }
        }
    }

    private String getOreDictOre(ItemStack stack)
    {
        for (String ore : ModMaterials.converter.keySet())
        {
            for (ItemStack oreStack : OreDictionary.getOres(ore))
            {
                if (oreStack.getItem() == stack.getItem() && oreStack.getMetadata() == stack.getMetadata())
                {
                    return ore;
                }
            }
        }
        return null;
    }
}
