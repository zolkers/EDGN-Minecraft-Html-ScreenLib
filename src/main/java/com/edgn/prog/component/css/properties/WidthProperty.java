package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.SizedComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.prog.parser.units.CssUnitParser;

public final class WidthProperty extends AbstractCssProperty {
    public WidthProperty() {
        super("width");
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return CssUnitParser.parseSize(value, context.width(), context.height(), context);
    }

    @Override
    public void applyCalculatedValue(EdgnComponent component, Object calculatedValue) {
        if (component instanceof SizedComponent sized && calculatedValue instanceof Integer width) {
            sized.setWidth(width);
        }
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof SizedComponent sized) {
            int width = CssUnitParser.parseSize(value, context.width(), context.height(), context);
            sized.setWidth(width);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && CssUnitParser.isValidUnit(value);
    }
}