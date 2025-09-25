package com.edgn.prog.component.html.components;

import com.edgn.prog.component.*;
import com.edgn.prog.component.html.AbstractEdgnComponent;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public abstract class CssAwareComponent extends AbstractEdgnComponent
        implements RenderableComponent, SizedComponent, PaddedComponent, MarginComponent, VirtualComponent {

    protected int backgroundColor = 0x00000000;
    protected int width = -1;
    protected int height = -1;
    protected int[] padding = {0, 0, 0, 0};
    protected int[] margin = {0, 0, 0, 0};
    protected int calculatedX = 0;
    protected int calculatedY = 0;
    protected int calculatedWidth = 0;
    protected int calculatedHeight = 0;
    private boolean visible = true;
    private VirtualBounds virtualBounds = new VirtualBounds(0, 0, 0, 0);

    protected CssAwareComponent(String tagName, Set<String> validAttributes) {
        super(tagName, validAttributes);
        VirtualizationManager.getInstance().registerComponent(this);
    }

    @Override
    public final void render(MinecraftRenderContext context) {
        if (!visible || !shouldRender(context)) {
            return;
        }

        renderSelf(context);
        renderChildren(context);
    }

    protected void renderSelf(MinecraftRenderContext context) {
        renderBackground(context, calculatedX, calculatedY, calculatedWidth, calculatedHeight);
        renderContent(context, calculatedX, calculatedY, calculatedWidth, calculatedHeight);
    }

    protected void renderChildren(MinecraftRenderContext context) {
        for (EdgnComponent child : children) {
            child.render(context);
        }
    }

    protected abstract void renderContent(MinecraftRenderContext context, int x, int y, int width, int height);

    public void setCalculatedBounds(int x, int y, int width, int height) {
        this.calculatedX = x;
        this.calculatedY = y;
        this.calculatedWidth = width;
        this.calculatedHeight = height;
        this.virtualBounds = new VirtualBounds(x, y, width, height);
    }

    public int getCalculatedX() { return calculatedX; }
    public int getCalculatedY() { return calculatedY; }
    public int getCalculatedWidth() { return calculatedWidth; }
    public int getCalculatedHeight() { return calculatedHeight; }


    protected void renderBackground(MinecraftRenderContext context, int x, int y, int w, int h) {
        if ((backgroundColor & 0xFF000000) != 0) {
            context.drawRect(x, y, w, h, backgroundColor);
        }
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

    @Override
    public boolean isVisible() { return visible; }

    @Override
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public boolean isInViewport(MinecraftRenderContext context) {
        return virtualBounds.isInViewport(context.width(), context.height());
    }

    @Override
    public void updateVirtualization(MinecraftRenderContext context) {
        boolean currentlyInViewport = isInViewport(context);
        onViewportStatusChanged(currentlyInViewport);

        for (var child : children) {
            if (child instanceof VirtualComponent virtualChild) {
                virtualChild.updateVirtualization(context);
            }
        }
    }

    @Override
    public VirtualBounds getVirtualBounds() { return virtualBounds; }

    @Override
    public void setVirtualBounds(VirtualBounds bounds) { this.virtualBounds = bounds; }

    protected void onViewportStatusChanged(boolean inViewport) {}
    @Override
    public void setBackgroundColor(int color) { this.backgroundColor = color; }

    @Override
    public int getBackgroundColor() { return backgroundColor; }

    @Override
    public void setWidth(int width) { this.width = width; }

    @Override
    public void setHeight(int height) { this.height = height; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public void setPadding(int top, int right, int bottom, int left) {
        this.padding = new int[]{top, right, bottom, left};
    }

    @Override
    public int[] getPadding() { return padding.clone(); }

    @Override
    public void setMargin(int top, int right, int bottom, int left) {
        this.margin = new int[]{top, right, bottom, left};
    }

    @Override
    public int[] getMargin() { return margin.clone(); }

    protected void dispose() {
        VirtualizationManager.getInstance().unregisterComponent(this);
    }
}