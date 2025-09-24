package com.edgn.prog.minecraft;

public interface MinecraftRenderContext {
    int width();
    int height();
    void pushTransform(int x, int y, int width, int height);
    void popTransform();
    void drawRect(int x, int y, int width, int height, int color);
    void drawText(String text, int x, int y, int color);
}