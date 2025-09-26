package com.edgn.edml.component.edml.scroll;

public interface ScrollableComponent {
    boolean handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount);
    boolean isPointInBounds(double x, double y);
    int getScrollOffset();
    void setScrollOffset(int offset);
    int getMaxScrollOffset();
    boolean canScroll();
}