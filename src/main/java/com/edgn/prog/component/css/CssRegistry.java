package com.edgn.prog.component.css;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public interface CssRegistry {
    void registerProperty(CssProperty property);
    CssProperty getProperty(String name);
    Set<String> getRegisteredProperties();
    boolean isPropertyRegistered(String name);
    void applyCssRule(EdgnComponent component, String property, String value, MinecraftRenderContext context);
}