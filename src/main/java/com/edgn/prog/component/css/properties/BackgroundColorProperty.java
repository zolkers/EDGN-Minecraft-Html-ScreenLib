package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.RenderableComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.css.CssPropertyName;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class BackgroundColorProperty extends AbstractCssProperty {
    public BackgroundColorProperty() {
        super(CssPropertyName.BACKGROUND_COLOR.getPropertyName());
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof RenderableComponent renderable) {
            int color = ColorUtils.parseColor(value);
            renderable.setBackgroundColor(color);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && (ColorUtils.isNamedColor(value) || ColorUtils.isHexColor(value));
    }
}