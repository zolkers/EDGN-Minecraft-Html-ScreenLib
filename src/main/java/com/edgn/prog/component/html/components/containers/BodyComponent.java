package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.component.ClickableComponent;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class BodyComponent extends CssAwareComponent implements ClickableComponent {

    private static final Set<String> BODY_ATTRIBUTES = Set.of(
            "class", "id", "style", "onload", "onunload"
    );

    private int renderX;
    private int renderY;
    private int renderWidth;
    private int renderHeight;

    public BodyComponent() {
        super("body", BODY_ATTRIBUTES);
    }

    @Override
    protected void renderInternal(MinecraftRenderContext context) {
        int[] renderBounds = calculateRenderBounds(0, 0, context.width(), context.height());
        this.renderX = renderBounds[0];
        this.renderY = renderBounds[1];
        this.renderWidth = renderBounds[2];
        this.renderHeight = renderBounds[3];

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

    @Override
    protected void onViewportStatusChanged(boolean inViewport) {
        if (!inViewport) {
            System.out.println("Body component went out of viewport");
        }
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        if (!isPointInBounds(mouseX, mouseY)) {
            return false;
        }

        for (EdgnComponent child : children) {
            if (child instanceof ClickableComponent clickable) {
                if (clickable.handleClick(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public boolean isPointInBounds(double x, double y) {
        return x >= renderX && x < renderX + renderWidth &&
                y >= renderY && y < renderY + renderHeight;
    }
}