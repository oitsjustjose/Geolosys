package com.oitsjustjose.geolosys.common.event;

import com.oitsjustjose.geolosys.common.config.CommonConfig;
import com.oitsjustjose.geolosys.common.utils.Constants;

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

        Player player = event.getPlayer();
        if (!player.getPersistentData().contains(Constants.MANUAL_GIVE_NBT_KEY)) {
            ItemHandlerHelper.giveItemToPlayer(player,
                    PatchouliAPI.get().getBookStack(new ResourceLocation(Constants.MODID, "field_manual")));
            player.getPersistentData().putBoolean(Constants.MANUAL_GIVE_NBT_KEY, true);
        }
    }
}
