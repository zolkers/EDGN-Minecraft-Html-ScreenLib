package com.edgn.edml.ui.virtual;

import com.edgn.edml.core.rendering.MinecraftRenderContext;

@SuppressWarnings("unused")
public interface VirtualComponent {
    boolean isVisible();
    void setVisible(boolean visible);
    boolean isInViewport(MinecraftRenderContext context);
    void updateVirtualization(MinecraftRenderContext context);
    VirtualBounds getVirtualBounds();
    void setVirtualBounds(VirtualBounds bounds);
}