package com.edgn.prog.component.html.components;

import com.edgn.prog.minecraft.MinecraftRenderContext;

@SuppressWarnings("unused")
public interface VirtualComponent {
    boolean isVisible();
    void setVisible(boolean visible);
    boolean isInViewport(MinecraftRenderContext context);
    void updateVirtualization(MinecraftRenderContext context);
    VirtualBounds getVirtualBounds();
    void setVirtualBounds(VirtualBounds bounds);
}