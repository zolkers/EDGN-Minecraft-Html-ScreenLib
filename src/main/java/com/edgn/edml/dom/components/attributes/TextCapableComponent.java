package com.edgn.edml.dom.components.attributes;

public interface TextCapableComponent {
    void setTextColor(int color);
    int getTextColor();
    default boolean hasTextContent() {
        return true;
    }
}