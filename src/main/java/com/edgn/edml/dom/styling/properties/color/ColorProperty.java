package com.edgn.edml.dom.styling.properties.color;

import com.edgn.edml.dom.components.attributes.TextCapableComponent;
import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;

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