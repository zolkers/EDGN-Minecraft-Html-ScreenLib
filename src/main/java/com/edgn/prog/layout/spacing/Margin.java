package com.edgn.prog.layout.spacing;

public final class Margin {
    private final int top, right, bottom, left;
    
    public Margin(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }
    
    public Margin(int vertical, int horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }
    
    public Margin(int all) {
        this(all, all, all, all);
    }
    
    public static Margin none() { return new Margin(0); }
    public static Margin of(int value) { return new Margin(value); }
    
    public int top() { return top; }
    public int right() { return right; }
    public int bottom() { return bottom; }
    public int left() { return left; }
    
    public int vertical() { return top + bottom; }
    public int horizontal() { return left + right; }
}