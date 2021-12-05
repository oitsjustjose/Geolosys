package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.common.blocks.ModBlocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
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
                ? BiomeColors.getAverageGrassColor(lightReader, pos)
                : GrassColor.get(0.5D, 1.0D), ModBlocks.getInstance().peat);
        blockColors.register((unknown, lightReader, pos, unknown2) -> lightReader != null && pos != null
                ? BiomeColors.getAverageGrassColor(lightReader, pos)
                : GrassColor.get(0.5D, 1.0D), ModBlocks.getInstance().rhododendron);

    }

    @SubscribeEvent
    public static void registerItemColors(ColorHandlerEvent.Item evt) {
        final BlockColors blockColors = evt.getBlockColors();
        final ItemColors itemColors = evt.getItemColors();

        // Use the Block's colour handler for an ItemBlock
        ItemColor itemBlockColourHandler = (stack, tintIndex) -> {
            BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
            return blockColors.getColor(state, null, null, tintIndex);
        };

        if (itemBlockColourHandler != null) {
            itemColors.register(itemBlockColourHandler, ModBlocks.getInstance().peat);
            itemColors.register(itemBlockColourHandler, ModBlocks.getInstance().rhododendron);
        }
    }
}
