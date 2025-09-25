package com.edgn.prog.layout;

import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

public interface ComponentSizeCalculator {
    int calculateHeight(CssAwareComponent component, MinecraftRenderContext context, int availableWidth);
    int calculateWidth(CssAwareComponent component, MinecraftRenderContext context, int availableHeight);
}