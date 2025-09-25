package com.edgn.prog.minecraft.screen.screens;

import com.edgn.prog.component.css.CssRegistry;
import com.edgn.prog.component.css.CssRule;
import com.edgn.prog.component.css.EdgnCssRegistry;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.layout.ComponentSizeCalculator;
import com.edgn.prog.layout.HtmlLikeComponentSizeCalculator;
import com.edgn.prog.layout.HtmlLikeLayoutEngine;
import com.edgn.prog.layout.LayoutEngine;
import com.edgn.prog.minecraft.context.FabricRenderContext;
import com.edgn.prog.minecraft.screen.EdgnScreen;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class InlineEdgnScreen extends EdgnScreen {
    private final EdgnComponent rootComponent;
    private final List<CssRule> cssRules;
    private final CssRegistry cssRegistry;
    private final LayoutEngine layoutEngine;

    public InlineEdgnScreen(Text title, EdgnComponent rootComponent, List<CssRule> cssRules) {
        super(title);
        this.rootComponent = rootComponent;
        this.cssRules = cssRules;
        this.cssRegistry = EdgnCssRegistry.getInstance();

        ComponentSizeCalculator sizeCalculator = new HtmlLikeComponentSizeCalculator();
        this.layoutEngine = new HtmlLikeLayoutEngine(sizeCalculator);

        applyCssToComponents();
    }

    private void applyCssToComponents() {
        for (CssRule rule : cssRules) {
            List<EdgnComponent> matchingComponents = findMatchingComponents(rule.selector());

            for (EdgnComponent component : matchingComponents) {
                for (Map.Entry<String, String> declaration : rule.declarations().entrySet()) {
                    cssRegistry.applyCssRule(component, declaration.getKey(), declaration.getValue(), null);
                }
            }
        }
    }

    private List<EdgnComponent> findMatchingComponents(String selector) {
        List<EdgnComponent> matches = new ArrayList<>();
        findComponentsByTag(rootComponent, selector, matches);
        return matches;
    }

    private void findComponentsByTag(EdgnComponent component, String tagName, List<EdgnComponent> results) {
        if (component.getTagName().equals(tagName)) {
            results.add(component);
        }

        for (EdgnComponent child : component.getChildren()) {
            findComponentsByTag(child, tagName, results);
        }
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
}