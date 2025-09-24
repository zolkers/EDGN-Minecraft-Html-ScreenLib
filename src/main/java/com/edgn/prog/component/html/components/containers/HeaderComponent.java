package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class HeaderComponent extends CssAwareComponent {

    public static final Set<String> HEADER_ATTRIBUTES = Set.of(
            "class", "id", "style", "role", "aria-level"
    );

    public HeaderComponent() {
        super("header", HEADER_ATTRIBUTES);
    }

    @Override
    protected void renderInternal(MinecraftRenderContext context) {
        int headerHeight = calculateHeaderHeight();

        int[] renderBounds = calculateRenderBounds(0, 0, context.width(), headerHeight);
        int renderX = renderBounds[0];
        int renderY = renderBounds[1];
        int renderWidth = renderBounds[2];
        int renderHeight = renderBounds[3];

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

    private int calculateHeaderHeight() {
        if (height > 0) return height;

        try {
            return Integer.parseInt(getAttribute("height", "60"));
        } catch (NumberFormatException e) {
            return 60;
        }
    }
}