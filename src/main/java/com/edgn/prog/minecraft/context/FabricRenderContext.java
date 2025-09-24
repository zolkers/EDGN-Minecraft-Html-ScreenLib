package com.edgn.prog.minecraft.context;

import com.edgn.prog.minecraft.MinecraftRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.Stack;

//TODO: doing a complete bridge from Minecraft#drawContext would be insanely good ( and even more )
public final class FabricRenderContext implements MinecraftRenderContext {
    private final DrawContext drawContext;
    private final Stack<Transform> transformStack = new Stack<>();
    private Transform currentTransform;

    public FabricRenderContext(DrawContext drawContext, int screenWidth, int screenHeight) {
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

    private record Transform(int x, int y, int width, int height) {}
}
