package com.oitsjustjose.geolosys.client;

import com.oitsjustjose.geolosys.client.manual.GUIManual;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.network.PacketStackSurface;
import com.oitsjustjose.geolosys.common.network.PacketStackUnderground;
import com.oitsjustjose.geolosys.common.network.PacketStringSurface;
import com.oitsjustjose.geolosys.common.network.PacketStringUnderground;
import com.oitsjustjose.geolosys.common.utils.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.File;
import java.util.Objects;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        GUIManual.initPages();

        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStringSurface.class, PacketStringSurface::encode, PacketStringSurface::decode, PacketStringSurface::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackSurface.class, PacketStackSurface::encode, PacketStackSurface::decode, PacketStackSurface::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStringUnderground.class, PacketStringUnderground::encode, PacketStringUnderground::decode, PacketStringUnderground::handleClient);
        CommonProxy.networkManager.networkWrapper.registerMessage(CommonProxy.discriminator++, PacketStackUnderground.class, PacketStackUnderground::encode, PacketStackUnderground::decode, PacketStackUnderground::handleClient);
    }

    @Override
    public void throwDownloadError(File jsonFile)
    {
        ModInfo geolosysModInfo = null;
        for (ModInfo info : ModList.get().getMods())
        {
            if (info.getModId().equalsIgnoreCase(Constants.MODID))
            {
                geolosysModInfo = info;
                break;
            }
        }
        throw new ModLoadingException(Objects.requireNonNull(geolosysModInfo), ModLoadingStage.COMMON_SETUP,
                "geolosys.download.error.string", new RuntimeException());
    }

    @Override
    public void sendProspectingMessage(PlayerEntity player, ItemStack stack, Direction direction)
    {
        if (direction != null)
        {
            player.sendStatusMessage(
                    new TranslationTextComponent("geolosys.pro_pick.tooltip.found", stack.getDisplayName(), direction),
                    true);
        }
        else
        {
            player.sendStatusMessage(
                    new TranslationTextComponent("geolosys.pro_pick.tooltip.found_surface", stack.getDisplayName()),
                    true);
        }
    }

    @Override
    public void sendProspectingMessage(PlayerEntity player, String friendlyName, Direction direction)
    {
        if (direction != null)
        {
            player.sendStatusMessage(
                    new TranslationTextComponent("geolosys.pro_pick.tooltip.found", friendlyName, direction), true);
        }
        else
        {
            player.sendStatusMessage(
                    new TranslationTextComponent("geolosys.pro_pick.tooltip.found_surface", friendlyName), true);
        }
    }

}