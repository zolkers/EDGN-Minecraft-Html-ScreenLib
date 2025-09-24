package com.edgn.prog.component.css;

import com.edgn.prog.component.css.properties.*;

public final class EdgnCssRegistry {
    private static final CssRegistry INSTANCE = createDefaultCssRegistry();

    public static CssRegistry getInstance() {
        return INSTANCE;
    }

    private static CssRegistry createDefaultCssRegistry() {
        DefaultCssRegistry registry = new DefaultCssRegistry();

        registry.registerProperty(new BackgroundColorProperty());
        registry.registerProperty(new ColorProperty());
        registry.registerProperty(new WidthProperty());
        registry.registerProperty(new HeightProperty());
        registry.registerProperty(new PaddingProperty());
        registry.registerProperty(new MarginProperty());

        return registry;
    }
}