package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.SizedComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.css.CssPropertyName;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.prog.parser.units.CssUnitParser;

public final class HeightProperty extends AbstractCssProperty {
    public HeightProperty() {
        super(CssPropertyName.HEIGHT.getPropertyName());
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return CssUnitParser.parseSize(value, context.width(), context.height(), context, CssUnitParser.Direction.VERTICAL);
    }

    @Override
    public void applyCalculatedValue(EdgnComponent component, Object calculatedValue) {
        if (component instanceof SizedComponent sized && calculatedValue instanceof Integer height) {
            sized.setHeight(height);
        }
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof SizedComponent sized) {
            int height = CssUnitParser.parseSize(value, context.width(), context.height(), context, CssUnitParser.Direction.VERTICAL);
            sized.setHeight(height);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && CssUnitParser.isValidUnit(value);
    }
}