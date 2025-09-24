package com.edgn.prog.component;

public interface PaddedComponent {
    void setPadding(int top, int right, int bottom, int left);
    int[] getPadding();
}