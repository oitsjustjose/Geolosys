package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.items.ItemInit;
import com.oitsjustjose.geolosys.common.world.capability.IGeolosysCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

public class ManualGifting {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!CommonConfig.GIVE_MANUAL_TO_NEW.get()) {
            return;
        }
        PlayerEntity player = event.getPlayer();

        IGeolosysCapability geolosysCap = event.getEntity().getEntityWorld()
                .getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY).orElse(null);
        if (geolosysCap == null) {
            return;
        }

        if (!geolosysCap.hasPlayerReceivedManual(player.getUniqueID())) {
            ItemHandlerHelper.giveItemToPlayer(player,
                    new ItemStack(ItemInit.getInstance().getModItems().get("geolosys:field_manual")));
            geolosysCap.setPlayerReceivedManual(player.getUniqueID());
        }
    }
}
