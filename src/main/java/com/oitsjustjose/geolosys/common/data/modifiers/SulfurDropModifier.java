package com.oitsjustjose.geolosys.common.data.modifiers;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class SulfurDropModifier extends LootModifier {

    private Random rand = new Random();
    private float chance;
    private Item item;
    private int qty;

    public SulfurDropModifier(LootItemCondition[] conditions, Item item, float chance, int qty) {
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
        public SulfurDropModifier read(ResourceLocation name, JsonObject obj, LootItemCondition[] cond) {
            Item i = null;
            float chance = obj.get("chance").getAsFloat();
            int qty = obj.get("qty").getAsInt();

            ResourceLocation tagRes = new ResourceLocation(obj.get("item").getAsString());
            Tag<Item> tag = ItemTags.getAllTags().getTag(tagRes);
            if (tag != null && tag.getValues().size() > 0) {
                i = tag.getValues().get(0);
            }

            return new SulfurDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(SulfurDropModifier instance) {
            return null;
        }
    }
}
