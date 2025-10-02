package com.edgn.edml.core.rendering;

import net.minecraft.util.Identifier;

public interface MinecraftRenderContext {
    int width();
    int height();
    default void pushTransform(int x, int y, int width, int height) {}

    default void popTransform() {}

    default void drawRect(int x, int y, int width, int height, int color) {}

    default void drawText(String text, int x, int y, int color) {}

    default void drawImage(Identifier textureId, int x1, int y1, int x2, int y2, int rotation, boolean flipHorizontal, int tintColor) {}
}