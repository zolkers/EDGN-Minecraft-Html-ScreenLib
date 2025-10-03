package com.edgn.devtools.tabs;

import com.edgn.edml.screen.EdmlScreen;
import com.edgn.edml.utils.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public final class DebugTab implements IConsoleTab {
    private final EdmlScreen screen;
    public DebugTab(EdmlScreen screen) {
        this.screen = screen;
    }

    @Override
    public String getName() {
        return "Debug";
    }

    @Override
    public void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY) {
        int elementsX = x + 10;
        int screenY = y + 10;
        int fpsY = y + 30;
        int mouseCoordsY = y + 50;

        context.drawText(MinecraftClient.getInstance().textRenderer, "Current screen name: " + this.screen.getTitle().getString()
                , elementsX, screenY, ColorUtils.parseColor("white"),true);

        context.drawText(MinecraftClient.getInstance().textRenderer, "Game FPS: " + MinecraftClient.getInstance().getCurrentFps()
                , elementsX, fpsY, ColorUtils.parseColor("white"),true);

        context.drawText(MinecraftClient.getInstance().textRenderer, "Mouse coordinates: {" + mouseX + "," + mouseY + "}"
                , elementsX, mouseCoordsY, ColorUtils.parseColor("white"),true);

    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        return false;
    }
}
