package com.oitsjustjose.geolosys.client.render;

import java.util.OptionalDouble;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class GeolosysRenderType extends RenderType {
    private static final LineState BLOCK_SELECTION_LINE_STATE = new LineState(OptionalDouble.of(4.0D));
    public static final RenderType BLOCK_SELECTION = makeType("overlay_lines", DefaultVertexFormats.POSITION_COLOR, 1,
            256,
            State.getBuilder().line(BLOCK_SELECTION_LINE_STATE).layer(RenderState.POLYGON_OFFSET_LAYERING)
                    .transparency(TRANSLUCENT_TRANSPARENCY).texture(NO_TEXTURE).depthTest(DEPTH_ALWAYS)
                    .cull(CULL_DISABLED).lightmap(LIGHTMAP_DISABLED).writeMask(COLOR_WRITE).build(false));

    // region Initialization
    public GeolosysRenderType(String name, VertexFormat vertexFormat, int drawMode, int bufferSize, boolean useDelegate,
            boolean needsSorting, Runnable setupTaskIn, Runnable clearTaskIn) {
        super(name, vertexFormat, drawMode, bufferSize, useDelegate, needsSorting, setupTaskIn, clearTaskIn);
    }

}
