package com.edgn.edml.component.edss.properties;

import com.edgn.edml.component.edss.AbstractEdssProperty;
import com.edgn.edml.component.edss.EdssPropertyName;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.spacing.Margin;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.parser.units.EdssUnitParser;

public final class MarginProperty extends AbstractEdssProperty {
    public MarginProperty() {
        super(EdssPropertyName.MARGIN.getPropertyName());
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            Margin margin = parseMarginValue(value, context);
            boxComponent.setMargin(margin);
        }
    }

    @Override
    public boolean validate(String value) {
        if (!super.validate(value)) return false;
        String[] parts = value.trim().split("\\s+");
        for (String part : parts) {
            if (!EdssUnitParser.isValidUnit(part)) return false;
        }
        return parts.length >= 1 && parts.length <= 4;
    }

    private Margin parseMarginValue(String value, MinecraftRenderContext context) {
        String[] parts = value.trim().split("\\s+");
        int containerWidth = context.width();
        int containerHeight = context.height();

        return switch (parts.length) {
            case 1 -> {
                int margin = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                yield new Margin(margin);
            }
            case 2 -> {
                int vertical = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int horizontal = EdssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                yield new Margin(vertical, horizontal);
            }
            case 3 -> {
                int top = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int horizontal = EdssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                int bottom = EdssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                yield new Margin(top, horizontal, bottom, horizontal);
            }
            case 4 -> {
                int top = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int right = EdssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                int bottom = EdssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int left = EdssUnitParser.parseSize(parts[3], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                yield new Margin(top, right, bottom, left);
            }
            default -> Margin.none();
        };
    }
}