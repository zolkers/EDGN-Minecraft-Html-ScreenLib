package com.edgn.prog.component.css;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

public interface CssProperty {
    String getName();
    boolean validate(String value);
    Object calculateValue(String value, MinecraftRenderContext context);
    void applyCalculatedValue(EdgnComponent component, Object calculatedValue);
    default void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        Object calculated = calculateValue(value, context);
        if (calculated != null) {
            applyCalculatedValue(component, calculated);
        }
    }
}
