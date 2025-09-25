package com.edgn.prog.layout;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.List;
import java.util.Objects;

public record HtmlLikeLayoutEngine(ComponentSizeCalculator sizeCalculator) implements LayoutEngine {

    public HtmlLikeLayoutEngine(ComponentSizeCalculator sizeCalculator) {
        this.sizeCalculator = Objects.requireNonNull(sizeCalculator);
    }

    @Override
    public void layoutComponent(EdgnComponent rootComponent, MinecraftRenderContext context) {
        if (rootComponent instanceof CssAwareComponent cssRoot) {
            calculateIntrinsicSizes(cssRoot, context);

            cssRoot.setCalculatedBounds(0, 0, context.width(), cssRoot.getCalculatedHeight());
            layoutChildren(rootComponent.getChildren(), cssRoot, context);
        }
    }

    private void calculateIntrinsicSizes(CssAwareComponent component, MinecraftRenderContext context) {
        for (EdgnComponent child : component.getChildren()) {
            if (child instanceof CssAwareComponent cssChild) {
                calculateIntrinsicSizes(cssChild, context);
            }
        }

        int intrinsicHeight = sizeCalculator.calculateHeight(component, context, context.width());
        int intrinsicWidth = sizeCalculator.calculateWidth(component, context, intrinsicHeight);

        component.setCalculatedBounds(0, 0, intrinsicWidth, intrinsicHeight);
    }

    private void layoutChildren(List<EdgnComponent> children, CssAwareComponent parent, MinecraftRenderContext context) {
        int parentX = parent.getCalculatedX();
        int parentY = parent.getCalculatedY();
        int parentWidth = parent.getCalculatedWidth();
        int[] parentPadding = parent.getPadding();

        int contentX = parentX + parentPadding[3];
        int contentY = parentY + parentPadding[0];
        int contentWidth = parentWidth - parentPadding[1] - parentPadding[3];

        int currentY = contentY;

        for (EdgnComponent child : children) {
            if (child instanceof CssAwareComponent cssChild) {
                int[] childMargin = cssChild.getMargin();

                currentY += childMargin[0];

                int childWidth = Math.min(cssChild.getCalculatedWidth(), contentWidth);
                int childHeight = cssChild.getCalculatedHeight();

                cssChild.setCalculatedBounds(contentX, currentY, childWidth, childHeight);

                currentY += childHeight + childMargin[2];

                if (!child.getChildren().isEmpty()) {
                    layoutChildren(child.getChildren(), cssChild, context);
                }
            }
        }
    }
}