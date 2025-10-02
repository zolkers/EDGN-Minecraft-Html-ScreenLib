package com.edgn.edml.dom.styling.properties.sizing;

import com.edgn.edml.core.rendering.SizedComponent;
import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.units.EdssUnitParser;

public final class WidthProperty extends AbstractEdssProperty {
    public WidthProperty() {
        super("width");
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.HORIZONTAL);
    }

    @Override
    public void applyCalculatedValue(EdmlComponent component, Object calculatedValue) {
        if (component instanceof SizedComponent sized && calculatedValue instanceof Integer width) {
            sized.setWidth(width);
        }
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof SizedComponent sized) {
            int width = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.HORIZONTAL);
            sized.setWidth(width);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}