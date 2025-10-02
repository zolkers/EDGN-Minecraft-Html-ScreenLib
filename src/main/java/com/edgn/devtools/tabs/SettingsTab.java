// FILE: src/main/java/com/edgn/devtools/tabs/SettingsTab.java
package com.edgn.devtools.tabs;

import com.edgn.HTMLMyScreen;
import com.edgn.devtools.DevToolsOverlay;
import com.edgn.devtools.console.ConsoleSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Map;

public final class SettingsTab implements IConsoleTab {

    private static final int CHECKBOX_Y = 10;
    private static final int CHECKBOX_SIZE = 12;
    private static final int CHECKBOX_CLICK_PADDING = 10;

    @Override
    public String getName() {
        return "Settings";
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        ConsoleSettings settings = DevToolsOverlay.getInstance().getSettings();

        boolean debugEnabled = settings.isDebugBoxesEnabled();

        int checkboxX = x + 10;
        int checkboxY = y + CHECKBOX_Y;

        context.fill(checkboxX - 1, checkboxY - 1, checkboxX + CHECKBOX_SIZE + 1, checkboxY + CHECKBOX_SIZE + 1, 0xFFFFFFFF);
        context.fill(checkboxX, checkboxY, checkboxX + CHECKBOX_SIZE, checkboxY + CHECKBOX_SIZE, 0xFF000000);

        if (debugEnabled) {
            context.fill(checkboxX + 2, checkboxY + 2, checkboxX + CHECKBOX_SIZE - 2, checkboxY + CHECKBOX_SIZE - 2, 0xFF00FF00);
        }

        String checkboxText = "Show Debug Boxes";
        context.drawText(client.textRenderer, checkboxText, checkboxX + 20, checkboxY + 2, 0xFFFFFF, false);

        if (mouseX >= 10 - CHECKBOX_CLICK_PADDING && mouseX <= 10 + CHECKBOX_SIZE + 20 + CHECKBOX_CLICK_PADDING &&
                mouseY >= CHECKBOX_Y - CHECKBOX_CLICK_PADDING && mouseY <= CHECKBOX_Y + CHECKBOX_SIZE + CHECKBOX_CLICK_PADDING) {
            context.fill(x + 10 - CHECKBOX_CLICK_PADDING, y + CHECKBOX_Y - CHECKBOX_CLICK_PADDING,
                    x + 10 + CHECKBOX_SIZE + 20 + CHECKBOX_CLICK_PADDING, y + CHECKBOX_Y + CHECKBOX_SIZE + CHECKBOX_CLICK_PADDING,
                    0x33FFFFFF);
        }

        context.drawText(client.textRenderer, "Component Colors:", x + 10, y + 40, 0xCCCCCC, false);

        Map<String, Integer> colors = settings.getAllRegisteredColors();
        int currentY = y + 60;
        int index = 0;

        for (Map.Entry<String, Integer> entry : colors.entrySet()) {
            if (index >= 10) break;

            String tagName = entry.getKey();
            int color = entry.getValue();

            context.fill(x + 20, currentY, x + 30, currentY + 10, color);
            context.drawText(client.textRenderer, tagName, x + 35, currentY + 1, 0xFFFFFF, false);

            currentY += 15;
            index++;
        }

        context.drawText(client.textRenderer,
                String.format("Mouse: %.0f, %.0f", (float) mouseX,(float) mouseY),
                x + 10, y + height - 20, 0xFF00FFFF, false);
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        HTMLMyScreen.LOGGER.info("=== SettingsTab.handleClick ===");
        HTMLMyScreen.LOGGER.info("mouseX={}, mouseY={}, button={}", mouseX, mouseY, button);

        double checkboxMinX = 10 - CHECKBOX_CLICK_PADDING;
        double checkboxMaxX = 10 + CHECKBOX_SIZE + 20 + CHECKBOX_CLICK_PADDING;
        double checkboxMinY = CHECKBOX_Y - CHECKBOX_CLICK_PADDING;
        double checkboxMaxY = CHECKBOX_Y + CHECKBOX_SIZE + CHECKBOX_CLICK_PADDING;

        HTMLMyScreen.LOGGER.info("Checkbox bounds: X[{} to {}], Y[{} to {}]",
                checkboxMinX, checkboxMaxX, checkboxMinY, checkboxMaxY);

        boolean inBounds = mouseX >= checkboxMinX && mouseX <= checkboxMaxX &&
                mouseY >= checkboxMinY && mouseY <= checkboxMaxY;

        HTMLMyScreen.LOGGER.info("In bounds: {}", inBounds);

        if (inBounds) {
            ConsoleSettings settings = DevToolsOverlay.getInstance().getSettings();
            boolean newValue = !settings.isDebugBoxesEnabled();
            settings.setDebugBoxesEnabled(newValue);
            HTMLMyScreen.LOGGER.info("✓ Debug boxes toggled to: {}", newValue);
            return true;
        }

        return false;
    }
}