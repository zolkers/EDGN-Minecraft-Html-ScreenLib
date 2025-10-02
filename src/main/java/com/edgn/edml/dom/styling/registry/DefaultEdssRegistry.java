package com.edgn.edml.dom.styling.registry;

import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.properties.EdssProperty;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultEdssRegistry implements IEdssRegistry {
    private final Map<String, EdssProperty> properties = new ConcurrentHashMap<>();
    
    @Override
    public void registerProperty(EdssProperty property) {
        Objects.requireNonNull(property, "Property cannot be null");
        properties.put(property.getName().toLowerCase(), property);
    }
    
    @Override
    public EdssProperty getProperty(String name) {
        return properties.get(name.toLowerCase());
    }
    
    @Override
    public Set<String> getRegisteredProperties() {
        return Set.copyOf(properties.keySet());
    }
    
    @Override
    public boolean isPropertyRegistered(String name) {
        return properties.containsKey(name.toLowerCase());
    }
    
    @Override
    public void applyCssRule(EdmlComponent component, String property, String value, MinecraftRenderContext context) {
        EdssProperty cssProperty = getProperty(property);
        if (cssProperty != null && cssProperty.validate(value)) {
            cssProperty.apply(component, value, context);
        }
    }
}