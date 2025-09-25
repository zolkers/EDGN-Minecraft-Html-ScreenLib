package com.edgn.prog.layout;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

public interface LayoutEngine {
    void layoutComponent(EdgnComponent rootComponent, MinecraftRenderContext context);
}