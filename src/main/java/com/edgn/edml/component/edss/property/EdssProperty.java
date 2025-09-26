package com.edgn.edml.component.edss.property;

import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

public interface EdssProperty {
    String getName();
    boolean validate(String value);
    Object calculateValue(String value, MinecraftRenderContext context);
    void applyCalculatedValue(EdmlComponent component, Object calculatedValue);
    default void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        Object calculated = calculateValue(value, context);
        if (calculated != null) {
            applyCalculatedValue(component, calculated);
        }
    }
}
