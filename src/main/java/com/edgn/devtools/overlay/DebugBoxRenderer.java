package com.edgn.devtools.overlay;

import com.edgn.HTMLMyScreen;
import com.edgn.devtools.console.ConsoleSettings;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.dom.components.EdssAwareComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DebugBoxRenderer {

    private static final int BORDER_THICKNESS = 2;
    private static final int LABEL_PADDING = 2;

    public static void render(DrawContext context, EdmlComponent root, ConsoleSettings settings, int scrollOffset) {
        if (root instanceof EdssAwareComponent cssComp) {
            renderComponentTree(context, cssComp, settings, 0, scrollOffset);
        }
    }

    private static void renderComponentTree(
            DrawContext context,
            EdssAwareComponent component,
            ConsoleSettings settings,
            int depth,
            int scrollOffset
    ) {
        try {
            String tag = component.getTagName();
            if (!"body".equalsIgnoreCase(tag)) {
                renderComponentBox(context, component, settings, depth, scrollOffset);
            }

            for (EdmlComponent child : component.getChildren()) {
                if (child instanceof EdssAwareComponent cssChild) {
                    renderComponentTree(context, cssChild, settings, depth + 1, scrollOffset);
                }
            }
        } catch (Exception e) {
            HTMLMyScreen.LOGGER.error("Error rendering debug box for {}", component.getTagName(), e);
        }
    }

    private static void renderComponentBox(DrawContext context, EdssAwareComponent component,
                                           ConsoleSettings settings, int depth, int scrollOffset) {
        int x = component.getCalculatedX();
        int y = component.getCalculatedY() - scrollOffset;
        int width = component.getCalculatedWidth();
        int height = component.getCalculatedHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        int color = settings.getColorForComponent(component);
        int alpha = Math.max(0x60, 0xFF - (depth * 0x15));
        int colorWithAlpha = (color & 0x00FFFFFF) | (alpha << 24);

        drawBorder(context, x, y, width, height, colorWithAlpha, BORDER_THICKNESS);

        String label = component.getTagName() + " (" + width + "x" + height + ")";
        renderLabel(context, x, y, label, colorWithAlpha);
    }

    private static void drawBorder(DrawContext context, int x, int y, int width, int height,
                                   int color, int thickness) {
        context.fill(x, y, x + width, y + thickness, color);
        context.fill(x, y + height - thickness, x + width, y + height, color);
        context.fill(x, y, x + thickness, y + height, color);
        context.fill(x + width - thickness, y, x + width, y + height, color);
    }

    private static void renderLabel(DrawContext context, int x, int y, String label, int borderColor) {
        MinecraftClient client = MinecraftClient.getInstance();
        int textWidth = client.textRenderer.getWidth(label);
        int bgWidth = textWidth + LABEL_PADDING * 2;
        int bgHeight = 10 + LABEL_PADDING * 2;

        int labelX = x + 2;
        int labelY = y + 2;

        int bgColor = (borderColor & 0x00FFFFFF) | 0xCC000000;
        context.fill(labelX, labelY, labelX + bgWidth, labelY + bgHeight, bgColor);

        int textColor = 0xFFFFFFFF;
        context.drawText(client.textRenderer, label,
                labelX + LABEL_PADDING, labelY + LABEL_PADDING, textColor, true);
    }
}