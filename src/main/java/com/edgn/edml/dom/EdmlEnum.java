package com.edgn.edml.dom;

public enum EdmlEnum {
    BODY("body"),
    DIV("div"),
    HEADER("header"),
    MAIN("main"),
    FOOTER("footer"),
    SECTION("section"),
    H1("h1"),
    H2("h2"),
    H3("h3"),
    P("p"),
    SPAN("span"),
    INPUT("input"),
    BUTTON("button"),
    FORM("form"),
    IMG("img"),
    VIDEO("video"),
    TEXT("text"),
    SCROLLBAR("scrollbar"),
    VIRTUAL_LIST("virtual-list");

    private final String tagName;

    EdmlEnum(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public String toString() {
        return tagName;
    }
}
