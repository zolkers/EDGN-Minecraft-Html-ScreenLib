package com.edgn.edml.component.attribute;

public enum TagAttribute {
    // Core HTML attributes
    CLASS("class"),
    ID("id"),
    WIDTH("width"),
    HEIGHT("height"),
    SRC("src"),
    ALT("alt"),
    TITLE("title"),
    SUBTITLE("subtitle"),
    STYLE("style"),
    ROLE("role"),

    // Event handlers
    LOAD("load"),
    UNLOAD("unload"),
    CLICK("onclick"),
    HOVER("onhover"),

    // Data binding attributes
    DATA_BIND("data-bind"),
    DATA_VISIBLE("data-visible"),
    DATA_CLASS("data-class"),
    DATA_STYLE("data-style"),
    DATA_CLICK("data-click"),
    DATA_CHANGE("data-change"),
    DATA_TEXT("data-text"),
    DATA_TOOLTIP("data-tooltip"),
    DATA_THEME("data-theme"),
    DATA_ANIMATION("data-animation"),
    DATA_DELAY("data-delay"),
    DATA_LAYOUT("data-layout"),
    DATA_RESPONSIVE("data-responsive"),
    DATA_FULLSCREEN("data-fullscreen"),
    DATA_BEGIN("data-"),

    // Virtual list attributes
    DATA_FOR("data-for"),
    DATA_LIST("data-list"),
    DATA_TEMPLATE("data-template"),
    DATA_ITEM_HEIGHT("data-item-height"),
    DATA_ITEM_VAR("data-item-var"),
    DATA_LIST_PATH("data-list-path"),

    // Scrollbar attributes
    DATA_ORIENTATION("data-orientation"),
    DATA_TRACK_COLOR("data-track-color"),
    DATA_THUMB_COLOR("data-thumb-color"),
    DATA_THUMB_HOVER_COLOR("data-thumb-hover-color"),
    DATA_SCROLLBAR_WIDTH("data-scrollbar-width"),
    DATA_MIN_THUMB_SIZE("data-min-thumb-size"),
    DATA_SCROLLABLE("data-scrollable"),

    // Layout attributes
    DATA_OVERFLOW("data-overflow"),
    DATA_SCROLL_X("data-scroll-x"),
    DATA_SCROLL_Y("data-scroll-y"),

    // CSS class names
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
    SCROLLABLE("scrollable"),

    // Theme classes
    DARK_THEME("dark-theme"),
    LIGHT_THEME("light-theme"),
    PRIMARY("primary"),
    SUCCESS("success"),
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),

    // State classes
    SECTION_TITLE("section-title"),
    STATUS_OK("status-ok"),
    STATUS_ERROR("status-error"),
    STATUS_WARNING("status-warning"),

    // ARIA roles
    ROLE_BANNER("banner"),
    ROLE_NAVIGATION("navigation"),
    ROLE_MAIN("main"),
    ROLE_COMPLEMENTARY("complementary"),
    ROLE_CONTENTINFO("contentinfo"),
    ROLE_LIST("list"),
    ROLE_LISTITEM("listitem"),

    // Theme values
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

    public static TagAttribute fromString(String value) {
        for (TagAttribute attr : values()) {
            if (attr.property.equals(value)) {
                return attr;
            }
        }
        return null;
    }

    public boolean matches(String value) {
        return property.equals(value);
    }
}