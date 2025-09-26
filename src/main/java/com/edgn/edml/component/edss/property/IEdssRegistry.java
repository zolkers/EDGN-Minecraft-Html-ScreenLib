package com.edgn.edml.component.edss.property;

import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

import java.util.Set;

public interface IEdssRegistry {
    void registerProperty(EdssProperty property);
    EdssProperty getProperty(String name);
    Set<String> getRegisteredProperties();
    boolean isPropertyRegistered(String name);
    void applyCssRule(EdmlComponent component, String property, String value, MinecraftRenderContext context);
}