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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class OsmiumDropModifier extends LootModifier {

    private Random rand = new Random();
    private float chance;
    private Item item;
    private int qty;

    public OsmiumDropModifier(LootItemCondition[] conditions, Item item, float chance, int qty) {
        super(conditions);
        this.chance = chance;
        this.item = item;
        this.qty = qty;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> gennedLoot, LootContext ctx) {
        ItemStack ctxTool = ctx.getParam(LootContextParams.TOOL);

        /* Case for silk-touching ores */
        if (ctxTool != null && !ctxTool.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH,
                ctxTool) > 0) {
            return gennedLoot;
        }

        if (!CompatConfig.ENABLE_OSMIUM.get()) {
            return gennedLoot;
        }

        if (CompatConfig.ENABLE_OSMIUM_EXCLUSIVELY.get() || (rand.nextFloat() < this.chance)) {
            gennedLoot.clear();
            int fortune = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE,
                    ctxTool);
            int count = fortune > 0 ? ctx.getRandom().nextInt(fortune) + 1 : this.qty;
            ItemStack stack = new ItemStack(this.item, count);
            gennedLoot.add(stack);
        }

        return gennedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<OsmiumDropModifier> {
        @Override
        public OsmiumDropModifier read(ResourceLocation name, JsonObject obj, LootItemCondition[] cond) {
            Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(obj, "item")));
            float chance = GsonHelper.getAsFloat(obj, "chance");
            int qty = GsonHelper.getAsInt(obj, "qty");
            return new OsmiumDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(OsmiumDropModifier instance) {
            JsonObject obj = makeConditions(instance.conditions);
            obj.addProperty("item", instance.item.getRegistryName().toString());
            obj.addProperty("chance", instance.chance);
            obj.addProperty("qty", instance.qty);
            return obj;
        }
    }
}
