package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.css.CssPropertyName;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.layout.box.BoxModelComponent;
import com.edgn.prog.layout.spacing.Padding;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.prog.parser.units.CssUnitParser;

public final class PaddingProperty extends AbstractCssProperty {
    public PaddingProperty() {
        super(CssPropertyName.PADDING.getPropertyName());
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
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
            if (!CssUnitParser.isValidUnit(part)) return false;
        }
        return parts.length >= 1 && parts.length <= 4;
    }

    private Padding parsePaddingValue(String value, MinecraftRenderContext context) {
        String[] parts = value.trim().split("\\s+");
        int containerWidth = context.width();
        int containerHeight = context.height();

        return switch (parts.length) {
            case 1 -> {
                int padding = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                yield new Padding(padding);
            }
            case 2 -> {
                int vertical = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int horizontal = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                yield new Padding(vertical, horizontal);
            }
            case 3 -> {
                int top = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int horizontal = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                int bottom = CssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                yield new Padding(top, horizontal, bottom, horizontal);
            }
            case 4 -> {
                int top = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int right = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                int bottom = CssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int left = CssUnitParser.parseSize(parts[3], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                yield new Padding(top, right, bottom, left);
            }
            default -> Padding.none();
        };
    }
}