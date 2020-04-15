package com.oitsjustjose.geolosys.common.compat.modules;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

public class CoalVariants extends LootModifier
{
    Random rand;

    public CoalVariants(ILootCondition[] conditions)
    {
        super(conditions);
        this.rand = new Random();
    }

    @Nonnull
    @Override
    public List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
    {
        if (context.get(LootParameters.THIS_ENTITY) instanceof LivingEntity)
        {
            LivingEntity harvester = (LivingEntity) context.get(LootParameters.THIS_ENTITY);

            if (CommonConfig.ENABLE_COALS.get())
            {
                int y = harvester.getPosition().getY();
                ItemStack stackToAdd = ItemStack.EMPTY;

                if (y <= 12)
                {
                    stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:anthracite_coal"));
                }
                else if (y <= 24)
                {
                    stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:bituminous_coal"));
                }
                else if (y <= 36)
                {
                    stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:lignite_coal"));
                }
                else if (y <= 48)
                {
                    stackToAdd = new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:peat_coal"));
                }

                if (!stackToAdd.isEmpty())
                {
                    int fortuneLvl = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE,
                            harvester.getHeldItemMainhand());

                    if (this.rand.nextInt(5 - fortuneLvl) == 0)
                    {
                        generatedLoot.removeIf(x -> x.getItem() == Items.COAL);
                        generatedLoot.add(stackToAdd);
                    }
                }
            }
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CoalVariants>
    {
        @Override
        public CoalVariants read(ResourceLocation name, JsonObject object, ILootCondition[] conditionsIn)
        {
            return new CoalVariants(conditionsIn);
        }
    }
}