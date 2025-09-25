package com.edgn.prog.minecraft.screen.screens;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.exceptions.EdgnParsingException;
import com.edgn.prog.layout.ComponentSizeCalculator;
import com.edgn.prog.layout.HtmlLikeComponentSizeCalculator;
import com.edgn.prog.layout.HtmlLikeLayoutEngine;
import com.edgn.prog.layout.LayoutEngine;
import com.edgn.prog.minecraft.context.FabricRenderContext;
import com.edgn.prog.minecraft.screen.EdgnScreen;
import com.edgn.prog.parser.DefaultEdgnParser;
import com.edgn.prog.parser.EdgnParser;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class SimpleEdgnScreen extends EdgnScreen {
    private final EdgnComponent rootComponent;
    private final LayoutEngine layoutEngine;

    public SimpleEdgnScreen(Text title, EdgnComponent rootComponent) {
        super(title);
        this.rootComponent = rootComponent;

        ComponentSizeCalculator sizeCalculator = new HtmlLikeComponentSizeCalculator();
        this.layoutEngine = new HtmlLikeLayoutEngine(sizeCalculator);
    }

    public SimpleEdgnScreen(Text title, String htmlResourcePath) {
        super(title);
        try {
            EdgnParser parser = new DefaultEdgnParser();
            this.rootComponent = parser.parseResource(htmlResourcePath);

            ComponentSizeCalculator sizeCalculator = new HtmlLikeComponentSizeCalculator();
            this.layoutEngine = new HtmlLikeLayoutEngine(sizeCalculator);
        } catch (EdgnParsingException e) {
            throw new RuntimeException("Failed to load HTML screen: " + htmlResourcePath, e);
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