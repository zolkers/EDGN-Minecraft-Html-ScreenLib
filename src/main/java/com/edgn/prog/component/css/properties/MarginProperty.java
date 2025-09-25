package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.css.CssPropertyName;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.layout.box.BoxModelComponent;
import com.edgn.prog.layout.spacing.Margin;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.prog.parser.units.CssUnitParser;

public final class MarginProperty extends AbstractCssProperty {
    public MarginProperty() {
        super(CssPropertyName.MARGIN.getPropertyName());
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
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
            if (!CssUnitParser.isValidUnit(part)) return false;
        }
        return parts.length >= 1 && parts.length <= 4;
    }

    private Margin parseMarginValue(String value, MinecraftRenderContext context) {
        String[] parts = value.trim().split("\\s+");
        int containerWidth = context.width();
        int containerHeight = context.height();

        return switch (parts.length) {
            case 1 -> {
                int margin = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                yield new Margin(margin);
            }
            case 2 -> {
                int vertical = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int horizontal = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                yield new Margin(vertical, horizontal);
            }
            case 3 -> {
                int top = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int horizontal = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                int bottom = CssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                yield new Margin(top, horizontal, bottom, horizontal);
            }
            case 4 -> {
                int top = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int right = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                int bottom = CssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context, CssUnitParser.Direction.VERTICAL);
                int left = CssUnitParser.parseSize(parts[3], containerWidth, containerHeight, context, CssUnitParser.Direction.HORIZONTAL);
                yield new Margin(top, right, bottom, left);
            }
            default -> Margin.none();
        };
    }
}