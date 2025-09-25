package com.edgn.prog.layout.engine;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.layout.LayoutEngine;
import com.edgn.prog.layout.box.BoxModel;
import com.edgn.prog.layout.box.BoxModelComponent;
import com.edgn.prog.layout.spacing.Margin;
import com.edgn.prog.layout.spacing.Padding;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.List;

public final class FlexboxLayoutEngine implements LayoutEngine {
    
    public void layoutVerticalStack(List<BoxModelComponent> components, int containerWidth, int containerHeight) {
        int currentY = 0;
        
        for (BoxModelComponent component : components) {
            BoxModel box = component.getBoxModel();
            Margin margin = box.margin();
            
            currentY += margin.top();
            
            int componentWidth = Math.min(box.width(), containerWidth);
            int componentHeight = box.height();
            
            positionComponent(component, 0, currentY, componentWidth, componentHeight);
            
            currentY += componentHeight + margin.bottom();
        }
    }
    
    private void positionComponent(BoxModelComponent component, int x, int y, int width, int height) {
        BoxModel box = component.getBoxModel();
        Padding padding = box.padding();
        
        int contentX = x + padding.left();
        int contentY = y + padding.top();
        int contentWidth = width - padding.horizontal();
        int contentHeight = height - padding.vertical();
        
        if (component instanceof CssAwareComponent cssComp) {
            cssComp.setCalculatedBounds(x, y, width, height);
        }
    }

    @Override
    public void layoutComponent(EdgnComponent rootComponent, MinecraftRenderContext context) {

    }
}