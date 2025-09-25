package com.edgn.edml.layout.spacing;

public record Padding(int top, int right, int bottom, int left) {

    public Padding(int vertical, int horizontal) {
        this(vertical, horizontal, vertical, horizontal);
    }

    public Padding(int all) {
        this(all, all, all, all);
    }

    public static Padding none() {
        return new Padding(0);
    }

    public static Padding of(int value) {
        return new Padding(value);
    }

    public int vertical() {
        return top + bottom;
    }

    public int horizontal() {
        return left + right;
    }
}