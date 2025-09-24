package com.edgn.prog.component.html.components;

import com.edgn.prog.annotations.AwaitOverride;
import com.edgn.prog.component.*;
import com.edgn.prog.component.html.AbstractEdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public abstract class CssAwareComponent extends AbstractEdgnComponent
        implements RenderableComponent, SizedComponent, PaddedComponent, MarginComponent, VirtualComponent {

    protected int backgroundColor = 0x00000000;
    protected int width = -1;
    protected int height = -1;
    protected int[] padding = {0, 0, 0, 0};
    protected int[] margin = {0, 0, 0, 0};

    private boolean visible = true;
    private VirtualBounds virtualBounds = new VirtualBounds(0, 0, 0, 0);
    private boolean wasInViewportLastFrame = true;

    protected CssAwareComponent(String tagName, Set<String> validAttributes) {
        super(tagName, validAttributes);
        VirtualizationManager.getInstance().registerComponent(this);
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isInViewport(MinecraftRenderContext context) {
        return virtualBounds.isInViewport(context.width(), context.height());
    }

    @Override
    public void updateVirtualization(MinecraftRenderContext context) {
        boolean currentlyInViewport = isInViewport(context);

        int cullingDistance = VirtualizationManager.getInstance().getCullingDistance();
        VirtualBounds expandedViewport = new VirtualBounds(
                -cullingDistance,
                -cullingDistance,
                context.width() + 2 * cullingDistance,
                context.height() + 2 * cullingDistance
        );

        this.visible = visible && virtualBounds.intersects(expandedViewport);

        if (wasInViewportLastFrame != currentlyInViewport) {
            onViewportStatusChanged(currentlyInViewport);
        }

        wasInViewportLastFrame = currentlyInViewport;

        for (var child : children) {
            if (child instanceof VirtualComponent virtualChild) {
                virtualChild.updateVirtualization(context);
            }
        }
    }

    @Override
    public VirtualBounds getVirtualBounds() {
        return virtualBounds;
    }

    @Override
    public void setVirtualBounds(VirtualBounds bounds) {
        this.virtualBounds = bounds;
    }

    @AwaitOverride("Override this method to handle viewport changes")
    protected void onViewportStatusChanged(boolean inViewport) {}

    @Override
    public final void render(MinecraftRenderContext context) {
        if (!visible || !shouldRender(context)) {
            return;
        }

        updateVirtualBounds(context);
        renderInternal(context);
    }

    protected boolean shouldRender(MinecraftRenderContext context) {
        int culling = VirtualizationManager.getInstance().getCullingDistance();
        VirtualBounds expandedViewport = new VirtualBounds(
                -culling, -culling,
                context.width() + 2 * culling,
                context.height() + 2 * culling
        );

        return virtualBounds.intersects(expandedViewport);
    }

    protected void updateVirtualBounds(MinecraftRenderContext context) {
        int[] renderBounds = calculateRenderBounds(0, 0, context.width(), context.height());
        virtualBounds = new VirtualBounds(renderBounds[0], renderBounds[1], renderBounds[2], renderBounds[3]);
    }

    protected abstract void renderInternal(MinecraftRenderContext context);

    // ========== CSS INTERFACE IMPLEMENTATIONS ==========

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setPadding(int top, int right, int bottom, int left) {
        this.padding = new int[]{top, right, bottom, left};
    }

    @Override
    public int[] getPadding() {
        return padding.clone();
    }

    @Override
    public void setMargin(int top, int right, int bottom, int left) {
        this.margin = new int[]{top, right, bottom, left};
    }

    @Override
    public int[] getMargin() {
        return margin.clone();
    }
    protected void renderBackground(MinecraftRenderContext context, int x, int y, int w, int h) {
        if ((backgroundColor & 0xFF000000) != 0) {
            context.drawRect(x, y, w, h, backgroundColor);
        }
    }

    protected int[] calculateRenderBounds(int containerX, int containerY, int containerWidth, int containerHeight) {
        int marginTop = margin[0];
        int marginRight = margin[1];
        int marginBottom = margin[2];
        int marginLeft = margin[3];

        int renderX = containerX + marginLeft;
        int renderY = containerY + marginTop;
        int renderWidth = (width > 0 ? width : containerWidth) - marginLeft - marginRight;
        int renderHeight = (height > 0 ? height : containerHeight) - marginTop - marginBottom;

        renderWidth = Math.max(0, renderWidth);
        renderHeight = Math.max(0, renderHeight);

        return new int[]{renderX, renderY, renderWidth, renderHeight};
    }

    protected int[] calculateContentBounds(int renderX, int renderY, int renderWidth, int renderHeight) {
        int paddingTop = padding[0];
        int paddingRight = padding[1];
        int paddingBottom = padding[2];
        int paddingLeft = padding[3];

        int contentX = renderX + paddingLeft;
        int contentY = renderY + paddingTop;
        int contentWidth = renderWidth - paddingLeft - paddingRight;
        int contentHeight = renderHeight - paddingTop - paddingBottom;

        contentWidth = Math.max(0, contentWidth);
        contentHeight = Math.max(0, contentHeight);

        return new int[]{contentX, contentY, contentWidth, contentHeight};
    }

    protected void dispose() {
        VirtualizationManager.getInstance().unregisterComponent(this);
    }
}