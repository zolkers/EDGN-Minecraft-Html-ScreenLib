package com.edgn.edml.minecraft.screen.screens;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.annotations.AwaitOverride;
import com.edgn.edml.component.ClickableComponent;
import com.edgn.edml.component.DraggableComponent;
import com.edgn.edml.component.ResizableComponent;
import com.edgn.edml.component.attribute.AttributeProcessor;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.component.AbstractEdmlComponent;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.components.scroll.ScrollManager;
import com.edgn.edml.component.edml.components.ui.VirtualListComponent;
import com.edgn.edml.component.edss.property.EdssRegistry;
import com.edgn.edml.component.edss.property.EdssRule;
import com.edgn.edml.component.edss.property.IEdssRegistry;
import com.edgn.edml.data.binding.BindingContext;
import com.edgn.edml.data.binding.DataBindingEngine;
import com.edgn.edml.events.resize.ResizeEvent;
import com.edgn.edml.events.resize.ResizeListener;
import com.edgn.edml.events.resize.ResizeManager;
import com.edgn.edml.exceptions.EdssParsingException;
import com.edgn.edml.exceptions.EdmlParsingException;
import com.edgn.edml.layout.sizing.IComponentSizeCalculator;
import com.edgn.edml.layout.sizing.ComponentSizeCalculator;
import com.edgn.edml.layout.engine.LayoutEngine;
import com.edgn.edml.layout.engine.ILayoutEngine;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.edml.minecraft.context.FabricRenderContext;
import com.edgn.edml.minecraft.screen.EdmlScreen;
import com.edgn.edml.parser.DefaultEdmlParser;
import com.edgn.edml.parser.EdssParser;
import com.edgn.edml.parser.EdmlParser;
import com.edgn.edml.parser.SimpleEdssParser;
import com.edgn.edml.resources.EdmlResourceLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public class StandardResourceEdmlScreen extends EdmlScreen implements ResizeListener {
    private final EdmlComponent rootComponent;
    private final List<EdssRule> cssRules;
    private final IEdssRegistry cssRegistry;
    private final ILayoutEngine layoutEngine;
    private final BindingContext bindingContext;
    private final DataBindingEngine bindingEngine;

    public StandardResourceEdmlScreen(Text title, String resourceName) {
        this(title, resourceName, resourceName);
    }

    public StandardResourceEdmlScreen(Text title, String htmlName, String cssName) {
        super(title);
        this.cssRegistry = EdssRegistry.getInstance();
        this.bindingContext = new BindingContext();
        this.bindingEngine = new DataBindingEngine(bindingContext);

        IComponentSizeCalculator sizeCalculator = new ComponentSizeCalculator();
        this.layoutEngine = new LayoutEngine(sizeCalculator);

        try {
            String htmlContent = EdmlResourceLoader.loadHtml(htmlName);
            String cssContent = EdmlResourceLoader.loadCss(cssName);

            EdmlParser htmlParser = new DefaultEdmlParser();
            EdssParser cssParser = new SimpleEdssParser();

            this.rootComponent = htmlParser.parseDocument(htmlContent);
            this.cssRules = cssParser.parse(cssContent);

            applyCssToComponents();
            initializeData();
            bindingEngine.processComponent(rootComponent);

            ResizeManager.getInstance().addListener(this);

            HTMLMyScreen.LOGGER.info("StandardResourceEdmlScreen initialized: {}/{}", htmlName, cssName);

        } catch (EdmlParsingException | EdssParsingException e) {
            HTMLMyScreen.LOGGER.error("Failed to load EDGN screen: {}/{} - {}", htmlName, cssName, e.getMessage());
            throw new RuntimeException("Failed to load EDGN screen: " + htmlName + "/" + cssName, e);
        }
    }

    @AwaitOverride
    protected void initializeData() {
        // Hook method - override in subclasses
    }

    @Override
    public void onResize(ResizeEvent event) {
        HTMLMyScreen.LOGGER.debug("Screen resize: {}x{} -> {}x{}",
                event.oldWidth(), event.oldHeight(),
                event.newWidth(), event.newHeight());

        performFullResize(event);
    }

    private void performFullResize(ResizeEvent event) {
        var resizeContext = new WindowSizeContext(event.newWidth(), event.newHeight());

        // Step 1: Invalidate all component sizes
        if (rootComponent instanceof ResizableComponent resizable) {
            resizable.invalidateSize(resizeContext);
            resizable.onParentResize(event);
        }

        // Step 2: Reapply CSS with new dimensions
        applyCssToComponentTree(rootComponent, resizeContext);

        // Step 3: Mark root as needing layout
        if (rootComponent instanceof ResizableComponent resizable) {
            resizable.markNeedsLayout();
        }
    }

    private void applyCssToComponents() {
        MinecraftClient client = MinecraftClient.getInstance();
        var realContext = new WindowSizeContext(
                client.getWindow().getScaledWidth(),
                client.getWindow().getScaledHeight()
        );

        applyCssToComponentTree(rootComponent, realContext);
    }

    private void applyCssToComponentTree(EdmlComponent component, MinecraftRenderContext context) {
        for (EdssRule rule : cssRules) {
            if (matchesSelector(rule.selector(), component)) {
                for (var entry : rule.declarations().entrySet()) {
                    cssRegistry.applyCssRule(component, entry.getKey(), entry.getValue(), context);
                }
            }
        }

        if (component instanceof AttributeProcessor processor) {
            processor.processAttributes(cssRules, cssRegistry, context);
        }

        for (EdmlComponent child : component.getChildren()) {
            applyCssToComponentTree(child, context);
        }
    }

    private boolean matchesSelector(String selector, EdmlComponent component) {
        selector = selector.trim();

        if (selector.matches("^[a-zA-Z][a-zA-Z0-9-]*$")) {
            return component.getTagName().equals(selector);
        }

        if (selector.startsWith(".")) {
            String className = selector.substring(1);
            return hasClass(component, className);
        }

        if (selector.startsWith("#")) {
            String id = selector.substring(1);
            return hasId(component, id);
        }

        return false;
    }

    private boolean hasClass(EdmlComponent component, String className) {
        if (component instanceof AbstractEdmlComponent abstractComp) {
            return abstractComp.hasClass(className);
        }
        return false;
    }

    private boolean hasId(EdmlComponent component, String id) {
        if (component instanceof AbstractEdmlComponent abstractComp) {
            String componentId = abstractComp.getAttribute(TagAttribute.ID.getProperty(), "");
            return componentId.equals(id);
        }
        return false;
    }

    @Override
    protected void init() {
        super.init();
        ResizeManager.getInstance().updateDimensions(this.width, this.height);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        super.resize(client, width, height);
        ResizeManager.getInstance().updateDimensions(width, height);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (rootComponent != null) {
            FabricRenderContext renderContext = new FabricRenderContext(context, this.width, this.height);

            // Always layout if needed
            if (rootComponent instanceof ResizableComponent resizable && resizable.needsLayout()) {
                layoutEngine.layoutComponent(rootComponent, renderContext);
                resizable.clearNeedsLayout();
            }

            rootComponent.render(renderContext);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        HTMLMyScreen.LOGGER.info("=== SCREEN mouseClicked: ({}, {}) button={} ===", mouseX, mouseY, button);

        if (rootComponent instanceof ClickableComponent clickable) {
            boolean handled = clickable.handleClick(mouseX, mouseY, button);
            HTMLMyScreen.LOGGER.info("Root component handled: {}", handled);
            return handled;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (rootComponent instanceof DraggableComponent draggable) {
            if (draggable.handleDrag(mouseX, mouseY)) {
                return true;
            }
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (rootComponent instanceof DraggableComponent draggable) {
            draggable.handleRelease();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean handleVirtualListDrag(EdmlComponent component, double mouseX, double mouseY) {
        if (component instanceof VirtualListComponent virtualList) {
            if (virtualList.handleDrag(mouseX, mouseY)) {
                return true;
            }
        }

        for (EdmlComponent child : component.getChildren()) {
            if (handleVirtualListDrag(child, mouseX, mouseY)) {
                return true;
            }
        }

        return false;
    }

    private void releaseVirtualLists(EdmlComponent component) {
        if (component instanceof VirtualListComponent virtualList) {
            virtualList.handleRelease();
        }

        for (EdmlComponent child : component.getChildren()) {
            releaseVirtualLists(child);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        boolean handled = ScrollManager.getInstance().handleScrollEvent(mouseX, mouseY, horizontalAmount, verticalAmount);
        if (handled) {
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        ResizeManager.getInstance().removeListener(this);
        ScrollManager.getInstance().dispose();
        bindingEngine.dispose();
        super.close();
    }

    public BindingContext getBindingContext() {
        return bindingContext;
    }

    public DataBindingEngine getBindingEngine() {
        return bindingEngine;
    }

    public EdmlComponent getRootComponent() {
        return rootComponent;
    }

    private record WindowSizeContext(int width, int height) implements MinecraftRenderContext {}
}