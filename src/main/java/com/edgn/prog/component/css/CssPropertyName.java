package com.edgn.prog.component.css;

public enum CssPropertyName {
    BACKGROUND_COLOR("background-color"),
    COLOR("color"),
    WIDTH("width"),
    HEIGHT("height"),
    PADDING("padding"),
    MARGIN("margin"),
    FONT_SIZE("font-size"),
    FONT_WEIGHT("font-weight"),
    DISPLAY("display"),
    POSITION("position");
    
    private final String propertyName;
    
    CssPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
    
    public String getPropertyName() {
        return propertyName;
    }
    
    @Override
    public String toString() {
        return propertyName;
    }
}