package com.oitsjustjose.geolosys.common.data.modifiers;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class QuartzDropModifier extends LootModifier {

    private Random rand = new Random();
    private HashMap<Item, Float> quartzes;
    private float chance;

    public QuartzDropModifier(ILootCondition[] conditions, HashMap<Item, Float> quartzes,
            float chance) {
        super(conditions);
        this.quartzes = quartzes;
        this.chance = chance;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> gennedLoot, LootContext ctx) {
        if (rand.nextFloat() < this.chance) {
            gennedLoot.add(new ItemStack(pick(), 1));
        }
        return gennedLoot;
    }

    private Item pick() {
        float rng = rand.nextFloat();
        float minDist = Float.MAX_VALUE;
        Item minDistItem = null;

        for (Entry<Item, Float> e : quartzes.entrySet()) {
            float dist = Math.abs(e.getValue() - rng);
            if (dist < minDist) {
                minDist = dist;
                minDistItem = e.getKey();
            }
        }
        return minDistItem;
    }

    public static class Serializer extends GlobalLootModifierSerializer<QuartzDropModifier> {
        @Override
        public QuartzDropModifier read(ResourceLocation name, JsonObject obj,
                ILootCondition[] cond) {
            JsonArray a = JSONUtils.getJsonArray(obj, "quartzes");
            HashMap<Item, Float> quartzes = new HashMap<Item, Float>();
            float occChance = JSONUtils.getFloat(obj, "chance");

            a.forEach((el) -> {
                JsonObject j = el.getAsJsonObject();
                String iName = j.get("item").getAsString();
                float chance = j.get("chance").getAsFloat();

                Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(iName));
                if (i != null && i != Items.AIR) {
                    quartzes.put(i, chance);
                }
            });

            return new QuartzDropModifier(cond, quartzes, occChance);
        }

        @Override
        public JsonObject write(QuartzDropModifier instance) {
            return null;
        }
    }
}
