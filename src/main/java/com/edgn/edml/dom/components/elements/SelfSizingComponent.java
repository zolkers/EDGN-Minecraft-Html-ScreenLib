package com.edgn.edml.dom.components.elements;

import com.edgn.edml.core.rendering.MinecraftRenderContext;

public sealed interface SelfSizingComponent permits ImageComponent, TextComponent {
    int calculateOptimalWidth(MinecraftRenderContext context);
    int calculateOptimalHeight(MinecraftRenderContext context, int availableWidth);
    default boolean canSelfSize() {
        return true;
    }
}