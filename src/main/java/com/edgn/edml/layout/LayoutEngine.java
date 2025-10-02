package com.edgn.edml.layout;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.dom.components.EdssAwareComponent;
import com.edgn.edml.layout.sizing.IComponentSizeCalculator;
import com.edgn.edml.layout.box.Margin;
import com.edgn.edml.layout.box.Padding;
import com.edgn.edml.core.rendering.MinecraftRenderContext;

import java.util.List;
import java.util.Objects;

public record LayoutEngine(IComponentSizeCalculator sizeCalculator) implements ILayoutEngine {

    public LayoutEngine(IComponentSizeCalculator sizeCalculator) {
        this.sizeCalculator = Objects.requireNonNull(sizeCalculator);
    }

    @Override
    public void layoutComponent(EdmlComponent rootComponent, MinecraftRenderContext context) {
        if (rootComponent instanceof EdssAwareComponent cssRoot) {
            calculateIntrinsicSizes(cssRoot, context);
            performLayout(cssRoot, 0, 0, context);
        }
    }

    private void calculateIntrinsicSizes(EdssAwareComponent component, MinecraftRenderContext context) {
        for (EdmlComponent child : component.getChildren()) {
            if (child instanceof EdssAwareComponent cssChild) {
                calculateIntrinsicSizes(cssChild, context);
            }
        }

        if (component.getWidth() == 0) {
            int calculatedWidth = sizeCalculator.calculateWidth(component, context, 0);
            component.setWidth(calculatedWidth);
        }

        if (component.getHeight() == 0) {
            int availableWidth = component.getWidth() - component.getPadding().horizontal();
            int calculatedHeight = sizeCalculator.calculateHeight(component, context, availableWidth);
            component.setHeight(calculatedHeight);
        }
    }

    private void performLayout(EdssAwareComponent component, int x, int y, MinecraftRenderContext context) {
        int componentHeight = component.getHeight();

        if (EdmlEnum.BODY.getTagName().equals(component.getTagName())) {
            componentHeight = Math.max(componentHeight, context.height());
            component.setHeight(componentHeight);
        }

        component.setCalculatedBounds(x, y, component.getWidth(), componentHeight);
        layoutChildren(component.getChildren(), component, context);
    }

    private void layoutChildren(List<EdmlComponent> children, EdssAwareComponent parent, MinecraftRenderContext context) {
        if (children.isEmpty()) return;

        Padding parentPadding = parent.getPadding();
        int contentX = parent.getCalculatedX() + parentPadding.left();
        int contentY = parent.getCalculatedY() + parentPadding.top();
        int contentWidth = parent.getBoxModel().contentWidth();

        int currentY = contentY;

        for (EdmlComponent child : children) {
            if (child instanceof EdssAwareComponent cssChild) {
                Margin childMargin = cssChild.getMargin();

                currentY += childMargin.top();

                int childX = contentX + childMargin.left();

                int availableWidth = contentWidth - childMargin.horizontal();
                int childWidth = Math.min(cssChild.getWidth(), availableWidth);

                if (childWidth != cssChild.getWidth()) {
                    cssChild.setWidth(childWidth);
                }

                performLayout(cssChild, childX, currentY, context);

                int childActualHeight = cssChild.getCalculatedHeight();
                currentY += childActualHeight;

                currentY += childMargin.bottom();

                HTMLMyScreen.LOGGER.trace("Laid out {} at Y={}, height={}, next Y={}",
                        cssChild.getTagName(), currentY - childActualHeight - childMargin.bottom(),
                        childActualHeight, currentY);
            }
        }
    }
}