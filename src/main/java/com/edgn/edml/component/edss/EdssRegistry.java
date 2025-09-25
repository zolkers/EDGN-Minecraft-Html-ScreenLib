package com.edgn.edml.component.edss;

import com.edgn.edml.component.edss.properties.*;

public final class EdssRegistry {
    private static final IEdssRegistry INSTANCE = createDefaultCssRegistry();

    public static IEdssRegistry getInstance() {
        return INSTANCE;
    }

    private static IEdssRegistry createDefaultCssRegistry() {
        DefaultEdssRegistry registry = new DefaultEdssRegistry();

        registry.registerProperty(new BackgroundColorProperty());
        registry.registerProperty(new ColorProperty());
        registry.registerProperty(new WidthProperty());
        registry.registerProperty(new HeightProperty());
        registry.registerProperty(new PaddingProperty());
        registry.registerProperty(new MarginProperty());

        return registry;
    }
}