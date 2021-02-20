package com.oitsjustjose.geolosys.common.compat.modules.drops;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.Types.Ores;
import com.oitsjustjose.geolosys.common.compat.CompatLoader;
import com.oitsjustjose.geolosys.common.config.CompatConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SulfurDrop {
    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent evt) {
        if (!CompatConfig.ENABLE_YELLORIUM.get() || evt.getPlayer().isCreative()) {
            return;
        }

        if (evt.getState().getBlock() != Ores.COAL.getBlock()) {
            return;
        }

        ItemStack sulfur = findSulfur();

        if (sulfur.isEmpty()) {
            return;
        }

        // Check that the block can legit be broken
        if (!evt.getState().canHarvestBlock(evt.getWorld(), evt.getPos(), evt.getPlayer())) {
            return;
        }

        Random rand = evt.getWorld().getRandom();

        if (rand.nextInt(100) < 10 + (10
                * EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, evt.getPlayer().getHeldItemMainhand()))) {
            CompatLoader.injectDrop(evt.getPlayer().getEntityWorld(), evt.getPos(), rand, sulfur, false);
        }
    }

    private ItemStack findSulfur() {
        ResourceLocation sulfurTag = new ResourceLocation("forge", "dusts/sulfur");

        if (ItemTags.getCollection().get(sulfurTag) == null) {
            return ItemStack.EMPTY;
        }

        if (ItemTags.getCollection().get(sulfurTag).getAllElements().size() > 0) {
            return new ItemStack(ItemTags.getCollection().get(sulfurTag).getAllElements().get(0));
        }

        return ItemStack.EMPTY;
    }
}