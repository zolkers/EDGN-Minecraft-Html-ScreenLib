package com.edgn.devtools.console;

import com.edgn.HTMLMyScreen;
import com.edgn.devtools.tabs.TabManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class ConsoleRenderer {
    public static final int TAB_HEIGHT = 28;
    public static final int PADDING = 8;


    public void render(DrawContext context, int x, int y, int width, int height,
                       int mouseX, int mouseY, TabManager tabManager, ConsoleSettings settings) {
        int bgColor = applyOpacity(0x1E1E1E, settings.getOpacity());
        context.fill(x, y, x + width, y + height, bgColor);

        renderTabBar(context, x, y, width, mouseX, mouseY, tabManager);

        int contentY = y + TAB_HEIGHT;
        int contentHeight = height - TAB_HEIGHT;

        if (tabManager.getActiveTab() != null) {
            int contentBg = applyOpacity(0x252526, settings.getOpacity());
            context.fill(x, contentY, x + width, contentY + contentHeight, contentBg);

            int adjustedMouseX = mouseX - PADDING;
            int adjustedMouseY = mouseY - TAB_HEIGHT - PADDING;

            tabManager.getActiveTab().render(context,
                    x + PADDING, contentY + PADDING,
                    width - PADDING * 2, contentHeight - PADDING * 2,
                    adjustedMouseX, adjustedMouseY);
        }
    }

    private void renderTabBar(DrawContext context, int x, int y, int width,
                              int mouseX, int mouseY, TabManager tabManager) {
        MinecraftClient client = MinecraftClient.getInstance();

        int currentX = x + PADDING;
        int activeIndex = tabManager.getActiveTabIndex();

        for (int i = 0; i < tabManager.getTabCount(); i++) {
            String tabName = tabManager.getTabName(i);
            int tabWidth = client.textRenderer.getWidth(tabName) + 20;

            boolean isActive = i == activeIndex;
            boolean isHovered = mouseX >= currentX && mouseX < currentX + tabWidth &&
                    mouseY >= y && mouseY < y + TAB_HEIGHT;

            int tabColor;
            if (isActive) {
                tabColor = 0xFF252526;
            } else if (isHovered) {
                tabColor = 0xFF2D2D30;
            } else {
                tabColor = 0xFF1E1E1E;
            }

            context.fill(currentX, y, currentX + tabWidth, y + TAB_HEIGHT, tabColor);

            int textColor = isActive ? 0xFFFFFFFF : 0xFFAAAAAA;
            context.drawText(client.textRenderer, tabName,
                    currentX + 10, y + (TAB_HEIGHT - 9) / 2, textColor, false);

            currentX += tabWidth + 4;
        }
    }

    public boolean handleTabClick(int mouseX, int mouseY, TabManager tabManager, int consoleY) {
        MinecraftClient client = MinecraftClient.getInstance();

        HTMLMyScreen.LOGGER.info("ConsoleRenderer.handleTabClick: mouseX={}, mouseY={}", mouseX, mouseY);

        if (mouseY < TAB_HEIGHT) {
            int currentX = PADDING;

            for (int i = 0; i < tabManager.getTabCount(); i++) {
                String tabName = tabManager.getTabName(i);
                int tabWidth = client.textRenderer.getWidth(tabName) + 20;

                if (mouseX >= currentX && mouseX < currentX + tabWidth) {
                    HTMLMyScreen.LOGGER.info("Tab {} clicked", i);
                    tabManager.setActiveTab(i);
                    return true;
                }

                currentX += tabWidth + 4;
            }
        }

        return false;
    }

    private int applyOpacity(int color, float opacity) {
        int alpha = (int) (opacity * 255);
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}