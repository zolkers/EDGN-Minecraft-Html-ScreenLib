package com.edgn.edml.component.edss.property;

import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

public abstract class AbstractEdssProperty implements EdssProperty {

    protected final String name;
    protected AbstractEdssProperty(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean validate(String value) {
        return value != null && !value.trim().isEmpty();
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return value;
    }
    @Override
    public void applyCalculatedValue(EdmlComponent component, Object calculatedValue) {
        if (calculatedValue instanceof String stringValue) {
            apply(component, stringValue, null);
        }
    }
}