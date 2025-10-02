package com.edgn.edml.core.rendering.context;

import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Objects;
import java.util.Stack;
public final class RenderContext implements MinecraftRenderContext {
    private final DrawContext drawContext;
    private final Stack<Transform> transformStack = new Stack<>();
    private Transform currentTransform;

    public RenderContext(DrawContext drawContext, int screenWidth, int screenHeight) {
        this.drawContext = Objects.requireNonNull(drawContext);
        this.currentTransform = new Transform(0, 0, screenWidth, screenHeight);
    }

    @Override
    public int width() {
        return currentTransform.width();
    }

    @Override
    public int height() {
        return currentTransform.height();
    }

    @Override
    public void pushTransform(int x, int y, int width, int height) {
        transformStack.push(currentTransform);
        currentTransform = new Transform(
                currentTransform.x() + x,
                currentTransform.y() + y,
                width,
                height
        );
    }

    @Override
    public void popTransform() {
        if (!transformStack.isEmpty()) {
            currentTransform = transformStack.pop();
        }
    }

    @Override
    public void drawRect(int x, int y, int width, int height, int color) {
        int actualX = currentTransform.x() + x;
        int actualY = currentTransform.y() + y;
        drawContext.fill(actualX, actualY, actualX + width, actualY + height, color);
    }

    @Override
    public void drawText(String text, int x, int y, int color) {
        int actualX = currentTransform.x() + x;
        int actualY = currentTransform.y() + y;
        drawContext.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(text),
                actualX,
                actualY,
                color,
                false
        );
    }

    @Override
    public void drawImage(Identifier textureId, int x1, int y1, int x2, int y2, int rotation, boolean flipHorizontal, int tintColor) {
        int actualX1 = currentTransform.x() + x1;
        int actualY1 = currentTransform.y() + y1;
        int actualX2 = currentTransform.x() + x2;
        int actualY2 = currentTransform.y() + y2;

        Color color = new Color(tintColor, true);
        drawImageInternal(textureId, actualX1, actualY1, actualX2, actualY2, rotation, flipHorizontal, color);
    }

    private void drawImageInternal(Identifier id, int x1, int y1, int x2, int y2, int rotation, boolean parity, Color color) {
        int[][] texCoords = { {0,1},{1,1},{1,0},{0,0} };
        rotation = ((rotation % 4) + 4) % 4;

        for (int i = 0; i < rotation; i++) {
            int t0 = texCoords[3][0];
            int t1 = texCoords[3][1];
            texCoords[3][0] = texCoords[2][0]; texCoords[3][1] = texCoords[2][1];
            texCoords[2][0] = texCoords[1][0]; texCoords[2][1] = texCoords[1][1];
            texCoords[1][0] = texCoords[0][0]; texCoords[1][1] = texCoords[0][1];
            texCoords[0][0] = t0; texCoords[0][1] = t1;
        }

        if (parity) {
            int t = texCoords[1][0]; texCoords[1][0] = texCoords[0][0]; texCoords[0][0] = t;
            t = texCoords[3][0];     texCoords[3][0] = texCoords[2][0]; texCoords[2][0] = t;
        }

        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, id);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buf = tess.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        RenderSystem.enableBlend();

        buf.vertex(x1, y2, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).texture(texCoords[0][0], texCoords[0][1]);
        buf.vertex(x2, y2, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).texture(texCoords[1][0], texCoords[1][1]);
        buf.vertex(x2, y1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).texture(texCoords[2][0], texCoords[2][1]);
        buf.vertex(x1, y1, 0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).texture(texCoords[3][0], texCoords[3][1]);

        BuiltBuffer built = buf.endNullable();
        if (built != null) BufferRenderer.drawWithGlobalProgram(built);
        RenderSystem.disableBlend();
    }

    private record Transform(int x, int y, int width, int height) {}
}
