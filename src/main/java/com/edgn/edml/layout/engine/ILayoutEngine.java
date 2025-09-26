package com.edgn.edml.layout.engine;

import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

public interface ILayoutEngine {
    void layoutComponent(EdmlComponent rootComponent, MinecraftRenderContext context);
}