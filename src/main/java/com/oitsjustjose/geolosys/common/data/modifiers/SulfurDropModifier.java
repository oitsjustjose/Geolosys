package com.oitsjustjose.geolosys.common.data.modifiers;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CompatConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

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

        ItemStack ctxTool = ctx.getParam(LootContextParams.TOOL);

        /* Case for silk-touching ores */
        if (ctxTool != null && !ctxTool.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH,
                ctxTool) > 0) {
            return gennedLoot;
        }

        if (!CompatConfig.ENABLE_SULFUR.get()) {
            return gennedLoot;
        }

        if (rand.nextFloat() < this.chance) {
            gennedLoot.add(new ItemStack(this.item, this.qty));
        }

        return gennedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SulfurDropModifier> {
        @Override
        public SulfurDropModifier read(ResourceLocation name, JsonObject obj, LootItemCondition[] cond) {
            String item = GsonHelper.getAsString(obj, "item");
            float chance = GsonHelper.getAsFloat(obj, "chance");
            int qty = GsonHelper.getAsInt(obj, "qty");

            ResourceLocation itemRes = new ResourceLocation(item);
            Item i = ForgeRegistries.ITEMS.getValue(itemRes);
            if (i == null) {
                i = Items.AIR;
            }

            return new SulfurDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(SulfurDropModifier instance) {
            JsonObject obj = makeConditions(instance.conditions);
            obj.addProperty("item", instance.item.getRegistryName().toString());
            obj.addProperty("chance", instance.chance);
            obj.addProperty("qty", instance.qty);
            return obj;
        }
    }
}
