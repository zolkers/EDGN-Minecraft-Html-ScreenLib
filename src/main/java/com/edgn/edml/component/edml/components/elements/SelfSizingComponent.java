package com.edgn.edml.component.edml.components.elements;

import com.edgn.edml.minecraft.MinecraftRenderContext;

public sealed interface SelfSizingComponent permits ImageComponent, TextComponent {
    int calculateOptimalWidth(MinecraftRenderContext context);
    int calculateOptimalHeight(MinecraftRenderContext context, int availableWidth);
    default boolean canSelfSize() {
        return true;
    }
}