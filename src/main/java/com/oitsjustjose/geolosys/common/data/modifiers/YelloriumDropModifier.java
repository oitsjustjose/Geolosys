package com.oitsjustjose.geolosys.common.data.modifiers;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CompatConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class YelloriumDropModifier extends LootModifier {

    private Random rand = new Random();
    private float chance;
    private Item item;
    private int qty;

    public YelloriumDropModifier(ILootCondition[] conditions, Item item, float chance, int qty) {
        super(conditions);
        this.chance = chance;
        this.item = item;
        this.qty = qty;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> gennedLoot, LootContext ctx) {

        if (CompatConfig.ENABLE_YELLORIUM.get()) {
            if ((rand.nextFloat() < this.chance)) {
                gennedLoot.clear();
                gennedLoot.add(new ItemStack(this.item, this.qty));
            }
        }

        return gennedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<YelloriumDropModifier> {
        @Override
        public YelloriumDropModifier read(ResourceLocation name, JsonObject obj, ILootCondition[] cond) {
            Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(obj, "item")));
            float chance = JSONUtils.getFloat(obj, "chance");
            int qty = JSONUtils.getInt(obj, "qty");
            return new YelloriumDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(YelloriumDropModifier instance) {
            return null;
        }
    }
}
