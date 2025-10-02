package com.edgn.edml.ui.virtual;

@SuppressWarnings("unused")
public record VirtualBounds(int x, int y, int width, int height) {
    
    public boolean intersects(VirtualBounds other) {
        return !(x >= other.x + other.width || 
                x + width <= other.x || 
                y >= other.y + other.height || 
                y + height <= other.y);
    }
    
    public boolean contains(int pointX, int pointY) {
        return pointX >= x && pointX < x + width && 
               pointY >= y && pointY < y + height;
    }
    
    public boolean isInViewport(int viewportWidth, int viewportHeight) {
        return intersects(new VirtualBounds(0, 0, viewportWidth, viewportHeight));
    }
}