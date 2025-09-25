package com.edgn.edml.component.edss.properties;

import com.edgn.edml.component.TextCapableComponent;
import com.edgn.edml.component.edss.AbstractEdssProperty;
import com.edgn.edml.component.edss.EdssPropertyName;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class ColorProperty extends AbstractEdssProperty {
    public ColorProperty() {
        super(EdssPropertyName.COLOR.getPropertyName());
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