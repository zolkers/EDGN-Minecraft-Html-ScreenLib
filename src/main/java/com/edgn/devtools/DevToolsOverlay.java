package com.edgn.devtools;

import com.edgn.HTMLMyScreen;
import com.edgn.devtools.console.ConsoleRenderer;
import com.edgn.devtools.console.ConsoleSettings;
import com.edgn.devtools.overlay.DebugBoxRenderer;
import com.edgn.devtools.tabs.TabManager;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.screen.EdmlScreen;
import com.edgn.edml.ui.scroll.ScrollableComponent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DevToolsOverlay {
    private static final DevToolsOverlay INSTANCE = new DevToolsOverlay();

    private boolean active = false;
    private EdmlScreen attachedScreen = null;
    private TabManager tabManager;
    private final ConsoleRenderer consoleRenderer;
    private final ConsoleSettings settings;

    private int consoleHeight;
    private boolean isDraggingDivider = false;
    private int dragStartY = 0;
    private int dragStartHeight = 0;

    private static final int DIVIDER_HEIGHT = 4;
    private static final int DIVIDER_HOVER_AREA = 8;

    private DevToolsOverlay() {
        this.consoleRenderer = new ConsoleRenderer();
        this.settings = new ConsoleSettings();
        this.consoleHeight = settings.getConsoleHeight();
    }

    public static DevToolsOverlay getInstance() {
        return INSTANCE;
    }

    public void toggle() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen instanceof EdmlScreen edmlScreen) {
            if (active && attachedScreen == edmlScreen) {
                deactivate();
            } else {
                activate(edmlScreen);
            }
        } else {
            HTMLMyScreen.LOGGER.warn("Cannot toggle DevTools - current screen is not an EdmlScreen");
        }
    }

    public void activate(EdmlScreen screen) {
        if (active && attachedScreen == screen) {
            return;
        }

        this.attachedScreen = screen;
        this.tabManager = new TabManager(screen);
        this.active = true;

        resizeScreen(screen);

        HTMLMyScreen.LOGGER.info("DevTools overlay activated for {}",
                screen.getClass().getSimpleName());
    }

    public void deactivate() {
        if (!active || attachedScreen == null) {
            return;
        }

        restoreScreen(attachedScreen);

        this.active = false;
        this.attachedScreen = null;
        this.tabManager = null;

        HTMLMyScreen.LOGGER.info("DevTools overlay deactivated");
    }

    private void resizeScreen(EdmlScreen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        int fullHeight = client.getWindow().getScaledHeight();
        int newHeight = fullHeight - consoleHeight;

        screen.resize(client, client.getWindow().getScaledWidth(), newHeight);

        HTMLMyScreen.LOGGER.debug("Screen resized: {}x{}",
                client.getWindow().getScaledWidth(), newHeight);
    }

    private void restoreScreen(EdmlScreen screen) {
        MinecraftClient client = MinecraftClient.getInstance();
        screen.resize(client,
                client.getWindow().getScaledWidth(),
                client.getWindow().getScaledHeight());

        HTMLMyScreen.LOGGER.debug("Screen restored to full size");
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAttachedTo(EdmlScreen screen) {
        return active && attachedScreen == screen;
    }

    public int getConsoleHeight() {
        return active ? consoleHeight : 0;
    }

    public void render(DrawContext context, int screenWidth, int screenHeight,
                       int mouseX, int mouseY, EdmlComponent rootComponent) {
        if (!active || tabManager == null || rootComponent == null) {
            return;
        }

        int dividerY = screenHeight - DIVIDER_HEIGHT / 2;

        if (settings.isDebugBoxesEnabled()) {
            try {
                int scrollOffset = getScrollOffset(rootComponent);
                DebugBoxRenderer.render(context, rootComponent, settings, scrollOffset);
            } catch (Exception e) {
                HTMLMyScreen.LOGGER.error("Error rendering debug boxes", e);
            }
        }

        renderDivider(context, dividerY, screenWidth, mouseY);

        consoleRenderer.render(context, 0, screenHeight, screenWidth, consoleHeight,
                mouseX, mouseY - screenHeight, tabManager, settings);
    }

    private int getScrollOffset(EdmlComponent rootComponent) {
        if (rootComponent instanceof ScrollableComponent scrollable) {
            return scrollable.getScrollOffset();
        }
        return 0;
    }

    private void renderDivider(DrawContext context, int dividerY, int width, int mouseY) {
        boolean isHovered = Math.abs(mouseY - dividerY) < DIVIDER_HOVER_AREA;
        int color = isDraggingDivider ? 0xFF00AAFF : (isHovered ? 0xFF666666 : 0xFF444444);

        context.fill(0, dividerY - 1, width, dividerY + DIVIDER_HEIGHT - 1, color);

        if (isHovered || isDraggingDivider) {
            int centerX = width / 2;
            context.fill(centerX - 20, dividerY, centerX + 20, dividerY + 2, 0xFFFFFFFF);
        }
    }

    public boolean handleMouseClick(double mouseX, double mouseY, int button, int screenHeight) {
        if (!active || tabManager == null) {
            return false;
        }

        int dividerY = screenHeight - DIVIDER_HEIGHT / 2;

        if (Math.abs(mouseY - dividerY) < DIVIDER_HOVER_AREA && button == 0) {
            isDraggingDivider = true;
            dragStartY = (int) mouseY;
            dragStartHeight = consoleHeight;
            return true;
        }

        if (mouseY >= screenHeight) {
            int consoleY = screenHeight;
            double relativeMouseX = mouseX;
            double relativeMouseY = mouseY - consoleY;

            if (consoleRenderer.handleTabClick((int) relativeMouseX, (int) relativeMouseY, tabManager, 0)) {
                return true;
            }

            double contentMouseX = relativeMouseX - ConsoleRenderer.PADDING;
            double contentMouseY = relativeMouseY - ConsoleRenderer.TAB_HEIGHT - ConsoleRenderer.PADDING;

            return tabManager.handleClick(contentMouseX, contentMouseY, button);
        }

        return false;
    }

    public boolean handleMouseDrag(double mouseY, int button) {
        if (!active || !isDraggingDivider || button != 0 || attachedScreen == null) {
            return false;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        int fullHeight = client.getWindow().getScaledHeight();

        int delta = dragStartY - (int) mouseY;
        consoleHeight = Math.clamp(dragStartHeight + delta, 100, fullHeight - 100);
        settings.setConsoleHeight(consoleHeight);

        resizeScreen(attachedScreen);

        return true;
    }

    public boolean handleMouseRelease(int button) {
        if (isDraggingDivider && button == 0) {
            isDraggingDivider = false;
            return true;
        }
        return false;
    }

    public boolean handleScroll(double mouseX, double mouseY, double amount, int screenHeight) {
        if (!active || tabManager == null) {
            return false;
        }

        if (mouseY >= screenHeight) {
            return tabManager.handleScroll(mouseX, mouseY - screenHeight, amount);
        }

        return false;
    }

    public ConsoleSettings getSettings() {
        return settings;
    }
}