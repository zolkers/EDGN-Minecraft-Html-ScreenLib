package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.MarginComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.prog.parser.units.CssUnitParser;

public final class MarginProperty extends AbstractCssProperty {
    public MarginProperty() {
        super("margin");
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return parseMarginValue(value, context);
    }

    @Override
    public void applyCalculatedValue(EdgnComponent component, Object calculatedValue) {
        if (component instanceof MarginComponent margined && calculatedValue instanceof int[] margin) {
            margined.setMargin(margin[0], margin[1], margin[2], margin[3]);
        }
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof MarginComponent margined) {
            int[] margins = parseMarginValue(value, context);
            margined.setMargin(margins[0], margins[1], margins[2], margins[3]);
        }
    }

    @Override
    public boolean validate(String value) {
        if (!super.validate(value)) return false;

        String[] parts = value.trim().split("\\s+");
        for (String part : parts) {
            if (!CssUnitParser.isValidUnit(part)) {
                return false;
            }
        }
        return parts.length >= 1 && parts.length <= 4;
    }

    private int[] parseMarginValue(String value, MinecraftRenderContext context) {
        String[] parts = value.trim().split("\\s+");
        int containerWidth = context.width();
        int containerHeight = context.height();

        return switch (parts.length) {
            case 1 -> {
                int margin = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context);
                yield new int[]{margin, margin, margin, margin};
            }
            case 2 -> {
                int vertical = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context);
                int horizontal = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context);
                yield new int[]{vertical, horizontal, vertical, horizontal};
            }
            case 3 -> {
                int top = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context);
                int horizontal = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context);
                int bottom = CssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context);
                yield new int[]{top, horizontal, bottom, horizontal};
            }
            case 4 -> {
                int top = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context);
                int right = CssUnitParser.parseSize(parts[1], containerWidth, containerHeight, context);
                int bottom = CssUnitParser.parseSize(parts[2], containerWidth, containerHeight, context);
                int left = CssUnitParser.parseSize(parts[3], containerWidth, containerHeight, context);
                yield new int[]{top, right, bottom, left};
            }
            default -> new int[]{0, 0, 0, 0};
        };
    }
}