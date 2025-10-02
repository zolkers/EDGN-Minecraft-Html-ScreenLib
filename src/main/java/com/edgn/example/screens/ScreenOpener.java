package com.edgn.example.screens;

import com.edgn.example.screens.demo.LargeListTestScreen;
import net.minecraft.client.MinecraftClient;

public final class ScreenOpener {

    public static void openTestScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> client.setScreen(new LargeListTestScreen()));
        }
    }
}