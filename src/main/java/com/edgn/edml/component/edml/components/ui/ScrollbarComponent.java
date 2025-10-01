package com.edgn.edml.component.edml.components.ui;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.ClickableComponent;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

/**
 * Scrollbar component - fully functional with visual rendering
 */
public final class ScrollbarComponent extends EdssAwareComponent implements ClickableComponent {

    private static final Set<String> SCROLLBAR_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(),
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.DATA_ORIENTATION.getProperty(),
            TagAttribute.DATA_TRACK_COLOR.getProperty(),
            TagAttribute.DATA_THUMB_COLOR.getProperty(),
            TagAttribute.DATA_THUMB_HOVER_COLOR.getProperty(),
            TagAttribute.DATA_SCROLLBAR_WIDTH.getProperty(),
            TagAttribute.DATA_MIN_THUMB_SIZE.getProperty()
    );

    public enum Orientation {
        VERTICAL, HORIZONTAL
    }

    private Orientation orientation = Orientation.VERTICAL;
    private int trackColor = ColorUtils.parseColor("#e0e0e0");
    private int thumbColor = ColorUtils.parseColor("#888888");
    private int thumbHoverColor = ColorUtils.parseColor("#666666");
    private int scrollbarWidth = 8;
    private int minThumbSize = 20;

    private int scrollOffset = 0;
    private int maxScrollOffset = 0;
    private int contentSize = 0;
    private int viewportSize = 0;

    private boolean isHovered = false;
    private boolean isDragging = false;
    private double dragStartPos = 0;
    private int dragStartOffset = 0;

    public ScrollbarComponent() {
        super("scrollbar", SCROLLBAR_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String orientStr = getAttribute(TagAttribute.DATA_ORIENTATION.getProperty(), "vertical");
        orientation = orientStr.equalsIgnoreCase("horizontal") ? Orientation.HORIZONTAL : Orientation.VERTICAL;

        trackColor = parseColorAttribute(TagAttribute.DATA_TRACK_COLOR, "#e0e0e0");
        thumbColor = parseColorAttribute(TagAttribute.DATA_THUMB_COLOR, "#888888");
        thumbHoverColor = parseColorAttribute(TagAttribute.DATA_THUMB_HOVER_COLOR, "#666666");

        scrollbarWidth = parseIntAttribute(TagAttribute.DATA_SCROLLBAR_WIDTH, 8);
        minThumbSize = parseIntAttribute(TagAttribute.DATA_MIN_THUMB_SIZE, 20);
    }

    private int parseColorAttribute(TagAttribute attr, String defaultValue) {
        String colorStr = getAttribute(attr.getProperty(), "");
        return colorStr.isEmpty() ? ColorUtils.parseColor(defaultValue) : ColorUtils.parseColor(colorStr);
    }

    private int parseIntAttribute(TagAttribute attr, int defaultValue) {
        String valueStr = getAttribute(attr.getProperty(), "");
        if (valueStr.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        // CRITICAL: Don't check shouldRender() here, always render if parent wants us visible
        if (maxScrollOffset <= 0) {
            return; // Nothing to scroll
        }

        if (orientation == Orientation.VERTICAL) {
            renderVerticalScrollbar(context, x, y, width, height);
        } else {
            renderHorizontalScrollbar(context, x, y, width, height);
        }
    }

    private void renderVerticalScrollbar(MinecraftRenderContext context, int x, int y, int width, int height) {
        // Draw track background
        context.drawRect(x, y, scrollbarWidth, height, trackColor);

        // Calculate thumb dimensions
        int thumbHeight = calculateThumbSize(height);
        int thumbY = calculateThumbPosition(y, height, thumbHeight);

        // Draw thumb with hover effect
        int currentThumbColor = (isHovered || isDragging) ? thumbHoverColor : thumbColor;
        int thumbX = x + 1;
        int thumbWidth = scrollbarWidth - 2;

        context.drawRect(thumbX, thumbY, thumbWidth, thumbHeight, currentThumbColor);
    }

    private void renderHorizontalScrollbar(MinecraftRenderContext context, int x, int y, int width, int height) {
        // Draw track background
        context.drawRect(x, y, width, scrollbarWidth, trackColor);

        // Calculate thumb dimensions
        int thumbWidth = calculateThumbSize(width);
        int thumbX = calculateThumbPosition(x, width, thumbWidth);

        // Draw thumb with hover effect
        int currentThumbColor = (isHovered || isDragging) ? thumbHoverColor : thumbColor;
        int thumbY = y + 1;
        int thumbHeight = scrollbarWidth - 2;

        context.drawRect(thumbX, thumbY, thumbWidth, thumbHeight, currentThumbColor);
    }

    private int calculateThumbSize(int trackSize) {
        if (contentSize <= 0 || viewportSize <= 0) {
            return minThumbSize;
        }

        // Thumb size proportional to viewport/content ratio
        int size = (viewportSize * trackSize) / contentSize;
        return Math.max(minThumbSize, Math.min(size, trackSize));
    }

    private int calculateThumbPosition(int trackStart, int trackSize, int thumbSize) {
        if (maxScrollOffset <= 0) {
            return trackStart;
        }

        int availableSpace = trackSize - thumbSize;
        if (availableSpace <= 0) {
            return trackStart;
        }

        int position = (scrollOffset * availableSpace) / maxScrollOffset;
        return trackStart + Math.max(0, Math.min(position, availableSpace));
    }

    /**
     * Updates the scrollbar state - must be called before rendering
     */
    public void updateScrollState(int offset, int maxOffset, int content, int viewport) {
        this.scrollOffset = Math.max(0, Math.min(maxOffset, offset));
        this.maxScrollOffset = Math.max(0, maxOffset);
        this.contentSize = Math.max(0, content);
        this.viewportSize = Math.max(0, viewport);
    }

    public void updateHoverState(double mouseX, double mouseY, int scrollOffset) {
        isHovered = isPointInScrollbar(mouseX, mouseY + scrollOffset);
    }

    private boolean isPointInScrollbar(double mouseX, double mouseY) {
        int x = getCalculatedX();
        int y = getCalculatedY();
        int width = getCalculatedWidth();
        int height = getCalculatedHeight();

        if (orientation == Orientation.VERTICAL) {
            return mouseX >= x && mouseX < x + scrollbarWidth &&
                    mouseY >= y && mouseY < y + height;
        } else {
            return mouseY >= y && mouseY < y + scrollbarWidth &&
                    mouseX >= x && mouseX < x + width;
        }
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        HTMLMyScreen.LOGGER.debug("Scrollbar.handleClick: mouse=({}, {}), bounds=({}, {}, {}, {}), scrollbarWidth={}",
                mouseX, mouseY, getCalculatedX(), getCalculatedY(),
                getCalculatedWidth(), getCalculatedHeight(), scrollbarWidth);

        if (!isPointInScrollbar(mouseX, mouseY) || button != 0) {
            HTMLMyScreen.LOGGER.debug("Not in scrollbar or wrong button");
            return false;
        }

        isDragging = true;
        dragStartPos = orientation == Orientation.VERTICAL ? mouseY : mouseX;
        dragStartOffset = scrollOffset;
        HTMLMyScreen.LOGGER.debug("Started dragging at pos={}", dragStartPos);
        return true;
    }

    public boolean handleDrag(double mouseX, double mouseY) {
        if (!isDragging) {
            return false;
        }

        double currentPos = orientation == Orientation.VERTICAL ? mouseY : mouseX;
        double delta = currentPos - dragStartPos;

        int trackSize = orientation == Orientation.VERTICAL ? getCalculatedHeight() : getCalculatedWidth();
        int thumbSize = calculateThumbSize(trackSize);
        int availableSpace = trackSize - thumbSize;

        if (availableSpace > 0 && maxScrollOffset > 0) {
            double ratio = delta / availableSpace;
            int offsetDelta = (int) (ratio * maxScrollOffset);

            scrollOffset = Math.max(0, Math.min(maxScrollOffset, dragStartOffset + offsetDelta));

            return true;
        }

        return false;
    }

    public void handleRelease() {
        isDragging = false;
    }

    @Override
    public boolean isPointInBounds(double x, double y) {
        return x >= getCalculatedX() && x < getCalculatedX() + getCalculatedWidth() &&
                y >= getCalculatedY() && y < getCalculatedY() + getCalculatedHeight();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int offset) {
        this.scrollOffset = Math.max(0, Math.min(maxScrollOffset, offset));
    }

    public int getMaxScrollOffset() {
        return maxScrollOffset;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public int getScrollbarWidth() {
        return scrollbarWidth;
    }
}