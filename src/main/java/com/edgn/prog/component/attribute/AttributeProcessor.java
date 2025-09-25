package com.edgn.prog.component.attribute;

import com.edgn.prog.component.css.CssRegistry;
import com.edgn.prog.component.css.CssRule;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.List;

public interface AttributeProcessor {
    void processAttributes(List<CssRule> cssRules, CssRegistry cssRegistry, MinecraftRenderContext context);
}