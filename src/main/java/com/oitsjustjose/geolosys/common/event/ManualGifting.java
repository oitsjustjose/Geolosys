package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.common.items.ItemInit;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ManualGifting
{
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event)
    {
        PlayerEntity player = event.getPlayer();
        CompoundNBT tag = player.getPersistentData();
        if (!tag.contains("geolosys:has_manual"))
        {
            player.addItemStackToInventory(new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:field_manual")));
            tag.putBoolean("geolosys:has_manual", true);
        }
    }
}
