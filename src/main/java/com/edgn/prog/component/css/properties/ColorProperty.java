package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.TextComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.css.CssPropertyName;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class ColorProperty extends AbstractCssProperty {
    public ColorProperty() {
        super(CssPropertyName.COLOR.getPropertyName());
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof TextComponent textComp) {
            int color = parseColor(value);
            textComp.setTextColor(color);
        }
    }

    private int parseColor(String colorStr) {
        return ColorUtils.parseColor(colorStr);
    }
}
