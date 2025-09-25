package com.edgn.edml.component.attribute;

public enum TagAttribute {
    CLASS("class"),
    ID("id"),
    WIDTH("width"),
    HEIGHT("height"),
    SRC("src"),
    ALT("alt"),
    TITLE("title"),
    SUBTITLE("subtitle"),

    DATA_BIND("data-bind"),
    DATA_VISIBLE("data-visible"),
    DATA_CLASS("data-class"),
    DATA_STYLE("data-style"),
    DATA_CLICK("data-click"),
    DATA_CHANGE("data-change"),
    DATA_LIST("data-list"),
    DATA_TEMPLATE("data-template"),
    DATA_ITEM_HEIGHT("data-item-height"),
    STYLE("style"),
    ROLE("role"),
    LOAD("load"),
    UNLOAD("unload"),
    CLICK("onclick"),
    HOVER("onhover"),
    DATA("data-*"),
    DATA_TEXT("data-text"),
    DATA_TOOLTIP("data-tooltip"),
    DATA_THEME("data-theme"),
    DATA_ANIMATION("data-animation"),
    DATA_DELAY("data-delay"),
    DATA_LAYOUT("data-layout"),
    DATA_RESPONSIVE("data-responsive"),
    DATA_FULLSCREEN("data-fullscreen"),
    DATA_BEGIN("data-"),
    ANIMATED("animated"),
    BANNER("banner"),
    NAV("nav"),
    MAIN_CONTENT("main-content"),
    SIDEBAR("sidebar"),
    SITE_HEADER("site-header"),
    SITE_FOOTER("site-footer"),
    CUSTOM_HEADER("custom-header"),
    CUSTOM_FOOTER("custom-footer"),
    CUSTOM_MAIN("custom-main"),
    FULLSCREEN("fullscreen"),
    DARK_THEME("dark-theme"),
    LIGHT_THEME("light-theme"),
    PRIMARY("primary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    SECTION_TITLE("section-title"),
    STATUS_OK("status-ok"),
    STATUS_ERROR("status-error"),
    STATUS_WARNING("status-warning"),
    ROLE_BANNER("banner"),
    ROLE_NAVIGATION("navigation"),
    ROLE_MAIN("main"),
    ROLE_COMPLEMENTARY("complementary"),
    ROLE_CONTENTINFO("contentinfo"),
    THEME_DARK("dark"),
    THEME_LIGHT("light"),
    THEME_PRIMARY("primary"),
    THEME_SUCCESS("success"),
    THEME_DANGER("danger"),
    THEME_DEFAULT("default");

    private final String property;

    TagAttribute(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    @Override
    public String toString() {
        return property;
    }
}