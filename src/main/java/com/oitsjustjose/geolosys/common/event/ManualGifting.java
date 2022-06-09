package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Constants;
import com.oitsjustjose.geolosys.common.world.capability.Deposit.IDepositCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.patchouli.api.PatchouliAPI;

public class ManualGifting {
    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!CommonConfig.GIVE_MANUAL_TO_NEW.get()) {
            return;
        }
        PlayerEntity player = event.getPlayer();

        IDepositCapability geolosysCap = event.getEntity().getEntityWorld()
                .getCapability(GeolosysAPI.GEOLOSYS_WORLD_CAPABILITY).orElse(null);
        if (geolosysCap == null) {
            return;
        }

        if (!geolosysCap.hasPlayerReceivedManual(player.getUniqueID())) {
            ItemHandlerHelper.giveItemToPlayer(player,
                    PatchouliAPI.get().getBookStack(new ResourceLocation(Constants.MODID, "field_manual")));
            geolosysCap.setPlayerReceivedManual(player.getUniqueID());
        }
    }
}
