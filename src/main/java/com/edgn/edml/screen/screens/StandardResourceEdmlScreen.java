// ============================================================================
// FILE: src/main/java/com/edgn/edml/screen/screens/StandardResourceEdmlScreen.java
// ============================================================================

package com.edgn.edml.screen.screens;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.annotations.AwaitOverride;
import com.edgn.edml.core.events.resize.ResizableComponent;
import com.edgn.edml.dom.components.attributes.AttributeProcessor;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.core.component.AbstractEdmlComponent;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.ui.scroll.ScrollManager;
import com.edgn.edml.dom.styling.registry.EdssRegistry;
import com.edgn.edml.dom.styling.EdssRule;
import com.edgn.edml.dom.styling.registry.IEdssRegistry;
import com.edgn.edml.data.binding.BindingContext;
import com.edgn.edml.data.binding.DataBindingEngine;
import com.edgn.edml.core.events.resize.ResizeEvent;
import com.edgn.edml.core.events.resize.ResizeListener;
import com.edgn.edml.core.events.resize.ResizeManager;
import com.edgn.edml.exceptions.EdssParsingException;
import com.edgn.edml.exceptions.EdmlParsingException;
import com.edgn.edml.layout.sizing.IComponentSizeCalculator;
import com.edgn.edml.layout.sizing.ComponentSizeCalculator;
import com.edgn.edml.layout.LayoutEngine;
import com.edgn.edml.layout.ILayoutEngine;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.screen.EdmlScreen;
import com.edgn.edml.parser.document.DefaultEdmlParser;
import com.edgn.edml.parser.style.EdssParser;
import com.edgn.edml.parser.document.EdmlParser;
import com.edgn.edml.parser.style.SimpleEdssParser;
import com.edgn.edml.resources.EdmlResourceLoader;
import net.minecraft.client.MinecraftClient;
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

        if (rootComponent instanceof ResizableComponent resizable) {
            resizable.invalidateSize(resizeContext);
            resizable.onParentResize(event);
        }

        applyCssToComponentTree(rootComponent, resizeContext);

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

    @Override
    protected EdmlComponent getRootComponent() {
        return rootComponent;
    }

    @Override
    protected ILayoutEngine getLayoutEngine() {
        return layoutEngine;
    }

    private record WindowSizeContext(int width, int height) implements MinecraftRenderContext {}
}