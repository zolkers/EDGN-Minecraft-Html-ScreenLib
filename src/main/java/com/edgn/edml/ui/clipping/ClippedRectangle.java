package com.edgn.edml.ui.clipping;
public final class ClippedRectangle {
    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final boolean completelyHidden;

    private ClippedRectangle(int x, int y, int width, int height, boolean completelyHidden) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.completelyHidden = completelyHidden;
    }
    public static ClippedRectangle visible(int x, int y, int width, int height) {
        return new ClippedRectangle(x, y, width, height, false);
    }
    public static ClippedRectangle hidden() {
        return new ClippedRectangle(0, 0, 0, 0, true);
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

    public boolean isCompletelyHidden() {
        return completelyHidden;
    }

    public boolean isPartiallyVisible() {
        return !completelyHidden && (width > 0 && height > 0);
    }

    public ClippingInfo getClippingInfo(int originalX, int originalY, int originalWidth, int originalHeight) {
        if (completelyHidden) {
            return new ClippingInfo(originalHeight, 0, 0, originalWidth);
        }

        int clippedTop = y - originalY;
        int clippedBottom = (originalY + originalHeight) - (y + height);
        int clippedLeft = x - originalX;
        int clippedRight = (originalX + originalWidth) - (x + width);

        return new ClippingInfo(
            Math.max(0, clippedTop),
            Math.max(0, clippedRight),
            Math.max(0, clippedBottom),
            Math.max(0, clippedLeft)
        );
    }
    public record ClippingInfo(int top, int right, int bottom, int left) {
        public boolean isClipped() {
            return top > 0 || right > 0 || bottom > 0 || left > 0;
        }

        public boolean isClippedTop() {
            return top > 0;
        }

        public boolean isClippedBottom() {
            return bottom > 0;
        }

        public boolean isClippedLeft() {
            return left > 0;
        }

        public boolean isClippedRight() {
            return right > 0;
        }
    }
}