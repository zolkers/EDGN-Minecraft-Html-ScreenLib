package com.edgn.prog.component.css.properties;
import com.edgn.prog.component.RenderableComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

public final class BackgroundColorProperty extends AbstractCssProperty {
    public BackgroundColorProperty() {
        super("background-color");
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return ColorUtils.parseColor(value);
    }

    @Override
    public void applyCalculatedValue(EdgnComponent component, Object calculatedValue) {
        if (component instanceof RenderableComponent renderable && calculatedValue instanceof Integer color) {
            renderable.setBackgroundColor(color);
        }
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
