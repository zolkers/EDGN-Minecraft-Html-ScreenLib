package com.edgn.edml.component.edml.components.virtual;

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