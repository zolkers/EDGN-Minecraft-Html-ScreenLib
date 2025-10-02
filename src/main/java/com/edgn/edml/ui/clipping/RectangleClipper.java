package com.edgn.edml.ui.clipping;
public final class RectangleClipper {

    private RectangleClipper() {
        // Utility class
    }

    /**
     * Clips a rectangle to fit within a clipping region
     * 
     * @param region The clipping region
     * @param rectX Rectangle X position
     * @param rectY Rectangle Y position
     * @param rectWidth Rectangle width
     * @param rectHeight Rectangle height
     * @return A ClippedRectangle representing the visible portion
     */
    public static ClippedRectangle clip(ClippingRegion region, int rectX, int rectY, int rectWidth, int rectHeight) {
        if (!region.intersects(rectX, rectY, rectWidth, rectHeight)) {
            return ClippedRectangle.hidden();
        }
        int visibleX = Math.max(region.getX(), rectX);
        int visibleY = Math.max(region.getY(), rectY);
        int visibleRight = Math.min(region.getRight(), rectX + rectWidth);
        int visibleBottom = Math.min(region.getBottom(), rectY + rectHeight);

        int visibleWidth = visibleRight - visibleX;
        int visibleHeight = visibleBottom - visibleY;

        if (visibleWidth <= 0 || visibleHeight <= 0) {
            return ClippedRectangle.hidden();
        }

        return ClippedRectangle.visible(visibleX, visibleY, visibleWidth, visibleHeight);
    }

    /**
     * Clips a rectangle vertically only (useful for scrollable lists)
     */
    public static ClippedRectangle clipVertical(ClippingRegion region, int visibleX, int rectY, int rectWidth, int rectHeight) {
        if (rectY + rectHeight <= region.getY() || rectY >= region.getBottom()) {
            return ClippedRectangle.hidden();
        }

        int visibleY = Math.max(region.getY(), rectY);
        int visibleBottom = Math.min(region.getBottom(), rectY + rectHeight);
        int visibleHeight = visibleBottom - visibleY;

        if (visibleHeight <= 0) {
            return ClippedRectangle.hidden();
        }

        return ClippedRectangle.visible(visibleX, visibleY, rectWidth, visibleHeight);
    }

    /**
     * Clips a rectangle horizontally only
     */
    public static ClippedRectangle clipHorizontal(ClippingRegion region, int rectX, int rectY, int rectWidth, int rectHeight) {
        // Check if completely outside horizontally
        if (rectX + rectWidth <= region.getX() || rectX >= region.getRight()) {
            return ClippedRectangle.hidden();
        }
        int visibleX = Math.max(region.getX(), rectX);
        int visibleRight = Math.min(region.getRight(), rectX + rectWidth);
        int visibleWidth = visibleRight - visibleX;

        if (visibleWidth <= 0) {
            return ClippedRectangle.hidden();
        }

        return ClippedRectangle.visible(visibleX, rectY, visibleWidth, rectHeight);
    }
}