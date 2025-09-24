package com.edgn.prog.component.css.properties;

import com.edgn.prog.component.PaddedComponent;
import com.edgn.prog.component.css.AbstractCssProperty;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.prog.parser.units.CssUnitParser;

public final class PaddingProperty extends AbstractCssProperty {
    public PaddingProperty() {
        super("padding");
    }

    @Override
    public Object calculateValue(String value, MinecraftRenderContext context) {
        return parsePaddingValue(value, context);
    }

    @Override
    public void applyCalculatedValue(EdgnComponent component, Object calculatedValue) {
        if (component instanceof PaddedComponent padded && calculatedValue instanceof int[] padding) {
            padded.setPadding(padding[0], padding[1], padding[2], padding[3]);
        }
    }

    @Override
    public void apply(EdgnComponent component, String value, MinecraftRenderContext context) {
        if (component instanceof PaddedComponent padded) {
            int[] paddings = parsePaddingValue(value, context);
            padded.setPadding(paddings[0], paddings[1], paddings[2], paddings[3]);
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

    private int[] parsePaddingValue(String value, MinecraftRenderContext context) {
        String[] parts = value.trim().split("\\s+");
        int containerWidth = context.width();
        int containerHeight = context.height();

        return switch (parts.length) {
            case 1 -> {
                int padding = CssUnitParser.parseSize(parts[0], containerWidth, containerHeight, context);
                yield new int[]{padding, padding, padding, padding};
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