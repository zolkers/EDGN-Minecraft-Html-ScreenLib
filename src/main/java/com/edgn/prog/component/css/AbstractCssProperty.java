package com.edgn.prog.component.css;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

public abstract class AbstractCssProperty implements CssProperty {

    protected final String name;
    protected AbstractCssProperty(String name) {
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
    public void applyCalculatedValue(EdgnComponent component, Object calculatedValue) {
        if (calculatedValue instanceof String stringValue) {
            apply(component, stringValue, null);
        }
    }
}