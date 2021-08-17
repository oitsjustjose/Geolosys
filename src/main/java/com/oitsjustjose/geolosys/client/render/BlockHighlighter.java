package com.oitsjustjose.geolosys.client.render;

import java.awt.Color;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/*
 * MIT License
 *
 * SOURCED FROM OCCULTISM:https://github.com/klikli-dev/occultism 
 *      Copyright 2020 klikli-dev
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.oitsjustjose.geolosys.common.config.ClientConfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockHighlighter {

    protected Set<SelectionInfo> selectedBlocks = new HashSet<>();

    public void selectBlock(BlockPos pos, long expireTime) {
        this.selectBlock(pos, expireTime, new Color(1.0f, 1.0f, 1.0f, 0.8f));
    }

    public void selectBlock(BlockPos pos, long expireTime, Color color) {
        SelectionInfo info = new SelectionInfo(pos, expireTime, color);
        if (this.selectedBlocks.contains(info)) {
            this.selectedBlocks.remove(info);
        }
        this.selectedBlocks.add(info);
    }

    public void unselectBlock(BlockPos pos) {
        this.selectedBlocks.removeIf(info -> info.selectedBlock.equals(pos));
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event) {
        this.renderSelectedBlocks(event);
    }

    protected void renderSelectedBlocks(RenderWorldLastEvent event) {
        if (!this.selectedBlocks.isEmpty()) {
            long time = System.currentTimeMillis();

            MatrixStack matrixStack = event.getMatrixStack();
            IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
            IVertexBuilder builder = buffer.getBuffer(GeolosysRenderType.BLOCK_SELECTION);
            matrixStack.push();
            Vector3d projectedView = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
            Matrix4f transform = matrixStack.getLast().getMatrix();

            for (Iterator<SelectionInfo> it = this.selectedBlocks.iterator(); it.hasNext();) {
                SelectionInfo info = it.next();
                info.update();

                if (time > info.selectionExpireTime || info.selectedBlock == null) {
                    // remove expired or invalid selections
                    it.remove();
                    return;
                } else {
                    buildBlockOutline(builder, transform, info.selectedBlock.getX(), info.selectedBlock.getY(),
                            info.selectedBlock.getZ(), info.color.getRed() / 255.0f, info.color.getGreen() / 255.0f,
                            info.color.getBlue() / 255.0f, info.color.getAlpha() / 255.0f);
                }
            }

            matrixStack.pop();
            RenderSystem.enableTexture();
            RenderSystem.disableDepthTest();
            buffer.finish(GeolosysRenderType.BLOCK_SELECTION);
            RenderSystem.enableDepthTest();
        }
    }
    // endregion Methods

    public class SelectionInfo {
        public BlockPos selectedBlock;
        public long selectionExpireTime;
        public Color color;

        public SelectionInfo(BlockPos selectedBlock, long selectionExpireTime, Color color) {
            this.selectedBlock = selectedBlock;
            this.selectionExpireTime = selectionExpireTime;
            this.color = color;
        }

        @Override
        public int hashCode() {
            return this.selectedBlock.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;

            SelectionInfo other = (SelectionInfo) obj;
            if (other == null)
                return false;

            return other.selectedBlock.equals(this.selectedBlock);
        }

        public void update() {
            // This causes the opacity to drop slowly but sureley through the existence of
            // this particular SelectionInfo instance
            long secondsRemaining = selectionExpireTime - System.currentTimeMillis();
            long totalAppearanceTime = ClientConfig.DEPOSIT_HIGHLIGHT_DURATION.get() * 1000;
            if (secondsRemaining <= 0) {
                return;
            }
            float newOpacity = Math.max((float) ((double) secondsRemaining / totalAppearanceTime), 0F);
            this.color = new Color(this.color.getRed() / 255.0f, this.color.getGreen() / 255.0f,
                    this.color.getBlue() / 255.0f, newOpacity);
        }
    }

    void buildBlockOutline(IVertexBuilder buffer, Matrix4f transform, float x, float y, float z, float r, float g,
            float b, float a) {
        buffer.pos(transform, x, y, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y + 1, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y + 1, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y + 1, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y + 1, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y + 1, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y + 1, z).color(r, g, b, a).endVertex();

        buffer.pos(transform, x, y + 1, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y + 1, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y + 1, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y + 1, z).color(r, g, b, a).endVertex();

        buffer.pos(transform, x + 1, y, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y, z).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y + 1, z).color(r, g, b, a).endVertex();

        buffer.pos(transform, x, y, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x + 1, y, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y, z + 1).color(r, g, b, a).endVertex();
        buffer.pos(transform, x, y + 1, z + 1).color(r, g, b, a).endVertex();
    }
}
