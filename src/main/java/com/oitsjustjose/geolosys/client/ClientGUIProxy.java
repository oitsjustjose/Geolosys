package com.oitsjustjose.geolosys.client;

//import com.oitsjustjose.geolosys.client.manual.GUIManual;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ClientGUIProxy implements IGuiHandler
{
    public static int MANUAL_GUI_ID = 0;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
    {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z)
    {
        if (ID == MANUAL_GUI_ID)
        {
//            return new GUIManual();
        }
        return null;
    }
}
