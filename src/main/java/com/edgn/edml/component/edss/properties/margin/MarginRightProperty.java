package com.edgn.edml.component.edss.properties.margin;

import com.edgn.edml.component.edss.property.AbstractEdssProperty;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.spacing.Margin;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.parser.units.EdssUnitParser;

public final class MarginRightProperty extends AbstractEdssProperty {
    public MarginRightProperty() {
        super("margin-right");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            int right = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.HORIZONTAL);
            Margin currentMargin = boxComponent.getMargin();
            Margin newMargin = new Margin(currentMargin.top(), right, currentMargin.bottom(), currentMargin.left());
            boxComponent.setMargin(newMargin);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}