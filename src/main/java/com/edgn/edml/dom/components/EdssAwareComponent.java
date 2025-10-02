package com.edgn.edml.dom.components;

import com.edgn.edml.annotations.AwaitOverride;
import com.edgn.edml.core.rendering.RenderableComponent;
import com.edgn.edml.core.events.resize.ResizableComponent;
import com.edgn.edml.core.rendering.SizedComponent;
import com.edgn.edml.dom.components.attributes.AttributeProcessor;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.ui.virtual.VirtualBounds;
import com.edgn.edml.ui.virtual.VirtualComponent;
import com.edgn.edml.ui.virtual.VirtualizationManager;
import com.edgn.edml.dom.styling.registry.IEdssRegistry;
import com.edgn.edml.dom.styling.EdssRule;
import com.edgn.edml.core.component.AbstractEdmlComponent;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.core.events.resize.ResizeEvent;
import com.edgn.edml.layout.box.BoxModel;
import com.edgn.edml.layout.box.BoxModelComponent;
import com.edgn.edml.layout.box.Margin;
import com.edgn.edml.layout.box.Padding;
import com.edgn.edml.core.rendering.MinecraftRenderContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class EdssAwareComponent extends AbstractEdmlComponent
        implements RenderableComponent, SizedComponent, BoxModelComponent, VirtualComponent, AttributeProcessor, ResizableComponent {

    protected BoxModel boxModel;
    protected int backgroundColor = 0x00000000;
    protected int calculatedX = 0;
    protected int calculatedY = 0;
    protected boolean visible = true;
    protected VirtualBounds virtualBounds;
    protected boolean needsLayout = true;
    private boolean sizeInvalidated = false;
    private MinecraftRenderContext cachedContext;

    protected EdssAwareComponent(String tagName, Set<String> validAttributes) {
        super(tagName, validAttributes);
        this.boxModel = new BoxModel(0, 0, Padding.none(), Margin.none());
        this.virtualBounds = new VirtualBounds(0, 0, 0, 0);
        VirtualizationManager.getInstance().registerComponent(this);
    }

    @Override
    public BoxModel getBoxModel() {
        return boxModel;
    }

    @Override
    public void setBoxModel(BoxModel boxModel) {
        this.boxModel = boxModel;
        updateVirtualBounds();
        markNeedsLayout();
    }

    @Override
    public void setWidth(int width) {
        if (this.boxModel.width() != width) {
            this.boxModel = new BoxModel(width, boxModel.height(), boxModel.padding(), boxModel.margin());
            markNeedsLayout();
        }
    }

    @Override
    public void setHeight(int height) {
        int minHeight = getMinimumHeight();
        int actualHeight = Math.max(height, minHeight);
        if (this.boxModel.height() != actualHeight) {
            this.boxModel = new BoxModel(boxModel.width(), actualHeight, boxModel.padding(), boxModel.margin());
            markNeedsLayout();
        }
    }

    @Override
    public int getWidth() {
        return boxModel.width();
    }

    @Override
    public int getHeight() {
        return boxModel.height();
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public void invalidateSize(MinecraftRenderContext context) {
        this.cachedContext = context;
        this.sizeInvalidated = true;
        markNeedsLayout();

        for (var child : children) {
            if (child instanceof ResizableComponent resizable) {
                resizable.invalidateSize(context);
            }
        }
    }

    @Override
    public void onParentResize(ResizeEvent event) {
        boolean hasRelativeWidth = hasRelativeSizing(TagAttribute.WIDTH);
        boolean hasRelativeHeight = hasRelativeSizing(TagAttribute.HEIGHT);

        if (hasRelativeWidth || hasRelativeHeight) {
            sizeInvalidated = true;
            markNeedsLayout();
        }

        for (var child : children) {
            if (child instanceof ResizableComponent resizable) {
                resizable.onParentResize(event);
            }
        }
    }

    private boolean hasRelativeSizing(TagAttribute attr) {
        String value = getAttribute(attr.getProperty(), "");
        return value.contains("%") || value.contains("vw") || value.contains("vh");
    }

    @Override
    public boolean needsLayout() {
        return needsLayout || sizeInvalidated;
    }

    @Override
    public void markNeedsLayout() {
        this.needsLayout = true;
    }

    @Override
    public void clearNeedsLayout() {
        this.needsLayout = false;
        this.sizeInvalidated = false;
    }

    @Override
    public final void render(MinecraftRenderContext context) {
        this.cachedContext = context;

        if (!visible || !shouldRender(context)) {
            return;
        }
        renderSelf(context);
        renderChildren(context);
    }

    protected void renderSelf(MinecraftRenderContext context) {
        renderBackground(context);

        Padding padding = boxModel.padding();
        int contentX = calculatedX + padding.left();
        int contentY = calculatedY + padding.top();
        int contentWidth = boxModel.contentWidth();
        int contentHeight = boxModel.contentHeight();

        renderContent(context, contentX, contentY, contentWidth, contentHeight);
    }

    protected void renderChildren(MinecraftRenderContext context) {
        children.forEach(child -> child.render(context));
    }

    protected void renderBackground(MinecraftRenderContext context) {
        if ((backgroundColor & 0xFF000000) != 0) {
            context.drawRect(calculatedX, calculatedY, boxModel.width(), boxModel.height(), backgroundColor);
        }
    }

    protected abstract void renderContent(MinecraftRenderContext context, int x, int y, int width, int height);

    public void setCalculatedBounds(int x, int y, int width, int height) {
        boolean changed = this.calculatedX != x || this.calculatedY != y ||
                boxModel.width() != width || boxModel.height() != height;

        this.calculatedX = x;
        this.calculatedY = y;

        if (boxModel.width() != width || boxModel.height() != height) {
            this.boxModel = new BoxModel(width, height, boxModel.padding(), boxModel.margin());
            markNeedsLayout();
        }

        updateVirtualBounds();

        if (changed) {
            onBoundsChanged();
        }
    }

    @AwaitOverride
    protected void onBoundsChanged() {
        // Hook for subclasses
    }

    private void updateVirtualBounds() {
        this.virtualBounds = new VirtualBounds(calculatedX, calculatedY, boxModel.width(), boxModel.height());
    }

    public int getCalculatedX() { return calculatedX; }
    public int getCalculatedY() { return calculatedY; }
    public int getCalculatedWidth() { return boxModel.width(); }
    public int getCalculatedHeight() { return boxModel.height(); }

    @Override
    public void processAttributes(List<EdssRule> cssRules, IEdssRegistry cssRegistry, MinecraftRenderContext context) {
        this.cachedContext = context;
        processSpecificAttributes(context);

        String styleAttr = getAttribute(TagAttribute.STYLE.getProperty(), "");
        if (!styleAttr.isEmpty()) {
            processInlineStyles(styleAttr, cssRegistry, context);
        }

        String classAttr = getAttribute(TagAttribute.CLASS.getProperty(), "");
        if (!classAttr.isEmpty()) {
            processClassSelectors(classAttr, cssRules, cssRegistry, context);
        }

        String idAttr = getAttribute(TagAttribute.ID.getProperty(), "");
        if (!idAttr.isEmpty()) {
            processIdSelectors(idAttr, cssRules, cssRegistry, context);
        }
    }

    @AwaitOverride
    protected void processSpecificAttributes(MinecraftRenderContext context) {}

    private void processInlineStyles(String styleValue, IEdssRegistry cssRegistry, MinecraftRenderContext context) {
        Arrays.stream(styleValue.split(";"))
                .map(String::trim)
                .filter(decl -> !decl.isEmpty())
                .forEach(declaration -> {
                    String[] parts = declaration.split(":", 2);
                    if (parts.length == 2) {
                        cssRegistry.applyCssRule(this, parts[0].trim(), parts[1].trim(), context);
                    }
                });
    }

    private void processClassSelectors(String classList, List<EdssRule> cssRules, IEdssRegistry cssRegistry, MinecraftRenderContext context) {
        String[] classes = classList.trim().split("\\s+");
        cssRules.stream()
                .filter(rule -> matchesClassSelector(rule.selector(), classes))
                .forEach(rule -> applyRule(rule, cssRegistry, context));
    }

    private void processIdSelectors(String id, List<EdssRule> cssRules, IEdssRegistry cssRegistry, MinecraftRenderContext context) {
        cssRules.stream()
                .filter(rule -> matchesIdSelector(rule.selector(), id))
                .forEach(rule -> applyRule(rule, cssRegistry, context));
    }

    private void applyRule(EdssRule rule, IEdssRegistry cssRegistry, MinecraftRenderContext context) {
        rule.declarations().forEach((property, value) ->
                cssRegistry.applyCssRule(this, property, value, context));
    }

    private boolean matchesClassSelector(String selector, String[] componentClasses) {
        return selector.startsWith(".") &&
                Arrays.asList(componentClasses).contains(selector.substring(1));
    }

    private boolean matchesIdSelector(String selector, String componentId) {
        return selector.startsWith("#") &&
                selector.substring(1).equals(componentId);
    }

    @Override
    public boolean isVisible() { return visible; }

    @Override
    public void setVisible(boolean visible) { this.visible = visible; }

    @Override
    public VirtualBounds getVirtualBounds() { return virtualBounds; }

    @Override
    public void setVirtualBounds(VirtualBounds bounds) { this.virtualBounds = bounds; }

    @Override
    public boolean isInViewport(MinecraftRenderContext context) {
        return virtualBounds.isInViewport(context.width(), context.height());
    }

    @Override
    public void updateVirtualization(MinecraftRenderContext context) {
        boolean inViewport = isInViewport(context);
        onViewportStatusChanged(inViewport);

        children.stream()
                .filter(VirtualComponent.class::isInstance)
                .map(VirtualComponent.class::cast)
                .forEach(child -> child.updateVirtualization(context));
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

    @AwaitOverride
    protected void onViewportStatusChanged(boolean inViewport) {}

    protected int getMinimumHeight() {
        if (EdmlEnum.BODY.getTagName().equals(getTagName())) {
            return cachedContext != null ? cachedContext.height() : 0;
        }
        return 0;
    }

    protected void dispose() {
        VirtualizationManager.getInstance().unregisterComponent(this);
        this.cachedContext = null;
    }
}