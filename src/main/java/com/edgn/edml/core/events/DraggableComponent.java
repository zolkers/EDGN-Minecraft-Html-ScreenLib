package com.edgn.edml.core.events;

public interface DraggableComponent {
    boolean handleDrag(double mouseX, double mouseY);
    void handleRelease();
}