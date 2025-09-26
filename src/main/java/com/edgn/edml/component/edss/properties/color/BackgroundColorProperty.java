package com.edgn.edml.component.edss.properties.color;

import com.edgn.edml.component.RenderableComponent;
import com.edgn.edml.component.edss.property.AbstractEdssProperty;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class BackgroundColorProperty extends AbstractEdssProperty {
    public BackgroundColorProperty() {
        super("background-color");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
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