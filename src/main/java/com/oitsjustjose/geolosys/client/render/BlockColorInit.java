package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.common.blocks.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.GrassColors;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockColorInit {

    @SubscribeEvent
    public static void registerBlockColors(ColorHandlerEvent.Block evt) {
        BlockColors blockColors = evt.getBlockColors();

        blockColors.register((unknown, lightReader, pos, unknown2) -> lightReader != null && pos != null
                ? BiomeColors.getGrassColor(lightReader, pos)
                : GrassColors.get(0.5D, 1.0D), ModBlocks.getInstance().peat);

    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item evt) {
        final BlockColors blockColors = evt.getBlockColors();
        final ItemColors itemColors = evt.getItemColors();

        // Use the Block's colour handler for an ItemBlock
        IItemColor itemBlockColourHandler = (stack, tintIndex) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
            return blockColors.getColor(state, null, null, tintIndex);
        };

        if (itemBlockColourHandler != null) {
            itemColors.register(itemBlockColourHandler, ModBlocks.getInstance().peat);
        }
    }
}
