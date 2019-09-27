package com.oitsjustjose.geolosys.common.utils;

import java.util.ArrayList;

import com.oitsjustjose.geolosys.api.GeolosysAPI;
import com.oitsjustjose.geolosys.common.config.ModConfig;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class Utils
{
    private static ArrayList<BlockState> defaultMatchersCached = null;

    public static String blockStateToName(BlockState state)
    {
        return blockStateToStack(state).getDisplayName().getFormattedText();
    }

    public static ItemStack blockStateToStack(BlockState state)
    {
        return new ItemStack(state.getBlock().asItem(), 1);
    }

    public static boolean doStatesMatch(BlockState state1, BlockState state2)
    {
        return (state1.getBlock().getRegistryName() == state2.getBlock().getRegistryName());
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<BlockState> getDefaultMatchers()
    {
        if (defaultMatchersCached == null)
        {
            defaultMatchersCached = parseMatchersFromConfig();
        }
        return (ArrayList<BlockState>) defaultMatchersCached.clone();
    }

    @SuppressWarnings("deprecation")
    private static ArrayList<BlockState> parseMatchersFromConfig()
    {

        ArrayList<BlockState> ret = new ArrayList<>();
        for (String s : ModConfig.DEFAULT_REPLACEMENT_MATS.get().trim().replace(" ", "").split(","))
        {
            ResourceLocation lookup = new ResourceLocation(s);
            BlockState b = ForgeRegistries.BLOCKS.getValue(lookup).getDefaultState();
            if (b != null && !b.isAir())
            {
                ret.add(b);
            }
        }
        GeolosysAPI.plutonRegistry.getStones().forEach(x -> ret.add(x.getOre()));
        return ret;
    }

    // @SuppressWarnings("deprecation")
    // private static ArrayList<BlockState> parseMatchersFromConfig()
    // {
    // ArrayList<BlockState> ret = new ArrayList<>();
    // for (String s : ModConfig.DEFAULT_REPLACEMENT_MATS.get())
    // {
    // ResourceLocation lookup = new ResourceLocation(s);
    // BlockState b = ForgeRegistries.BLOCKS.getValue(lookup).getDefaultState();
    // if (b != null && !b.isAir())
    // {
    // ret.add(b);
    // }
    // }
    // return ret;
    // }
}