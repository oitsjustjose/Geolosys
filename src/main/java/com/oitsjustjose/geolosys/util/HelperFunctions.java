package com.oitsjustjose.geolosys.util;

import com.oitsjustjose.geolosys.Geolosys;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class HelperFunctions
{
    public static IBlockState getStateFromMeta(Block block, int meta)
    {
        try
        {
            return block.getStateForPlacement(null, null, EnumFacing.UP, 0F, 0F, 0F, meta, null, null);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static String getTranslation(String toTranslate)
    {
        String langFile = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        langFile = langFile.substring(0, langFile.indexOf("_")) + langFile.substring(langFile.indexOf("_")).toUpperCase();
        InputStream in = Geolosys.class.getResourceAsStream("/assets/geolosys/lang/" + langFile + ".lang");
        if (in == null)
        {
            langFile = "en_US";
            in = Geolosys.class.getResourceAsStream("/assets/geolosys/lang/" + langFile + ".lang");
        }
        try
        {
            for (String s : IOUtils.readLines(in, "utf-8"))
            {
                if (!s.contains("="))
                {
                    continue;
                }
                if (s.contains(toTranslate))
                {
                    return s.substring(s.indexOf("=") + 1);
                }
            }
        }
        catch (IOException e)
        {
        }
        return toTranslate;
    }

    public static ItemStack blockStateToStack(IBlockState state)
    {
        return new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state));
    }
}
