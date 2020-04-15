package com.oitsjustjose.geolosys.common.event;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.BlockInit;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import com.oitsjustjose.geolosys.common.utils.Utils;

import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CoalDrops
{
    @SubscribeEvent
    public void addCoalDrops(BlockEvent.BreakEvent evt)
    {
        if (!CommonConfig.ENABLE_COALS.get())
        {
            return;
        }

        if (!Utils.canMine(evt.getState(), evt.getPlayer().getHeldItemMainhand()))
        {
            return;
        }

        int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,
                evt.getPlayer().getHeldItemMainhand());

        if (evt.getState().getBlock() == BlockInit.getInstance().getModBlocks().get("geolosys:coal_ore"))
        {
            int y = evt.getPos().getY();
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
                int rng = random.nextInt(5 - fortuneLvl);
                if (rng == 0)
                {
                    evt.setResult(Result.DENY);
                    evt.setCanceled(true);

                    evt.getWorld().setBlockState(evt.getPos(), Blocks.AIR.getDefaultState(), 2 | 16);

                    ItemEntity extDrop = new ItemEntity(evt.getWorld().getWorld(), (double) evt.getPos().getX() + 0.5D,
                            (double) evt.getPos().getY(), (double) evt.getPos().getZ() + 0.5D, stackToAdd);
                    extDrop.setPickupDelay(10);
                    evt.getWorld().addEntity(extDrop);
                }
            }
        }
    }
}