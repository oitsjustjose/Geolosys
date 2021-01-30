package com.oitsjustjose.geolosys.common.compat.modules.mods;

import java.util.Random;

import com.oitsjustjose.geolosys.common.blocks.Types.Ores;
import com.oitsjustjose.geolosys.common.compat.CompatLoader;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class AppEng {
    private final String AE2_NAMESPACE = "appliedenergistics2";
    private final String CERTUS_QUARTZ_PATH = "certus_quartz_crystal";
    private final String CHARGED_CERTUS_QUARTZ_PATH = "charged_certus_quartz_crystal";

    @SubscribeEvent
    public void registerEvent(BlockEvent.BreakEvent evt) {
        if (evt.getState().getBlock() != Ores.QUARTZ.getBlock() || evt.getPlayer().isCreative()) {
            return;
        }

        Item certusQuartz = ForgeRegistries.ITEMS.getValue(new ResourceLocation(AE2_NAMESPACE, CERTUS_QUARTZ_PATH));
        Item chargedCertusQuartz = ForgeRegistries.ITEMS
                .getValue(new ResourceLocation(AE2_NAMESPACE, CHARGED_CERTUS_QUARTZ_PATH));

        if (certusQuartz == null || chargedCertusQuartz == null) {
            return;
        }

        // Check that the block can legit be broken
        if (!evt.getState().canHarvestBlock(evt.getWorld(), evt.getPos(), evt.getPlayer())) {
            return;
        }

        Random rand = evt.getWorld().getRandom();
        int enchLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,
                evt.getPlayer().getHeldItemMainhand());

        if (rand.nextInt(100) < 25 + (7 * enchLvl)) {
            CompatLoader.injectDrop(evt.getPlayer().getEntityWorld(), evt.getPos(), rand,
                    new ItemStack(certusQuartz, rand.nextInt(2) + rand.nextInt(enchLvl + 1)), false);
        }
        if (rand.nextInt(100) < 10 + (3 * enchLvl)) {
            CompatLoader.injectDrop(evt.getPlayer().getEntityWorld(), evt.getPos(), rand,
                    new ItemStack(chargedCertusQuartz, rand.nextInt(enchLvl + 1)), false);
        }
    }
}