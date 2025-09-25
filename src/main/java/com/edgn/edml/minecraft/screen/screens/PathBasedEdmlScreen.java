package com.edgn.edml.minecraft.screen.screens;

import com.edgn.edml.component.attribute.AttributeProcessor;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edss.IEdssRegistry;
import com.edgn.edml.component.edss.EdssRule;
import com.edgn.edml.component.edss.EdssRegistry;
import com.edgn.edml.component.html.AbstractEdmlComponent;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.exceptions.EdmlParsingException;
import com.edgn.edml.exceptions.EdssParsingException;
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

public final class PathBasedEdmlScreen extends EdmlScreen {
    private final EdmlComponent rootComponent;
    private final List<EdssRule> cssRules;
    private final IEdssRegistry cssRegistry;
    private final ILayoutEngine layoutEngine;

    public PathBasedEdmlScreen(Text title, String htmlPath, String cssPath) {
        super(title);
        this.cssRegistry = EdssRegistry.getInstance();

        IComponentSizeCalculator sizeCalculator = new ComponentSizeCalculator();
        this.layoutEngine = new LayoutEngine(sizeCalculator);

        try {
            String htmlContent = EdmlResourceLoader.loadHtmlWithPath(htmlPath);
            String cssContent = EdmlResourceLoader.loadCssWithPath(cssPath);

            EdmlParser htmlParser = new DefaultEdmlParser();
            EdssParser cssParser = new SimpleEdssParser();

            this.rootComponent = htmlParser.parseDocument(htmlContent);
            this.cssRules = cssParser.parse(cssContent);

            applyCssToComponents();

        } catch (EdmlParsingException | EdssParsingException e) {
            throw new RuntimeException("Failed to load EDGN screen: " + htmlPath + "/" + cssPath + " - " + e.getMessage(), e);
        }
    }

    private void applyCssToComponents() {
        MinecraftClient client = MinecraftClient.getInstance();
        var realContext = new WindowSizeContext(client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());

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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        if (rootComponent != null) {
            FabricRenderContext renderContext = new FabricRenderContext(context, this.width, this.height);

            layoutEngine.layoutComponent(rootComponent, renderContext);
            rootComponent.render(renderContext);
        }
    }

    private record WindowSizeContext(int width, int height) implements MinecraftRenderContext {}
}