package com.oitsjustjose.geolosys.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ClientGUIProxy implements IGuiHandler
{
    public static int MANUAL_GUI_ID = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == MANUAL_GUI_ID)
        {
            return null;
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == MANUAL_GUI_ID)
        {
            return new GuiManual();
        }
        return null;
    }
}
