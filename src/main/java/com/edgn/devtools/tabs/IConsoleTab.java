package com.edgn.devtools.tabs;

import net.minecraft.client.gui.DrawContext;

public interface IConsoleTab {
    String getName();
    void render(DrawContext context, int x, int y, int width, int height, int mouseX, int mouseY);
    boolean handleClick(double mouseX, double mouseY, int button);

    default void onActivated() {}
    default void onDeactivated() {}
    default boolean handleScroll(double mouseX, double mouseY, double amount) {
        return false;
    }
}
