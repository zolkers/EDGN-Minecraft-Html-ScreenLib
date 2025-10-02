package com.edgn.edml.dom.styling.registry;

import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.properties.EdssProperty;

import java.util.Set;

public interface IEdssRegistry {
    void registerProperty(EdssProperty property);
    EdssProperty getProperty(String name);
    Set<String> getRegisteredProperties();
    boolean isPropertyRegistered(String name);
    void applyCssRule(EdmlComponent component, String property, String value, MinecraftRenderContext context);
}