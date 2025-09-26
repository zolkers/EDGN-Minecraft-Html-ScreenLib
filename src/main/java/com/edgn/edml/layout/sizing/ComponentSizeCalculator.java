package com.edgn.edml.layout.sizing;

import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.EdmlEnum;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.component.edml.components.elements.SelfSizingComponent;
import com.edgn.edml.layout.spacing.Margin;
import com.edgn.edml.layout.spacing.Padding;
import com.edgn.edml.minecraft.MinecraftRenderContext;

public class ComponentSizeCalculator implements IComponentSizeCalculator {

    @Override
    public int calculateHeight(EdssAwareComponent component, MinecraftRenderContext context, int availableWidth) {
        if (component.getHeight() > 0) {
            return component.getHeight();
        }

        if (component instanceof SelfSizingComponent selfSizing) {
            return selfSizing.calculateOptimalHeight(context, availableWidth);
        }

        return calculateAutoHeight(component, context, availableWidth);
    }

    @Override
    public int calculateWidth(EdssAwareComponent component, MinecraftRenderContext context, int availableHeight) {
        if (component.getWidth() > 0) {
            return component.getWidth();
        }

        if (component instanceof SelfSizingComponent selfSizing) {
            int optimalWidth = selfSizing.calculateOptimalWidth(context);
            return Math.min(optimalWidth, context.width());
        }

        if (EdmlEnum.BODY.getTagName().equals(component.getTagName())) {
            return context.width();
        }

        return context.width();
    }

    private int calculateAutoHeight(EdssAwareComponent component, MinecraftRenderContext context, int availableWidth) {
        int childrenHeight = calculateChildrenTotalHeight(component, context, availableWidth);
        Padding padding = component.getPadding();

        return childrenHeight + padding.vertical();
    }

    private int calculateChildrenTotalHeight(EdssAwareComponent component, MinecraftRenderContext context, int availableWidth) {
        int totalHeight = 0;
        Padding parentPadding = component.getPadding();
        int childAvailableWidth = availableWidth - parentPadding.horizontal();

        for (EdmlComponent child : component.getChildren()) {
            if (child instanceof EdssAwareComponent cssChild) {
                Margin childMargin = cssChild.getMargin();

                totalHeight += childMargin.top();

                int childHeight = calculateHeight(cssChild, context, childAvailableWidth);
                totalHeight += childHeight;

                totalHeight += childMargin.bottom();
            }
        }

        return totalHeight;
    }
}