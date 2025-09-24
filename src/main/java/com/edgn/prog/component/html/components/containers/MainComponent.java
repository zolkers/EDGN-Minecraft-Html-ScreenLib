package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class MainComponent extends CssAwareComponent {
    public static final Set<String> MAIN_ATTRIBUTES = Set.of(
            "class", "id", "style", "role"
    );
    public MainComponent() {
        super("main", MAIN_ATTRIBUTES);
    }

    @Override
    protected void renderInternal(MinecraftRenderContext context) {
        int[] renderBounds = calculateRenderBounds(0, 0, context.width(), context.height());
        int renderX = renderBounds[0];
        int renderY = renderBounds[1];
        int renderWidth = renderBounds[2];
        int renderHeight = renderBounds[3];

        if (height <= 0) {
            renderHeight = calculateMainHeight(context);
        }

        renderBackground(context, renderX, renderY, renderWidth, renderHeight);

        int[] contentBounds = calculateContentBounds(renderX, renderY, renderWidth, renderHeight);
        int contentX = contentBounds[0];
        int contentY = contentBounds[1];
        int contentWidth = contentBounds[2];
        int contentHeight = contentBounds[3];

        context.pushTransform(contentX, contentY, contentWidth, contentHeight);

        for (EdgnComponent child : children) {
            child.render(context);
        }

        context.popTransform();
    }

    private int calculateMainHeight(MinecraftRenderContext context) {
        // Subtract typical header and footer heights
        return Math.max(100, context.height() - 100);
    }
}