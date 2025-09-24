package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class DivComponent extends CssAwareComponent {

    private static final Set<String> DIV_ATTRIBUTES = Set.of(
            "class", "id", "style", "title", "data-*", "role"
    );

    public DivComponent() {
        super("div", DIV_ATTRIBUTES);
    }

    @Override
    protected void renderInternal(MinecraftRenderContext context) {
        int[] renderBounds = calculateRenderBounds(0, 0, context.width(), context.height());
        int renderX = renderBounds[0];
        int renderY = renderBounds[1];
        int renderWidth = renderBounds[2];
        int renderHeight = renderBounds[3];

        if (height <= 0) {
            renderHeight = getDefaultHeight();
        }

        renderBackground(context, renderX, renderY, renderWidth, renderHeight);
        renderText(context, renderX, renderY, renderWidth, renderHeight);

        int[] contentBounds = calculateContentBounds(renderX, renderY, renderWidth, renderHeight);
        int contentX = contentBounds[0];
        int contentY = contentBounds[1];
        int contentWidth = contentBounds[2];
        int contentHeight = contentBounds[3];

        context.pushTransform(contentX, contentY, contentWidth, contentHeight);

        for (EdgnComponent child : children) {
            child.render(context);
        }

        context.popTransform();
    }

    private int getDefaultHeight() {
        if (hasClass("title")) return 40;
        if (hasClass("subtitle")) return 25;
        if (hasClass("content-card")) return 100;
        return 30;
    }

    private void renderText(MinecraftRenderContext context, int x, int y, int width, int height) {
        String textContent = getTextContent();
        if (!textContent.isEmpty()) {
            int textColor = getTextColor();
            int textY = y + (height - 10) / 2; // Center vertically
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
        // Animation logic here
    }

    private void pauseAnimations() {
        // Pause logic here
    }
}