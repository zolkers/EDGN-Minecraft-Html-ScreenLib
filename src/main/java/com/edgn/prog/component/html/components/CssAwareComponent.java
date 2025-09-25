package com.edgn.prog.component.html.components;

import com.edgn.prog.component.RenderableComponent;
import com.edgn.prog.component.SizedComponent;
import com.edgn.prog.component.attribute.AttributeProcessor;
import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.css.CssRegistry;
import com.edgn.prog.component.css.CssRule;
import com.edgn.prog.component.html.AbstractEdgnComponent;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.layout.box.BoxModel;
import com.edgn.prog.layout.box.BoxModelComponent;
import com.edgn.prog.layout.spacing.Margin;
import com.edgn.prog.layout.spacing.Padding;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public abstract class CssAwareComponent extends AbstractEdgnComponent
        implements RenderableComponent, SizedComponent, BoxModelComponent, VirtualComponent, AttributeProcessor {

    protected BoxModel boxModel;
    protected int backgroundColor = 0x00000000;
    protected int calculatedX = 0;
    protected int calculatedY = 0;
    protected boolean visible = true;
    protected VirtualBounds virtualBounds;

    protected CssAwareComponent(String tagName, Set<String> validAttributes) {
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
    }

    // ================== SizedComponent Implementation ==================

    @Override
    public void setWidth(int width) {
        this.boxModel = new BoxModel(width, boxModel.height(), boxModel.padding(), boxModel.margin());
    }

    @Override
    public void setHeight(int height) {
        this.boxModel = new BoxModel(boxModel.width(), height, boxModel.padding(), boxModel.margin());
    }

    @Override
    public int getWidth() {
        return boxModel.width();
    }

    @Override
    public int getHeight() {
        return boxModel.height();
    }

    // ================== RenderableComponent Implementation ==================

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    @Override
    public int getBackgroundColor() {
        return backgroundColor;
    }

    // ================== Rendering System ==================

    @Override
    public final void render(MinecraftRenderContext context) {
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

    // ================== Layout System ==================

    public void setCalculatedBounds(int x, int y, int width, int height) {
        this.calculatedX = x;
        this.calculatedY = y;

        if (boxModel.width() != width || boxModel.height() != height) {
            this.boxModel = new BoxModel(width, height, boxModel.padding(), boxModel.margin());
        }

        updateVirtualBounds();
    }

    private void updateVirtualBounds() {
        this.virtualBounds = new VirtualBounds(calculatedX, calculatedY, boxModel.width(), boxModel.height());
    }

    public int getCalculatedX() { return calculatedX; }
    public int getCalculatedY() { return calculatedY; }
    public int getCalculatedWidth() { return boxModel.width(); }
    public int getCalculatedHeight() { return boxModel.height(); }

    // ================== CSS Processing ==================

    @Override
    public void processAttributes(List<CssRule> cssRules, CssRegistry cssRegistry, MinecraftRenderContext context) {
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

    protected void processSpecificAttributes(MinecraftRenderContext context) {
        // Override in subclasses
    }

    private void processInlineStyles(String styleValue, CssRegistry cssRegistry, MinecraftRenderContext context) {
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

    private void processClassSelectors(String classList, List<CssRule> cssRules, CssRegistry cssRegistry, MinecraftRenderContext context) {
        String[] classes = classList.trim().split("\\s+");
        cssRules.stream()
                .filter(rule -> matchesClassSelector(rule.selector(), classes))
                .forEach(rule -> applyRule(rule, cssRegistry, context));
    }

    private void processIdSelectors(String id, List<CssRule> cssRules, CssRegistry cssRegistry, MinecraftRenderContext context) {
        cssRules.stream()
                .filter(rule -> matchesIdSelector(rule.selector(), id))
                .forEach(rule -> applyRule(rule, cssRegistry, context));
    }

    private void applyRule(CssRule rule, CssRegistry cssRegistry, MinecraftRenderContext context) {
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

    // ================== Virtualization ==================

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

    protected void onViewportStatusChanged(boolean inViewport) {
        // Override in subclasses
    }

    // ================== Cleanup ==================

    protected void dispose() {
        VirtualizationManager.getInstance().unregisterComponent(this);
    }
}