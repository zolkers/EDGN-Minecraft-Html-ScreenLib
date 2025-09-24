package com.edgn.prog.minecraft.screen;

import net.minecraft.text.Text;

public final class EdgnScreenFactory {

    /**
     * Creates screen from standardized resource paths
     * @param title Screen title
     * @param resourceName Base name (e.g. "main_menu" loads main_menu.edgn and main_menu.css)
     */
    public static EdgnScreen createScreen(Text title, String resourceName) {
        return new StandardResourceEdgnScreen(title, resourceName);
    }

    /**
     * Creates screen with different HTML and CSS names
     * @param title Screen title
     * @param htmlName HTML file name (loads from /edgn/html/)
     * @param cssName CSS file name (loads from /edgn/css/)
     */
    public static EdgnScreen createScreen(Text title, String htmlName, String cssName) {
        return new StandardResourceEdgnScreen(title, htmlName, cssName);
    }

    /**
     * Creates screen with subfolder paths
     * @param title Screen title
     * @param htmlPath Path relative to /edgn/html/ (e.g. "menus/main")
     * @param cssPath Path relative to /edgn/css/ (e.g. "styles/main")
     */
    public static EdgnScreen createScreenWithPaths(Text title, String htmlPath, String cssPath) {
        return new PathBasedEdgnScreen(title, htmlPath, cssPath);
    }

    /**
     * Quick factory method for same-named resources
     */
    public static EdgnScreen createScreen(String screenName) {
        return createScreen(Text.literal(screenName), screenName);
    }
}