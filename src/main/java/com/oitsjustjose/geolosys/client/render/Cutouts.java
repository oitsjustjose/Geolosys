package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.common.blocks.ModBlocks;
import com.oitsjustjose.geolosys.common.blocks.Types;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class Cutouts {
    public static void init() {
        RenderTypeLookup.setRenderLayer(Types.Coals.PEAT.getBlock(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(ModBlocks.getInstance().rhododendron, RenderType.getCutoutMipped());
    }
}
