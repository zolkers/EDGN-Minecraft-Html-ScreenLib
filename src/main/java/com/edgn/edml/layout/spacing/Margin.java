package com.edgn.edml.layout.spacing;

public record Margin(int top, int right, int bottom, int left) {

    public Margin(int vertical, int horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public Margin(int all) {
        this(all, all, all, all);
    }

    public static Margin none() {
        return new Margin(0);
    }

    public static Margin of(int value) {
        return new Margin(value);
    }

    public int vertical() {
        return top + bottom;
    }

    public int horizontal() {
        return left + right;
    }
}