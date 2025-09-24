package com.edgn.example.commands;

import com.edgn.example.screens.ScreenOpener;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class EdgnCommands {
    
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            registerOpenScreenCommand(dispatcher);
        });
    }
    
    private static void registerOpenScreenCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("edgn")
                .then(CommandManager.literal("open")
                    .then(CommandManager.literal("example")
                        .executes(EdgnCommands::openExampleScreen)
                    )
                )
        );
    }
    
    private static int openExampleScreen(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.literal("Opening EDGN Example Screen..."), false);
        
        ScreenOpener.openTestScreen();
        
        return 1;
    }
}