package com.edgn.prog.component.attribute;

public enum TagAttribute {
    CLASS("class"),
    ID("id"),
    TITLE("title"),
    SUBTITLE("subtitle"),
    STYLE("style"),
    LOAD("load"),
    UNLOAD("unload"),
    ROLE("role"),
    DATA("data-*"),
    DATA_TEXT("data-text"),
    DATA_BEGIN("data-")
    ;

    private final String property;
    TagAttribute(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }
}
