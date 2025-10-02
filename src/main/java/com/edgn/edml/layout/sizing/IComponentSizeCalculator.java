package com.edgn.edml.layout.sizing;

import com.edgn.edml.dom.components.EdssAwareComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;

public interface IComponentSizeCalculator {
    int calculateHeight(EdssAwareComponent component, MinecraftRenderContext context, int availableWidth);
    int calculateWidth(EdssAwareComponent component, MinecraftRenderContext context, int availableHeight);
}