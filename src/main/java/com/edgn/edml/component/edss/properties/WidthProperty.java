package com.edgn.edml.component.edss.properties;

import com.edgn.edml.component.SizedComponent;
import com.edgn.edml.component.edss.AbstractEdssProperty;
import com.edgn.edml.component.edss.EdssPropertyName;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.parser.units.EdssUnitParser;

public final class WidthProperty extends AbstractEdssProperty {
    public WidthProperty() {
        super(EdssPropertyName.WIDTH.getPropertyName());
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