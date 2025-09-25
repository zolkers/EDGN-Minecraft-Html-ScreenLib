package com.edgn.edml.minecraft.screen;

import com.edgn.edml.minecraft.screen.screens.PathBasedEdmlScreen;
import com.edgn.edml.minecraft.screen.screens.StandardResourceEdmlScreen;
import net.minecraft.text.Text;

public final class EdmlScreenFactory {

    /**
     * Creates screen from standardized resource paths
     * @param title Screen title
     * @param resourceName Base name (e.g. "main_menu" loads main_menu.edgn and main_menu.css)
     */
    public static EdmlScreen createScreen(Text title, String resourceName) {
        return new StandardResourceEdmlScreen(title, resourceName);
    }

    /**
     * Creates screen with different HTML and CSS names
     * @param title Screen title
     * @param htmlName HTML file name (loads from /edgn/html/)
     * @param cssName CSS file name (loads from /edgn/css/)
     */
    public static EdmlScreen createScreen(Text title, String htmlName, String cssName) {
        return new StandardResourceEdmlScreen(title, htmlName, cssName);
    }

    /**
     * Creates screen with subfolder paths
     * @param title Screen title
     * @param htmlPath Path relative to /edgn/html/ (e.g. "menus/main")
     * @param cssPath Path relative to /edgn/css/ (e.g. "styles/main")
     */
    public static EdmlScreen createScreenWithPaths(Text title, String htmlPath, String cssPath) {
        return new PathBasedEdmlScreen(title, htmlPath, cssPath);
    }

    /**
     * Quick factory method for same-named resources
     */
    public static EdmlScreen createScreen(String screenName) {
        return createScreen(Text.literal(screenName), screenName);
    }
}