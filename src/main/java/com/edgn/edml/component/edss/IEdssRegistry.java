package com.edgn.edml.component.edss;

import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

import java.util.Set;

public interface IEdssRegistry {
    void registerProperty(EdssProperty property);
    EdssProperty getProperty(String name);
    Set<String> getRegisteredProperties();
    boolean isPropertyRegistered(String name);
    void applyCssRule(EdmlComponent component, String property, String value, MinecraftRenderContext context);
}