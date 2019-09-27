package com.oitsjustjose.geolosys.client;

import java.io.File;
import java.util.Objects;

import com.oitsjustjose.geolosys.client.manual.GUIManual;
import com.oitsjustjose.geolosys.common.CommonProxy;
import com.oitsjustjose.geolosys.common.utils.Constants;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingException;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

public class ClientProxy extends CommonProxy
{
    public void init()
    {
        GUIManual.initPages();
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
    }
}