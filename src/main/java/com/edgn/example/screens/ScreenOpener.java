package com.edgn.example.screens;

import com.edgn.edml.minecraft.screen.EdmlScreenFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class ScreenOpener {

    public static void openTestScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> {
                var screen = EdmlScreenFactory.createScreenWithPaths(
                        Text.literal("Test Screen"),
                        "screens/test",
                        "themes/dark"
                );
                client.setScreen(screen);
            });
        }
    }
}