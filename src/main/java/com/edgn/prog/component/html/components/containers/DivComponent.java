package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.HtmlTag;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class DivComponent extends CssAwareComponent {
    private static final Set<String> DIV_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.DATA.getProperty(),
            TagAttribute.ROLE.getProperty(), TagAttribute.TITLE.getProperty(),
            TagAttribute.SUBTITLE.getProperty()
    );

    public DivComponent() {
        super(HtmlTag.DIV.getTagName(), DIV_ATTRIBUTES);
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        renderText(context, x, y, width, height);
    }

    private void renderText(MinecraftRenderContext context, int x, int y, int width, int height) {
        String textContent = getTextContent();
        if (!textContent.isEmpty()) {
            int textColor = getTextColor();
            int textY = y + (height - 10) / 2;
            context.drawText(textContent, x + 5, textY, textColor);
        }
    }

    private String getTextContent() {
        return getAttribute("data-text", "");
    }

    private int getTextColor() {
        if (hasClass("title")) return 0xFFFFFFFF;
        if (hasClass("subtitle")) return 0xFFCCCCCC;
        if (hasClass("section-title")) return 0xFF4CAF50;
        if (hasClass("status-ok")) return 0xFF4CAF50;
        return 0xFFE0E0E0;
    }

    @Override
    protected void onViewportStatusChanged(boolean inViewport) {
        if (hasClass("animated")) {
            if (inViewport) {
                startAnimations();
            } else {
                pauseAnimations();
            }
        }
    }

    private void startAnimations() {
        // Animation logic
    }

    private void pauseAnimations() {
        // Pause logic
    }
}