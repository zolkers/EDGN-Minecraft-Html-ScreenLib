package com.edgn.edml.dom.styling.properties.spacing;

import com.edgn.edml.dom.styling.properties.AbstractEdssProperty;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.box.Padding;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.dom.styling.units.EdssUnitParser;

public final class PaddingProperty extends AbstractEdssProperty {
    public PaddingProperty() {
        super("padding");
    }

    @Override
    public void apply(EdmlComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof BoxModelComponent boxComponent) {
            Padding padding = parsePaddingValue(value, context);
            boxComponent.setPadding(padding);
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

    private Padding parsePaddingValue(String value, MinecraftRenderContext context) {
        String[] parts = value.trim().split("\\s+");
        int containerWidth = context.width();
        int containerHeight = context.height();

        return switch (parts.length) {
            case 1 -> {
                int padding = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                yield new Padding(padding);
            }
            case 2 -> {
                int vertical = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int horizontal = EdssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                yield new Padding(vertical, horizontal);
            }
            case 3 -> {
                int top = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int horizontal = EdssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                int bottom = EdssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                yield new Padding(top, horizontal, bottom, horizontal);
            }
            case 4 -> {
                int top = EdssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int right = EdssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                int bottom = EdssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, EdssUnitParser.Direction.VERTICAL);
                int left = EdssUnitParser.parseSize(parts[3], containerWidth, containerHeight, context, EdssUnitParser.Direction.HORIZONTAL);
                yield new Padding(top, right, bottom, left);
            }
            default -> Padding.none();
        };
    }
}