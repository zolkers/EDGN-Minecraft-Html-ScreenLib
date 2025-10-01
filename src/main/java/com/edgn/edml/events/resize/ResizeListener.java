package com.edgn.edml.events.resize;

/**
 * Functional interface for handling resize events
 */
@FunctionalInterface
public interface ResizeListener {
    /**
     * Called when the window is resized
     * @param event The resize event containing old and new dimensions
     */
    void onResize(ResizeEvent event);
}