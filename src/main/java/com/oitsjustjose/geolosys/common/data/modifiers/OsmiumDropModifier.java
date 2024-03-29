package com.oitsjustjose.geolosys.common.data.modifiers;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CompatConfig;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class OsmiumDropModifier extends LootModifier {

    private Random rand = new Random();
    private float chance;
    private Item item;
    private int qty;

    public OsmiumDropModifier(ILootCondition[] conditions, Item item, float chance, int qty) {
        super(conditions);
        this.chance = chance;
        this.item = item;
        this.qty = qty;
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> gennedLoot, LootContext ctx) {
        ItemStack ctxTool = ctx.get(LootParameters.TOOL);
        // ItemStack ctxTool = ctx.getParamOrNull(LootParameters.TOOL);
        if (CompatConfig.ENABLE_OSMIUM.get()) {
            if (CompatConfig.ENABLE_OSMIUM_EXCLUSIVELY.get() || (rand.nextFloat() < this.chance)) {
                if (ctxTool != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, ctxTool) == 0) {
                    gennedLoot.clear();
                    int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, ctxTool);
                    gennedLoot.add(new ItemStack(this.item, this.qty * ctx.getRandom().nextInt(fortune + 1)));
                }
            }
        }

        return gennedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<OsmiumDropModifier> {
        @Override
        public OsmiumDropModifier read(ResourceLocation name, JsonObject obj, ILootCondition[] cond) {
            Item i = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(obj, "item")));
            float chance = JSONUtils.getFloat(obj, "chance");
            int qty = JSONUtils.getInt(obj, "qty");
            return new OsmiumDropModifier(cond, i, chance, qty);
        }

        @Override
        public JsonObject write(OsmiumDropModifier instance) {
            return null;
        }
    }
}
