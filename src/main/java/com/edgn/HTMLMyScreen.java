package com.edgn;

import com.edgn.example.screens.ScreenOpener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public final class HTMLMyScreen implements ModInitializer {
	public static final String MOD_ID = "html-my-screen";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static KeyBinding openTestScreenKey;

    @Override
    public void onInitialize() {
        if(!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
        openTestScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open EDGN Test Screen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "EDGN Framework"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openTestScreenKey.wasPressed()) {
                ScreenOpener.openTestScreen();
            }
        });
    }
}