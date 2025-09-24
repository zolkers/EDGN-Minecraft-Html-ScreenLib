package com.edgn.prog.component.css;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultCssRegistry implements CssRegistry {
    private final Map<String, CssProperty> properties = new ConcurrentHashMap<>();
    
    @Override
    public void registerProperty(CssProperty property) {
        Objects.requireNonNull(property, "Property cannot be null");
        properties.put(property.getName().toLowerCase(), property);
    }
    
    @Override
    public CssProperty getProperty(String name) {
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
    public void applyCssRule(EdgnComponent component, String property, String value, MinecraftRenderContext context) {
        CssProperty cssProperty = getProperty(property);
        if (cssProperty != null && cssProperty.validate(value)) {
            cssProperty.apply(component, value, context);
        }
    }
}