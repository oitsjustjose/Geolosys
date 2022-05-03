package com.oitsjustjose.geolosys.client.render;

import com.oitsjustjose.geolosys.Registry;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;

public class Cutouts {
    public static void init() {
        ItemBlockRenderTypes.setRenderLayer(Registry.PEAT.get(), RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(Registry.RHODODENDRON.get(), RenderType.cutoutMipped());
    }
}
