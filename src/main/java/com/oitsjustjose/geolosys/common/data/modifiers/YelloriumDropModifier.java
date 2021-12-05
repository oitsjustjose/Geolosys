package com.oitsjustjose.geolosys.common.data.modifiers;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CompatConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class YelloriumDropModifier extends LootModifier {

    private Random rand = new Random();
    private float chance;
    private Item item;
    private int qty;

    public YelloriumDropModifier(LootItemCondition[] conditions, Item item, float chance, int qty) {
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
        public YelloriumDropModifier read(ResourceLocation name, JsonObject obj, LootItemCondition[] cond) {
            Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(obj.get("item").getAsString()));
            float chance = obj.get("chance").getAsFloat();
            int qty = obj.get("qty").getAsInt();
            return new YelloriumDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(YelloriumDropModifier instance) {
            return null;
        }
    }
}
