package com.edgn.edml.dom.styling.properties.spacing.padding;

import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.box.Padding;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.units.EdssUnitParser;

public final class PaddingRightProperty extends AbstractEdssProperty {
    public PaddingRightProperty() {
        super("padding-right");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            int right = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.HORIZONTAL);
            Padding currentPadding = boxComponent.getPadding();
            Padding newPadding = new Padding(currentPadding.top(), right, currentPadding.bottom(), currentPadding.left());
            boxComponent.setPadding(newPadding);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}