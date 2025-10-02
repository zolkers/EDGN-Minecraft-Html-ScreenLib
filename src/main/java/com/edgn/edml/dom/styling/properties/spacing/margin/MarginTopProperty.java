package com.edgn.edml.dom.styling.properties.spacing.margin;

import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.box.Margin;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.units.EdssUnitParser;

public final class MarginTopProperty extends AbstractEdssProperty {
    public MarginTopProperty() {
        super("margin-top");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            int top = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.VERTICAL);
            Margin currentMargin = boxComponent.getMargin();
            Margin newMargin = new Margin(top, currentMargin.right(), currentMargin.bottom(), currentMargin.left());
            boxComponent.setMargin(newMargin);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}
