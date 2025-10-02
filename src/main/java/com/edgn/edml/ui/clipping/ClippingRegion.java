package com.edgn.edml.ui.clipping;
public final class ClippingRegion {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public ClippingRegion(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getBottom() {
        return y + height;
    }

    public int getRight() {
        return x + width;
    }
    public boolean contains(int pointX, int pointY) {
        return pointX >= x && pointX < x + width &&
               pointY >= y && pointY < y + height;
    }
    public boolean intersects(int rectX, int rectY, int rectWidth, int rectHeight) {
        return !(rectX >= x + width || 
                 rectX + rectWidth <= x || 
                 rectY >= y + height || 
                 rectY + rectHeight <= y);
    }
    public boolean contains(int rectX, int rectY, int rectWidth, int rectHeight) {
        return rectX >= x && 
               rectY >= y && 
               rectX + rectWidth <= x + width && 
               rectY + rectHeight <= y + height;
    }
}