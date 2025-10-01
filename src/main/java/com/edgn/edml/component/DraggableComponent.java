package com.edgn.edml.component;

public interface DraggableComponent {
    boolean handleDrag(double mouseX, double mouseY);
    void handleRelease();
}