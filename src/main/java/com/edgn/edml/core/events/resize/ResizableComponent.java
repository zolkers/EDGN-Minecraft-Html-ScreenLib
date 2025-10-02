package com.edgn.edml.core.events.resize;

import com.edgn.edml.core.rendering.MinecraftRenderContext;

/**
 * Interface for components that can respond to resize events
 */
public interface ResizableComponent {
    
    /**
     * Called when the component needs to recalculate its size
     * @param context The render context with new dimensions
     */
    void invalidateSize(MinecraftRenderContext context);
    
    /**
     * Called when a parent resize occurs
     * @param event The resize event
     */
    void onParentResize(ResizeEvent event);
    
    /**
     * Returns true if the component needs layout recalculation
     */
    boolean needsLayout();
    
    /**
     * Marks the component as needing layout
     */
    void markNeedsLayout();
    
    /**
     * Clears the needs layout flag
     */
    void clearNeedsLayout();
}