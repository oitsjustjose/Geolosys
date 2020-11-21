package com.oitsjustjose.geolosys.common.compat.modules;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class OsmiumDrop extends LootModifier {
    Random rand;

    public OsmiumDrop(ILootCondition[] conditions) {
        super(conditions);
        this.rand = new Random();
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (CompatConfig.ENABLE_OSMIUM.get()) {
            if (CompatConfig.ENABLE_OSMIUM_EXCLUSIVELY.get() || this.rand.nextBoolean()) {
                generatedLoot.removeIf(
                        x -> x.getItem() == ItemInit.getInstance().getModItems().get("geolosys:platinum_cluster"));
                generatedLoot.add(new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:osmium_cluster")));
            }
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<OsmiumDrop> {
        @Override
        public OsmiumDrop read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn) {
            return new OsmiumDrop(conditionsIn);
        }
    }
}