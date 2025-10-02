package com.edgn.edml.dom.styling.properties.color;

import com.edgn.edml.core.rendering.RenderableComponent;
import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;

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