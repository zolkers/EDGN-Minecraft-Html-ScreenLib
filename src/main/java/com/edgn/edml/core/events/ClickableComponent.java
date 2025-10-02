package com.edgn.edml.core.events;

public interface ClickableComponent {
    boolean handleClick(double mouseX, double mouseY, int button);
    boolean isPointInBounds(double x, double y);
}