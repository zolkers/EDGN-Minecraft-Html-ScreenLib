package com.edgn.prog.layout;

import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.HtmlTag;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.layout.config.ComponentDimensions;
import com.edgn.prog.layout.spacing.Margin;
import com.edgn.prog.minecraft.MinecraftRenderContext;

public class HtmlLikeComponentSizeCalculator implements ComponentSizeCalculator {
    
    @Override
    public int calculateHeight(CssAwareComponent component, MinecraftRenderContext context, int availableWidth) {
        if (component.getHeight() > 0) {
            return component.getHeight();
        }
        
        int contentHeight = calculateContentBasedHeight(component);
        int childrenHeight = calculateChildrenTotalHeight(component);
        
        return Math.max(contentHeight, childrenHeight);
    }
    
    @Override
    public int calculateWidth(CssAwareComponent component, MinecraftRenderContext context, int availableHeight) {
        if (component.getWidth() > 0) {
            return component.getWidth();
        }
        
        return context.width();
    }
    
    private int calculateContentBasedHeight(CssAwareComponent component) {
        String tagName = component.getTagName();
        
        if (HtmlTag.HEADER.getTagName().equals(tagName)) {
            return hasContent(component) ? ComponentDimensions.DEFAULT_HEADER_HEIGHT : 0;
        }
        
        if (HtmlTag.FOOTER.getTagName().equals(tagName)) {
            return hasContent(component) ? ComponentDimensions.DEFAULT_FOOTER_HEIGHT : 0;
        }
        
        if (HtmlTag.DIV.getTagName().equals(tagName)) {
            return calculateTextHeight(component);
        }
        
        return 0;
    }

    private int calculateChildrenTotalHeight(CssAwareComponent component) {
        int totalHeight = 0;

        for (var child : component.getChildren()) {
            if (child instanceof CssAwareComponent cssChild) {
                totalHeight += calculateHeight(cssChild, null, 0);

                Margin margins = cssChild.getMargin();
                totalHeight += margins.vertical();
            }
        }

        return totalHeight;
    }
    
    private int calculateTextHeight(CssAwareComponent component) {
        String text = component.getAttribute(TagAttribute.DATA_TEXT.getProperty(), "");
        if (text.isEmpty()) {
            return 0;
        }
        
        if (component.hasClass(TagAttribute.TITLE.getProperty())) {
            return ComponentDimensions.TITLE_HEIGHT;
        }
        if (component.hasClass(TagAttribute.SUBTITLE.getProperty())) {
            return ComponentDimensions.SUBTITLE_HEIGHT;
        }
        
        int lines = Math.max(1, text.length() / ComponentDimensions.CHARS_PER_LINE);
        return lines * ComponentDimensions.PIXELS_PER_LINE + ComponentDimensions.TEXT_PADDING;
    }
    
    private boolean hasContent(CssAwareComponent component) {
        return !component.getAttribute(TagAttribute.DATA_TEXT.getProperty(), "").isEmpty() ||
               !component.getChildren().isEmpty();
    }
}