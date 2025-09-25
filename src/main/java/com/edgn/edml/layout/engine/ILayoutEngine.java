package com.edgn.edml.layout.engine;

import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

public interface ILayoutEngine {
    void layoutComponent(EdmlComponent rootComponent, MinecraftRenderContext context);
}