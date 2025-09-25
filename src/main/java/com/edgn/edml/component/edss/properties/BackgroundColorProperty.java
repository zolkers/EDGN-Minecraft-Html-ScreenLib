package com.edgn.edml.component.edss.properties;

import com.edgn.edml.component.RenderableComponent;
import com.edgn.edml.component.edss.AbstractEdssProperty;
import com.edgn.edml.component.edss.EdssPropertyName;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class BackgroundColorProperty extends AbstractEdssProperty {
    public BackgroundColorProperty() {
        super(EdssPropertyName.BACKGROUND_COLOR.getPropertyName());
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