package com.edgn.edml.component.edml.components.containers;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.ClickableComponent;
import com.edgn.edml.component.DraggableComponent;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.EdmlEnum;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.component.edml.components.scroll.ScrollManager;
import com.edgn.edml.component.edml.components.scroll.ScrollableComponent;
import com.edgn.edml.component.edml.components.ui.ScrollbarComponent;
import com.edgn.edml.component.edml.components.ui.VirtualListComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

public final class BodyComponent extends EdssAwareComponent implements ClickableComponent, ScrollableComponent, DraggableComponent {

    private static final Set<String> BODY_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(),
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.LOAD.getProperty(),
            TagAttribute.UNLOAD.getProperty(),
            TagAttribute.DATA_LAYOUT.getProperty(),
            TagAttribute.DATA_RESPONSIVE.getProperty(),
            TagAttribute.DATA_FULLSCREEN.getProperty()
    );

    private String layout = "flow";
    private boolean responsive = true;
    private boolean fullscreen = false;
    private int scrollOffset = 0;
    private int contentHeight = 0;
    private int viewportHeight = 0;

    private ScrollbarComponent scrollbar;

    public BodyComponent() {
        super(EdmlEnum.BODY.getTagName(), BODY_ATTRIBUTES);
        ScrollManager.getInstance().setGlobalScrollComponent(this);
        initializeScrollbar();
    }

    private void initializeScrollbar() {
        scrollbar = new ScrollbarComponent();
        scrollbar.applyAttribute(TagAttribute.DATA_ORIENTATION.getProperty(), "vertical");
        scrollbar.applyAttribute(TagAttribute.DATA_SCROLLBAR_WIDTH.getProperty(), "10");
        scrollbar.applyAttribute(TagAttribute.DATA_TRACK_COLOR.getProperty(), "#e0e0e0");
        scrollbar.applyAttribute(TagAttribute.DATA_THUMB_COLOR.getProperty(), "#888888");
        scrollbar.applyAttribute(TagAttribute.DATA_THUMB_HOVER_COLOR.getProperty(), "#555555");
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String onLoad = getAttribute(TagAttribute.LOAD.getProperty(), "");
        layout = getAttribute(TagAttribute.DATA_LAYOUT.getProperty(), "flow");
        responsive = Boolean.parseBoolean(getAttribute(TagAttribute.DATA_RESPONSIVE.getProperty(), "true"));
        fullscreen = Boolean.parseBoolean(getAttribute(TagAttribute.DATA_FULLSCREEN.getProperty(), "false"));

        if (!onLoad.isEmpty()) {
            executeLoadEvent(onLoad);
        }

        this.viewportHeight = context.height();

        if (getWidth() == 0) {
            setWidth(context.width());
        }

        applyBodyTheme();
    }

    private void executeLoadEvent(String loadScript) {
        // Logic for onload scripts
    }

    private void applyBodyTheme() {
        if (getBackgroundColor() == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#1a1a1a"));
            } else if (hasClass(TagAttribute.LIGHT_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#f5f5f5"));
            } else {
                setBackgroundColor(ColorUtils.parseColor("#f5f5f5"));
            }
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        calculateContentHeight();
    }

    @Override
    protected void renderChildren(MinecraftRenderContext context) {
        // Apply scroll offset ONLY for children
        if (scrollOffset > 0) {
            context.pushTransform(0, -scrollOffset, getCalculatedWidth(), getCalculatedHeight() + scrollOffset);
        }

        // Render children with transform
        children.forEach(child -> child.render(context));

        if (scrollOffset > 0) {
            context.popTransform();
        }

        // Render scrollbar AFTER transform (so it stays fixed)
        if (canScroll()) {
            renderScrollbar(context, getCalculatedX(), getCalculatedY(), getCalculatedWidth(), getCalculatedHeight());
        }
    }

    private void calculateContentHeight() {
        contentHeight = 0;
        for (EdmlComponent child : children) {
            if (child instanceof EdssAwareComponent cssChild) {
                contentHeight += cssChild.getCalculatedHeight();
                contentHeight += cssChild.getMargin().vertical();
            }
        }
    }

    private void renderScrollbar(MinecraftRenderContext context, int x, int y, int width, int height) {
        int scrollbarWidth = scrollbar.getScrollbarWidth();
        int scrollbarX = x + width - scrollbarWidth;

        scrollbar.setCalculatedBounds(scrollbarX, y, scrollbarWidth, height);

        int maxOffset = getMaxScrollOffset();
        scrollbar.updateScrollState(scrollOffset, maxOffset, contentHeight, viewportHeight);

        scrollbar.render(context);
    }

    @Override
    public boolean handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!canScroll()) {
            return false;
        }

        int scrollDelta = (int) (-verticalAmount * 50);
        int newOffset = Math.max(0, Math.min(getMaxScrollOffset(), scrollOffset + scrollDelta));

        if (newOffset != scrollOffset) {
            scrollOffset = newOffset;
            if (scrollbar != null) {
                scrollbar.setScrollOffset(scrollOffset);
            }
            HTMLMyScreen.LOGGER.debug("Body scrolled to offset: {}", scrollOffset);
            return true;
        }

        return false;
    }

    @Override
    public boolean isPointInBounds(double x, double y) {
        return x >= getCalculatedX() && x < getCalculatedX() + getCalculatedWidth() &&
                y >= getCalculatedY() && y < getCalculatedY() + getCalculatedHeight();
    }

    @Override
    public int getScrollOffset() {
        return scrollOffset;
    }

    @Override
    public void setScrollOffset(int offset) {
        this.scrollOffset = Math.max(0, Math.min(getMaxScrollOffset(), offset));
        if (scrollbar != null) {
            scrollbar.setScrollOffset(this.scrollOffset);
        }
    }

    @Override
    public int getMaxScrollOffset() {
        return Math.max(0, contentHeight - viewportHeight);
    }

    @Override
    public boolean canScroll() {
        return contentHeight > viewportHeight;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        HTMLMyScreen.LOGGER.info("Body.handleClick: mouse=({}, {}), bounds=({}, {}, {}, {})",
                mouseX, mouseY, getCalculatedX(), getCalculatedY(),
                getCalculatedWidth(), getCalculatedHeight());

        if (!isPointInBounds(mouseX, mouseY)) {
            HTMLMyScreen.LOGGER.info("Body: Click outside bounds");
            return false;
        }

        if (canScroll() && scrollbar != null) {
            HTMLMyScreen.LOGGER.info("Body: Checking own scrollbar");
            if (scrollbar.handleClick(mouseX, mouseY, button)) {
                HTMLMyScreen.LOGGER.info("Body scrollbar clicked");
                return true;
            }
        }

        HTMLMyScreen.LOGGER.info("Body: Checking {} children", children.size());
        for (int i = 0; i < children.size(); i++) {
            EdmlComponent child = children.get(i);
            HTMLMyScreen.LOGGER.info("Body: Checking child {} - {}", i, child.getClass().getSimpleName());

            if (child instanceof ClickableComponent clickable) {
                boolean handled = clickable.handleClick(mouseX, mouseY, button);
                HTMLMyScreen.LOGGER.info("Body: Child {} returned {}", i, handled);
                if (handled) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean handleDrag(double mouseX, double mouseY) {
        if (scrollbar != null && scrollbar.isDragging()) {
            if (scrollbar.handleDrag(mouseX, mouseY)) {
                scrollOffset = scrollbar.getScrollOffset();
                return true;
            }
        }

        for (EdmlComponent child : children) {
            if (child instanceof DraggableComponent draggable) {
                if (draggable.handleDrag(mouseX, mouseY)) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public void handleRelease() {
        if (scrollbar != null) {
            scrollbar.handleRelease();
        }

        for (EdmlComponent child : children) {
            if (child instanceof DraggableComponent draggable) {
                draggable.handleRelease();
            }
        }
    }

    public String getLayout() {
        return layout;
    }

    public boolean isResponsive() {
        return responsive;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void onLoad() {
        String onLoadScript = getAttribute(TagAttribute.LOAD.getProperty(), "");
        if (!onLoadScript.isEmpty()) {
            executeLoadEvent(onLoadScript);
        }
    }

    public void onUnload() {
        String onUnloadScript = getAttribute(TagAttribute.UNLOAD.getProperty(), "");
        if (!onUnloadScript.isEmpty()) {
            // Logic for onUnload
        }
    }

    @Override
    protected void dispose() {
        ScrollManager.getInstance().unregisterScrollable(this);
        super.dispose();
    }
}