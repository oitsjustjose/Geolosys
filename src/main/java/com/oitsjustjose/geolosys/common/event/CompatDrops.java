package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.compat.ConfigCompat;
import com.oitsjustjose.geolosys.common.config.ModConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Objects;
import java.util.Random;

public class CompatDrops
{
    @SubscribeEvent
    public void addCoalDrops(BlockEvent.HarvestDropsEvent event)
    {
        if (!ModConfig.ENABLE_COALS.get())
        {
            return;
        }
        if (event.getState().getBlock() == BlockInit.getInstance().getModBlocks().get("geolosys:coal_ore"))
        {
            int y = event.getPos().getY();
            ItemStack stackToAdd = null;
            if (y <= 12)
            {
                // anthracite
                stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:anthracite_coal"));
            }
            else if (y <= 24)
            {
                // bitumen
                stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:bituminous_coal"));
            }
            else if (y <= 36)
            {
                // lignite
                stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:lignite_coal"));
            }
            else if (y <= 48)
            {
                // peat
                stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:peat_coal"));
            }
            if (stackToAdd != null)
            {
                Random random = new Random();
                int rng = random.nextInt(5 - event.getFortuneLevel());
                if (rng == 0)
                {
                    Geolosys.getInstance().LOGGER.info("should've added");

                    event.getDrops().add(stackToAdd);
                }
            }
        }
    }

    @SubscribeEvent
    public void addSulfurDrops(BlockEvent.HarvestDropsEvent event)
    {
        if (!ConfigCompat.ENABLE_SULFUR.get())
        {
            return;
        }
        if (event.getState().getBlock() == BlockInit.getInstance().getModBlocks().get("geolosys:coal_ore"))
        {
            ResourceLocation sulfurTag = new ResourceLocation("forge", "dusts/sulfur");
            if (Objects.requireNonNull(ItemTags.getCollection().get(sulfurTag)).getAllElements().size() > 0)
            {
                for (Item i : Objects.requireNonNull(ItemTags.getCollection().get(sulfurTag)).getAllElements())
                {
                    event.getDrops().add(new ItemStack(i));
                    break;
                }
            }
        }

    }

    @SubscribeEvent
    public void handleOsmiumDrops(BlockEvent.HarvestDropsEvent event)
    {
        if (!ConfigCompat.ENABLE_OSMIUM.get())
        {
            return;
        }
        if (event.getState().getBlock() == BlockInit.getInstance().getModBlocks().get("geolosys:platinum_ore"))
        {
            Random rand = new Random();
            if (ConfigCompat.ENABLE_OSMIUM_EXCLUSIVELY.get() || rand.nextBoolean())
            {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:osmium_cluster")));
            }
        }
    }

    @SubscribeEvent
    public void addYelloriumDrops(BlockEvent.HarvestDropsEvent event)
    {
        if (!ConfigCompat.ENABLE_YELLORIUM.get())
        {
            return;
        }
        if (event.getState().getBlock() == BlockInit.getInstance().getModBlocks().get("geolosys:autunite_ore"))
        {
            Random rand = new Random();
            if (rand.nextBoolean())
            {
                event.getDrops().clear();
                event.getDrops().add(new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:yellorium_cluster")));
            }
        }
    }
}
