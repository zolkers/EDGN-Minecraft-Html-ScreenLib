package com.edgn.edml.events.resize;

public record ResizeEvent(
    int oldWidth,
    int oldHeight,
    int newWidth,
    int newHeight
) {
    public boolean hasWidthChanged() {
        return oldWidth != newWidth;
    }
    
    public boolean hasHeightChanged() {
        return oldHeight != newHeight;
    }
    
    public boolean hasChanged() {
        return hasWidthChanged() || hasHeightChanged();
    }
    
    public int widthDelta() {
        return newWidth - oldWidth;
    }
    
    public int heightDelta() {
        return newHeight - oldHeight;
    }
}