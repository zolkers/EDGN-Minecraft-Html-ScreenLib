package com.edgn.prog.minecraft.screen.screens;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.exceptions.EdgnParsingException;
import com.edgn.prog.minecraft.context.FabricRenderContext;
import com.edgn.prog.minecraft.screen.EdgnScreen;
import com.edgn.prog.parser.DefaultEdgnParser;
import com.edgn.prog.parser.EdgnParser;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public final class SimpleEdgnScreen extends EdgnScreen {
    private final EdgnComponent rootComponent;

    public SimpleEdgnScreen(Text title, EdgnComponent rootComponent) {
        super(title);
        this.rootComponent = rootComponent;
    }
    
    public SimpleEdgnScreen(Text title, String htmlResourcePath) {
        super(title);
        try {
            EdgnParser parser = new DefaultEdgnParser();
            this.rootComponent = parser.parseResource(htmlResourcePath);
        } catch (EdgnParsingException e) {
            throw new RuntimeException("Failed to load HTML screen: " + htmlResourcePath, e);
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        FabricRenderContext renderContext = new FabricRenderContext(context, this.width, this.height);
        
        if (rootComponent != null) {
            rootComponent.render(renderContext);
        }
    }
}