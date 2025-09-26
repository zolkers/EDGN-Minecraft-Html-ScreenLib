package com.edgn.edml.component.edss.properties.padding;

import com.edgn.edml.component.edss.property.AbstractEdssProperty;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.spacing.Padding;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.parser.units.EdssUnitParser;

public final class PaddingTopProperty extends AbstractEdssProperty {
    public PaddingTopProperty() {
        super("padding-top");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            int top = EdssUnitParser.parseSize(value, context.width(), context.height(), context, EdssUnitParser.Direction.VERTICAL);
            Padding currentPadding = boxComponent.getPadding();
            Padding newPadding = new Padding(top, currentPadding.right(), currentPadding.bottom(), currentPadding.left());
            boxComponent.setPadding(newPadding);
        }
    }

    @Override
    public boolean validate(String value) {
        return super.validate(value) && EdssUnitParser.isValidUnit(value);
    }
}