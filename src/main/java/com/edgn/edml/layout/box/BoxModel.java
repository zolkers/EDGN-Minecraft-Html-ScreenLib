package com.edgn.edml.layout.box;

import com.edgn.edml.layout.spacing.Margin;
import com.edgn.edml.layout.spacing.Padding;

public final class BoxModel {
    private final Margin margin;
    private final Padding padding;
    private final int width;
    private final int height;
    
    public BoxModel(int width, int height, Padding padding, Margin margin) {
        this.width = width;
        this.height = height;
        this.padding = padding;
        this.margin = margin;
    }
    
    public BoxModel(int width, int height) {
        this(width, height, Padding.none(), Margin.none());
    }
    
    public Margin margin() { return margin; }
    public Padding padding() { return padding; }
    public int width() { return width; }
    public int height() { return height; }
    
    public int contentWidth() {
        return width - padding.horizontal();
    }
    
    public int contentHeight() {
        return height - padding.vertical();
    }
    
    public int totalWidth() {
        return width + margin.horizontal();
    }
    
    public int totalHeight() {
        return height + margin.vertical();
    }
    
    public BoxModel withMargin(Margin newMargin) {
        return new BoxModel(width, height, padding, newMargin);
    }
    
    public BoxModel withPadding(Padding newPadding) {
        return new BoxModel(width, height, newPadding, margin);
    }
}