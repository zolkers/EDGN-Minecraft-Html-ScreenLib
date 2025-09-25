package com.edgn.edml.component;

public interface TextCapableComponent {
    void setTextColor(int color);
    int getTextColor();
    default boolean hasTextContent() {
        return true;
    }
}