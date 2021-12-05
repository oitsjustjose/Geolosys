package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.common.blocks.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class Cutouts {
    public static void init() {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.getInstance().peat, RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.getInstance().rhododendron, RenderType.cutoutMipped());
    }
}
