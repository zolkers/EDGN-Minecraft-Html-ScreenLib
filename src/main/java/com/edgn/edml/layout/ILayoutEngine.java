package com.edgn.edml.layout;

import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;

public interface ILayoutEngine {
    void layoutComponent(EdmlComponent rootComponent, MinecraftRenderContext context);
}