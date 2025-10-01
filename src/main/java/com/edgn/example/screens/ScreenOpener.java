package com.edgn.example.screens;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.data.collections.ObservableList;
import com.edgn.edml.minecraft.screen.EdmlScreenFactory;
import com.edgn.edml.minecraft.screen.screens.StandardResourceEdmlScreen;
import com.edgn.example.screens.demo.LargeListTestScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Timer;
import java.util.TimerTask;

public final class ScreenOpener {

    public static void openTestScreen() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.execute(() -> client.setScreen(new LargeListTestScreen()));
        }
    }
}