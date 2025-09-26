package com.edgn.edml.component.attribute;

import com.edgn.edml.component.edss.property.IEdssRegistry;
import com.edgn.edml.component.edss.property.EdssRule;
import com.edgn.edml.minecraft.MinecraftRenderContext;

import java.util.List;

public interface AttributeProcessor {
    void processAttributes(List<EdssRule> cssRules, IEdssRegistry cssRegistry, MinecraftRenderContext context);
}