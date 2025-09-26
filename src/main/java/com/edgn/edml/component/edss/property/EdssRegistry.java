package com.edgn.edml.component.edss.property;

import com.edgn.edml.component.edss.properties.*;
import com.edgn.edml.component.edss.properties.margin.*;
import com.edgn.edml.component.edss.properties.color.BackgroundColorProperty;
import com.edgn.edml.component.edss.properties.color.ColorProperty;
import com.edgn.edml.component.edss.properties.padding.*;

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

        registry.registerProperty(new PaddingTopProperty());
        registry.registerProperty(new PaddingRightProperty());
        registry.registerProperty(new PaddingBottomProperty());
        registry.registerProperty(new PaddingLeftProperty());
        registry.registerProperty(new MarginTopProperty());
        registry.registerProperty(new MarginRightProperty());
        registry.registerProperty(new MarginBottomProperty());
        registry.registerProperty(new MarginLeftProperty());

        return registry;
    }
}