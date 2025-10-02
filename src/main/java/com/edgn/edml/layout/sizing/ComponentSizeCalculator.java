package com.edgn.edml.layout.sizing;

import com.edgn.edml.dom.components.attributes.TextCapableComponent;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.dom.components.EdssAwareComponent;
import com.edgn.edml.dom.components.elements.SelfSizingComponent;
import com.edgn.edml.layout.box.Margin;
import com.edgn.edml.layout.box.Padding;
import com.edgn.edml.core.rendering.MinecraftRenderContext;

public class ComponentSizeCalculator implements IComponentSizeCalculator {

    private static final int DEFAULT_LINE_HEIGHT = 20;
    private static final int MIN_BLOCK_HEIGHT = 10;

    @Override
    public int calculateHeight(EdssAwareComponent component, MinecraftRenderContext context, int availableWidth) {
        if (component.getHeight() > 0) {
            return component.getHeight();
        }

        if (component instanceof SelfSizingComponent selfSizing) {
            return selfSizing.calculateOptimalHeight(context, availableWidth);
        }

        if (hasTextContent(component)) {
            Padding padding = component.getPadding();
            return DEFAULT_LINE_HEIGHT + padding.vertical();
        }

        int childrenHeight = calculateChildrenTotalHeight(component, context, availableWidth);

        if (childrenHeight > 0) {
            return childrenHeight + component.getPadding().vertical();
        }

        if (component.getChildren().isEmpty()) {
            return MIN_BLOCK_HEIGHT;
        }

        return component.getPadding().vertical();
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

        return context.width();
    }

    private int calculateChildrenTotalHeight(EdssAwareComponent component, MinecraftRenderContext context, int availableWidth) {
        int totalHeight = 0;
        Padding parentPadding = component.getPadding();
        int childAvailableWidth = availableWidth - parentPadding.horizontal();

        for (EdmlComponent child : component.getChildren()) {
            if (child instanceof EdssAwareComponent cssChild) {
                Margin childMargin = cssChild.getMargin();

                totalHeight += childMargin.top();

                int childHeight = cssChild.getHeight();
                if (childHeight == 0) {
                    childHeight = calculateHeight(cssChild, context, childAvailableWidth);
                }
                totalHeight += childHeight;

                totalHeight += childMargin.bottom();
            }
        }

        return totalHeight;
    }

    private boolean hasTextContent(EdssAwareComponent component) {
        // Check data-text attribute
        String dataText = component.getAttribute(TagAttribute.DATA_TEXT.getProperty(), "");
        if (!dataText.isEmpty()) {
            return true;
        }

        // Check if implements TextCapableComponent with content
        if (component instanceof TextCapableComponent textCapable) {
            return textCapable.hasTextContent();
        }

        return false;
    }
}