package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.Geolosys;
import com.oitsjustjose.geolosys.capability.deposit.DepositCapability;
import com.oitsjustjose.geolosys.capability.deposit.IDepositCapability;
import com.oitsjustjose.geolosys.capability.player.IPlayerCapability;
import com.oitsjustjose.geolosys.capability.player.PlayerCapability;
import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

        try {
            Player player = event.getPlayer();
            IPlayerCapability cap = player.getLevel().getCapability(PlayerCapability.CAPABILITY).orElseThrow(() -> new RuntimeException("Player Capability is Null.."));
            if (!cap.hasManualBeenReceived(player.getUUID())) {
                ItemHandlerHelper.giveItemToPlayer(player,
                        PatchouliAPI.get().getBookStack(new ResourceLocation(Constants.MODID, "field_manual")));
                cap.setManualReceived(player.getUUID());
            }
        } catch (RuntimeException ex) {
            Geolosys.getInstance().LOGGER.error(ex);
        }
    }
}
