package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.common.blocks.ModBlocks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;

public class Cutouts {
    public static void init() {
        RenderTypeLookup.setRenderLayer(ModBlocks.getInstance().peat, RenderType.getCutoutMipped());
    }
}
