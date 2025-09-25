package com.edgn.edml.component.html.components;

import com.edgn.edml.minecraft.MinecraftRenderContext;

@SuppressWarnings("unused")
public interface VirtualComponent {
    boolean isVisible();
    void setVisible(boolean visible);
    boolean isInViewport(MinecraftRenderContext context);
    void updateVirtualization(MinecraftRenderContext context);
    VirtualBounds getVirtualBounds();
    void setVirtualBounds(VirtualBounds bounds);
}