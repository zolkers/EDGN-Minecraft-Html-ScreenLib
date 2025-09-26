package com.edgn.edml.component.edml.components.elements;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.component.TextCapableComponent;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.EdmlEnum;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class TextComponent extends EdssAwareComponent implements SelfSizingComponent, TextCapableComponent {
    private String textContent = "";
    private int textColor = 0xFFFFFFFF;
    private static final int CHAR_WIDTH = 8;
    private static final int LINE_HEIGHT = 16;
    private static final int MIN_CHARS_PER_LINE = 10;
    public TextComponent() {
        super(EdmlEnum.TEXT.getTagName(), Set.of());
    }
    public void setTextContent(String text) {
        this.textContent = text != null ? text.trim() : "";
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        if (!textContent.isEmpty()) {
            context.drawText(textContent, x, y, textColor);
        }
    }

    @Override
    @KeepEmpty
    protected void processSpecificAttributes(MinecraftRenderContext context) {}

    @Override
    public int calculateOptimalWidth(MinecraftRenderContext context) {
        if (textContent.isEmpty()) return 0;

        int optimalWidth = textContent.length() * CHAR_WIDTH;
        int maxWidth = context.width() / 2;

        return Math.min(optimalWidth, maxWidth);
    }

    @Override
    public int calculateOptimalHeight(MinecraftRenderContext context, int availableWidth) {
        if (textContent.isEmpty()) return 0;

        int effectiveWidth = availableWidth > 0 ? availableWidth : calculateOptimalWidth(context);
        int charsPerLine = Math.max(MIN_CHARS_PER_LINE, effectiveWidth / CHAR_WIDTH);

        int lines = Math.max(1, (textContent.length() + charsPerLine - 1) / charsPerLine);
        return lines * LINE_HEIGHT;
    }

    @Override
    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public boolean hasTextContent() {
        return !textContent.isEmpty();
    }

    public String getTextContent() {
        return textContent;
    }

    @Override
    public void addChild(EdmlComponent child) {
        throw new UnsupportedOperationException("TextComponent cannot have children");
    }
}