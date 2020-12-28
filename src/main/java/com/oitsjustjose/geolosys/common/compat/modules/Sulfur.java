// package com.oitsjustjose.geolosys.common.compat.modules;

// import java.util.List;
// import java.util.Random;

// import javax.annotation.Nonnull;

// import com.google.gson.JsonObject;
// import com.oitsjustjose.geolosys.common.config.CompatConfig;

// import net.minecraft.item.Item;
// import net.minecraft.item.ItemStack;
// import net.minecraft.tags.ItemTags;
// import net.minecraft.util.ResourceLocation;
// import net.minecraft.world.storage.loot.LootContext;
// import net.minecraft.world.storage.loot.conditions.ILootCondition;
// import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
// import net.minecraftforge.common.loot.LootModifier;

// public class Sulfur extends LootModifier {
// private Random rand;
// private ItemStack sulfurStack;

// public Sulfur(ILootCondition[] conditions) {
// super(conditions);
// this.rand = new Random();
// this.sulfurStack = findSulfur();
// }

// @Nonnull
// @Override
// public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext
// context) {
// if (CompatConfig.ENABLE_SULFUR.get() && !this.sulfurStack.isEmpty() &&
// this.rand.nextInt(100) < 10) {
// generatedLoot.add(this.sulfurStack);
// }

// return generatedLoot;
// }

// private ItemStack findSulfur() {
// ResourceLocation sulfurTag = new ResourceLocation("forge", "dusts/sulfur");

// if (!ItemTags.getCollection().getTagMap().containsKey(sulfurTag)) {
// return ItemStack.EMPTY;
// }

// if (ItemTags.getCollection().get(sulfurTag).getAllElements().size() > 0) {
// for (Item i : ItemTags.getCollection().get(sulfurTag).getAllElements()) {
// return new ItemStack(i);
// }
// }

// return ItemStack.EMPTY;
// }

// public static class Serializer extends GlobalLootModifierSerializer<Sulfur> {
// @Override
// public Sulfur read(ResourceLocation name, JsonObject object, ILootCondition[]
// conditionsIn) {
// return new Sulfur(conditionsIn);
// }
// }
// }