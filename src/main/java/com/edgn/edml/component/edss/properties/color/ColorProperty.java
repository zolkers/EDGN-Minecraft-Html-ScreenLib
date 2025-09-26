package com.edgn.edml.component.edss.properties.color;

import com.edgn.edml.component.TextCapableComponent;
import com.edgn.edml.component.edss.property.AbstractEdssProperty;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class ColorProperty extends AbstractEdssProperty {
    public ColorProperty() {
        super("color");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof TextCapableComponent textCapable) {
            int color = parseColor(value);
            textCapable.setTextColor(color);
        }
    }

    private int parseColor(String colorStr) {
        return ColorUtils.parseColor(colorStr);
    }
}