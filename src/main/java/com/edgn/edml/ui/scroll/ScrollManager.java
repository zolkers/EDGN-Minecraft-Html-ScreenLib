package com.edgn.edml.ui.scroll;

import com.edgn.HTMLMyScreen;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScrollManager {
    private static final ScrollManager INSTANCE = new ScrollManager();
    
    private final List<ScrollableComponent> scrollableComponents = new CopyOnWriteArrayList<>();
    private ScrollableComponent globalScrollComponent;
    
    public static ScrollManager getInstance() {
        return INSTANCE;
    }
    
    public void registerScrollable(ScrollableComponent component) {
        scrollableComponents.add(component);
        HTMLMyScreen.LOGGER.debug("Registered scrollable component: {}", component.getClass().getSimpleName());
    }
    
    public void unregisterScrollable(ScrollableComponent component) {
        scrollableComponents.remove(component);
    }
    
    public void setGlobalScrollComponent(ScrollableComponent component) {
        this.globalScrollComponent = component;
        HTMLMyScreen.LOGGER.debug("Set global scroll component: {}", component.getClass().getSimpleName());
    }
    
    public boolean handleScrollEvent(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        for (ScrollableComponent component : scrollableComponents) {
            if (component.isPointInBounds(mouseX, mouseY) && component.canScroll()) {
                boolean handled = component.handleScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
                if (handled) {
                    HTMLMyScreen.LOGGER.debug("Scroll handled by: {}", component.getClass().getSimpleName());
                    return true;
                }
            }
        }
        
        if (globalScrollComponent != null && globalScrollComponent.canScroll()) {
            return globalScrollComponent.handleScroll(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        
        return false;
    }
    
    public void dispose() {
        scrollableComponents.clear();
        globalScrollComponent = null;
    }
}