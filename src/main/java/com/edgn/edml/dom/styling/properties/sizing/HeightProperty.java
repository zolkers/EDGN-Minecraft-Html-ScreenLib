package com.edgn.edml.dom.styling.properties.sizing;

import com.edgn.edml.core.rendering.SizedComponent;
import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.units.EdssUnitParser;

public final class HeightProperty extends AbstractEdssProperty {
    public HeightProperty() {
        super("height");
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.VERTICAL);
    }

    @Override
    public void applyCalculatedValue(EdmlComponent component, Object calculatedValue) {
        if (component instanceof SizedComponent sized && calculatedValue instanceof Integer height) {
            sized.setHeight(height);
        }
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof SizedComponent sized) {
            int height = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.VERTICAL);
            sized.setHeight(height);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}