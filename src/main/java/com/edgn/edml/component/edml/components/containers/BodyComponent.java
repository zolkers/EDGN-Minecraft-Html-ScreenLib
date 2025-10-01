package com.edgn.edml.component.edml.components.containers;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.ClickableComponent;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.EdmlEnum;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.component.edml.components.scroll.ScrollManager;
import com.edgn.edml.component.edml.components.scroll.ScrollableComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

public final class BodyComponent extends EdssAwareComponent implements ClickableComponent, ScrollableComponent {

    private static final Set<String> BODY_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.LOAD.getProperty(),
            TagAttribute.UNLOAD.getProperty(), TagAttribute.DATA_LAYOUT.getProperty(),
            TagAttribute.DATA_RESPONSIVE.getProperty(), TagAttribute.DATA_FULLSCREEN.getProperty()
    );

    private String layout = "flow";
    private boolean responsive = true;
    private boolean fullscreen = false;
    private int scrollOffset = 0;
    private int contentHeight = 0;
    private int viewportHeight = 0;

    public BodyComponent() {
        super(EdmlEnum.BODY.getTagName(), BODY_ATTRIBUTES);
        ScrollManager.getInstance().setGlobalScrollComponent(this);
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
        // Logic pour exécuter les scripts onload
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

        // Apply scroll offset
        if (scrollOffset > 0) {
            context.pushTransform(0, -scrollOffset, width, height + scrollOffset);
        }

        // Render scrollbar if needed
        if (canScroll()) {
            renderScrollbar(context, x, y, width, height);
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
        int scrollbarWidth = 8;
        int scrollbarX = x + width - scrollbarWidth;

        context.drawRect(scrollbarX, y, scrollbarWidth, height, ColorUtils.parseColor("#e0e0e0"));

        if (contentHeight > viewportHeight) {
            int thumbHeight = Math.max(20, (viewportHeight * viewportHeight) / contentHeight);
            int thumbY = y + (scrollOffset * (height - thumbHeight)) / getMaxScrollOffset();

            context.drawRect(scrollbarX + 1, thumbY, scrollbarWidth - 2, thumbHeight, ColorUtils.parseColor("#888888"));
        }
    }

    // ScrollableComponent implementation
    @Override
    public boolean handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!canScroll()) return false;

        int scrollDelta = (int) (-verticalAmount * 50);
        int newOffset = Math.max(0, Math.min(getMaxScrollOffset(), scrollOffset + scrollDelta));

        if (newOffset != scrollOffset) {
            scrollOffset = newOffset;
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
        if (!isPointInBounds(mouseX, mouseY)) {
            return false;
        }

        for (EdmlComponent child : children) {
            if (child instanceof ClickableComponent clickable) {
                if (clickable.handleClick(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return true;
    }

    public String getLayout() { return layout; }
    public boolean isResponsive() { return responsive; }
    public boolean isFullscreen() { return fullscreen; }

    public void onLoad() {
        String onLoadScript = getAttribute(TagAttribute.LOAD.getProperty(), "");
        if (!onLoadScript.isEmpty()) {
            executeLoadEvent(onLoadScript);
        }
    }

    public void onUnload() {
        String onUnloadScript = getAttribute(TagAttribute.UNLOAD.getProperty(), "");
        if (!onUnloadScript.isEmpty()) {
            // Logic pour onUnload
        }
    }

    @Override
    protected void dispose() {
        ScrollManager.getInstance().unregisterScrollable(this);
        super.dispose();
    }
}