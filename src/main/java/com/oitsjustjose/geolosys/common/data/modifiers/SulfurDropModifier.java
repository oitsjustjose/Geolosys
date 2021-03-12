package com.oitsjustjose.geolosys.common.data.modifiers;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.common.config.CompatConfig;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class SulfurDropModifier extends LootModifier {

    private Random rand = new Random();
    private float chance;
    private Item item;
    private int qty;

    public SulfurDropModifier(ILootCondition[] conditions, Item item, float chance, int qty) {
        super(conditions);
        this.chance = chance;
        this.item = item;
        this.qty = qty;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> gennedLoot, LootContext ctx) {
        if (this.item == null) {
            return gennedLoot;
        }

        if (CompatConfig.ENABLE_SULFUR.get()) {
            if (rand.nextFloat() < this.chance) {
                gennedLoot.add(new ItemStack(this.item, this.qty));
            }
        }

        return gennedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SulfurDropModifier> {
        @Override
        public SulfurDropModifier read(ResourceLocation name, JsonObject obj, ILootCondition[] cond) {
            Item i = null;
            float chance = JSONUtils.getFloat(obj, "chance");
            int qty = JSONUtils.getInt(obj, "qty");

            ResourceLocation tagRes = new ResourceLocation(JSONUtils.getString(obj, "tag"));
            ITag<Item> tag = ItemTags.getCollection().get(tagRes);
            if (tag != null && tag.getAllElements().size() > 0) {
                i = tag.getAllElements().get(0);
            }

            return new SulfurDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(SulfurDropModifier instance) {
            return null;
        }
    }
}
