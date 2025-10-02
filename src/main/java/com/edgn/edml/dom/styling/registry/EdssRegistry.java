package com.edgn.edml.dom.styling.registry;

import com.edgn.edml.dom.styling.properties.sizing.HeightProperty;
import com.edgn.edml.dom.styling.properties.sizing.WidthProperty;
import com.edgn.edml.dom.styling.properties.spacing.MarginProperty;
import com.edgn.edml.dom.styling.properties.spacing.PaddingProperty;
import com.edgn.edml.dom.styling.properties.spacing.margin.*;
import com.edgn.edml.dom.styling.properties.color.BackgroundColorProperty;
import com.edgn.edml.dom.styling.properties.color.ColorProperty;
import com.edgn.edml.dom.styling.properties.spacing.padding.*;

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