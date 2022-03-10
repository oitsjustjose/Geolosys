package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.Registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class Cutouts {
    public static void init() {
        ItemBlockRenderTypes.setRenderLayer(Registry.PEAT, RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.RHODODENDRON, RenderType.cutoutMipped());
    }
}
