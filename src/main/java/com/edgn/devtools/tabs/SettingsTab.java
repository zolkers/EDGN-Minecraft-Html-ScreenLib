package com.edgn.devtools.tabs;

import com.edgn.devtools.DevToolsOverlay;
import com.edgn.devtools.console.ConsoleSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {

        double checkboxMinX = 10 - CHECKBOX_CLICK_PADDING;
        double checkboxMaxX = 10 + CHECKBOX_SIZE + 20 + CHECKBOX_CLICK_PADDING;
        double checkboxMinY = CHECKBOX_Y - CHECKBOX_CLICK_PADDING;
        double checkboxMaxY = CHECKBOX_Y + CHECKBOX_SIZE + CHECKBOX_CLICK_PADDING;


        boolean inBounds = mouseX >= checkboxMinX && mouseX <= checkboxMaxX &&
                mouseY >= checkboxMinY && mouseY <= checkboxMaxY;

        if (inBounds) {
            ConsoleSettings settings = DevToolsOverlay.getInstance().getSettings();
            boolean newValue = !settings.isDebugBoxesEnabled();
            settings.setDebugBoxesEnabled(newValue);
            return true;
        }

        return false;
    }
}