package com.edgn.edml.dom.components.attributes;

import com.edgn.edml.dom.styling.registry.IEdssRegistry;
import com.edgn.edml.dom.styling.EdssRule;
import com.edgn.edml.core.rendering.MinecraftRenderContext;

import java.util.List;

public interface AttributeProcessor {
    void processAttributes(List<EdssRule> cssRules, IEdssRegistry cssRegistry, MinecraftRenderContext context);
}