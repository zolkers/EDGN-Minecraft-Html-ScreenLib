package com.edgn.edml.component.edss.properties.padding;

import com.edgn.edml.component.edss.property.AbstractEdssProperty;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.spacing.Padding;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.parser.units.EdssUnitParser;

public final class PaddingLeftProperty extends AbstractEdssProperty {
    public PaddingLeftProperty() {
        super("padding-left");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            int left = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.HORIZONTAL);
            Padding currentPadding = boxComponent.getPadding();
            Padding newPadding = new Padding(currentPadding.top(), currentPadding.right(), currentPadding.bottom(), left);
            boxComponent.setPadding(newPadding);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}