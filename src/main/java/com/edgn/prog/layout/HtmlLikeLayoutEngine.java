package com.edgn.prog.layout;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.layout.spacing.Margin;
import com.edgn.prog.layout.spacing.Padding;
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
        Padding parentPadding = parent.getPadding();

        int contentX = parentX + parentPadding.left();
        int contentY = parentY + parentPadding.top();
        int contentWidth = parent.getBoxModel().contentWidth();

        int currentY = contentY;

        for (EdgnComponent child : children) {
            if (child instanceof CssAwareComponent cssChild) {
                Margin childMargin = cssChild.getMargin();

                currentY += childMargin.top();

                int childWidth = Math.min(cssChild.getBoxModel().width(), contentWidth);
                int childHeight = cssChild.getBoxModel().height();

                cssChild.setCalculatedBounds(contentX, currentY, childWidth, childHeight);
                currentY += childHeight + childMargin.bottom();

                if (!child.getChildren().isEmpty()) {
                    layoutChildren(child.getChildren(), cssChild, context);
                }
            }
        }
    }
}