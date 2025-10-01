package com.edgn.edml.component.edml.components.ui;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.ClickableComponent;
import com.edgn.edml.component.DraggableComponent;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.component.edml.components.scroll.ScrollableComponent;
import com.edgn.edml.component.edml.components.scroll.ScrollManager;
import com.edgn.edml.data.collections.ObservableList;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;
import com.edgn.utils.HtmlUtils;

import java.util.*;
import java.util.regex.Pattern;

public final class VirtualListComponent extends EdssAwareComponent implements ScrollableComponent, ClickableComponent, DraggableComponent {

    private static final Set<String> VIRTUAL_LIST_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(),
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.DATA_FOR.getProperty(),
            TagAttribute.DATA_LIST.getProperty(),
            TagAttribute.DATA_TEMPLATE.getProperty(),
            TagAttribute.DATA_ITEM_HEIGHT.getProperty()
    );

    private ObservableList<?> dataList;
    private String itemVariable = "item";
    private String itemTemplate = "";
    private int itemHeight = 40;
    private int scrollOffset = 0;

    private int visibleStartIndex = 0;
    private int visibleEndIndex = 0;

    private ScrollbarComponent scrollbar;
    private final ObservableList.ListChangeListener<Object> listChangeListener;

    public VirtualListComponent() {
        super("virtual-list", VIRTUAL_LIST_ATTRIBUTES);

        this.listChangeListener = new ObservableList.ListChangeListener<>() {
            @Override
            public void onItemAdded(int index, Object item) {
                markNeedsLayout();
            }

            @Override
            public void onItemRemoved(int index, Object item) {
                markNeedsLayout();
            }

            @Override
            public void onItemChanged(int index, Object oldItem, Object newItem) {
                // No layout needed for content change
            }

            @Override
            public void onListCleared() {
                scrollOffset = 0;
                markNeedsLayout();
            }

            @Override
            public void onRangeAdded(int startIndex, Collection<Object> items) {
                markNeedsLayout();
            }

            @Override
            public void onRangeRemoved(int startIndex, int count) {
                markNeedsLayout();
            }
        };

        ScrollManager.getInstance().registerScrollable(this);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String dataForAttr = getAttribute(TagAttribute.DATA_FOR.getProperty(), "");
        if (!dataForAttr.isEmpty()) {
            parseDataForAttribute(dataForAttr);
        }

        String itemHeightStr = getAttribute(TagAttribute.DATA_ITEM_HEIGHT.getProperty(), "40");
        try {
            itemHeight = Integer.parseInt(itemHeightStr);
        } catch (NumberFormatException e) {
            HTMLMyScreen.LOGGER.warn("Invalid item height: {}, using default: 40", itemHeightStr);
            itemHeight = 40;
        }

        String template = getAttribute(TagAttribute.DATA_TEMPLATE.getProperty(), "");
        if (!template.isEmpty()) {
            itemTemplate = HtmlUtils.decodeEntities(template);
        } else {
            itemTemplate = "{{" + itemVariable + "}}";
        }

        initializeScrollbar();
    }

    private void parseDataForAttribute(String dataFor) {
        String[] parts = dataFor.split("\\s+in\\s+");
        if (parts.length == 2) {
            itemVariable = parts[0].trim();
            String listPath = parts[1].trim();
            HTMLMyScreen.LOGGER.debug("Parsed data-for: itemVar='{}', listPath='{}'", itemVariable, listPath);
        } else {
            HTMLMyScreen.LOGGER.warn("Invalid data-for format: '{}'", dataFor);
        }
    }

    private void initializeScrollbar() {
        scrollbar = new ScrollbarComponent();
        scrollbar.applyAttribute(TagAttribute.DATA_ORIENTATION.getProperty(), "vertical");
        scrollbar.applyAttribute(TagAttribute.DATA_SCROLLBAR_WIDTH.getProperty(), "8");
    }

    public void bindToList(ObservableList<?> list) {
        if (this.dataList != null) {
            @SuppressWarnings("unchecked")
            ObservableList<Object> oldList = (ObservableList<Object>) this.dataList;
            oldList.removeListener(listChangeListener);
        }

        this.dataList = list;

        if (list != null) {
            @SuppressWarnings("unchecked")
            ObservableList<Object> observableList = (ObservableList<Object>) list;
            observableList.addListener(listChangeListener);

            HTMLMyScreen.LOGGER.info("VirtualList bound to list with {} items", list.size());
            markNeedsLayout();
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        if (dataList == null || dataList.isEmpty()) {
            renderEmptyState(context, x, y, width, height);
            return;
        }

        context.pushTransform(0, 0, width, height);

        updateVisibleRange();
        renderVisibleItems(context, x, y, width, height);

        context.popTransform();
    }

    @Override
    protected void renderChildren(MinecraftRenderContext context) {
        // VirtualList doesn't have traditional children
        // Render scrollbar here so it's outside any parent transforms
        if (canScroll() && scrollbar != null) {
            renderScrollbar(context, getCalculatedX(), getCalculatedY(),
                    getCalculatedWidth(), getCalculatedHeight());
        }
    }

    private void renderEmptyState(MinecraftRenderContext context, int x, int y, int width, int height) {
        String emptyText = "No items to display";
        int textX = x + (width / 2) - 60;
        int textY = y + (height / 2);
        context.drawText(emptyText, textX, textY, ColorUtils.parseColor("#999999"));
    }

    private void updateVisibleRange() {
        if (dataList == null || dataList.isEmpty()) {
            visibleStartIndex = 0;
            visibleEndIndex = 0;
            return;
        }

        int viewportHeight = getCalculatedHeight();
        int itemsPerPage = Math.max(1, (viewportHeight / itemHeight) + 2); // +2 for buffer

        visibleStartIndex = Math.max(0, scrollOffset / itemHeight);
        visibleEndIndex = Math.min(dataList.size() - 1, visibleStartIndex + itemsPerPage);
    }

    private void renderVisibleItems(MinecraftRenderContext context, int x, int y, int width, int height) {
        int scrollbarWidth = scrollbar != null ? scrollbar.getScrollbarWidth() : 0;
        int contentWidth = width - scrollbarWidth;

        int currentY = y - (scrollOffset % itemHeight);

        HTMLMyScreen.LOGGER.info("renderVisibleItems: scrollOffset={}, startY={}, visibleRange=[{}, {}]",
                scrollOffset, currentY, visibleStartIndex, visibleEndIndex);

        for (int i = visibleStartIndex; i <= visibleEndIndex && i < dataList.size(); i++) {
            if (currentY + itemHeight < y || currentY >= y + height) {
                currentY += itemHeight;
                continue;
            }

            HTMLMyScreen.LOGGER.info("Rendering item {} at Y={}", i, currentY);
            Object itemData = dataList.get(i);
            renderItem(context, itemData, i, x, currentY, contentWidth);
            currentY += itemHeight;
        }
    }

    private void renderItem(MinecraftRenderContext context, Object itemData, int index, int x, int y, int width) {
        int itemColor = (index % 2 == 0) ?
                ColorUtils.parseColor("#ffffff") :
                ColorUtils.parseColor("#f9f9f9");

        context.drawRect(x, y, width, itemHeight, itemColor);

        String processedContent = processTemplate(itemData, index);

        int textX = x + 10;
        int textY = y + (itemHeight - 10) / 2;
        context.drawText(processedContent, textX, textY, ColorUtils.parseColor("#333333"));
    }

    private String processTemplate(Object itemData, int index) {
        String result = itemTemplate;
        result = result.replaceAll("\\{\\{\\s*" + Pattern.quote(itemVariable) + "\\s*\\}\\}",
                itemData.toString());
        result = result.replaceAll("\\{\\{\\s*index\\s*\\}\\}", String.valueOf(index));
        result = result.replaceAll("<[^>]*>", "").trim();
        return result;
    }

    private void renderScrollbar(MinecraftRenderContext context, int x, int y, int width, int height) {
        if (scrollbar == null || !canScroll()) {
            return;
        }

        int scrollbarWidth = scrollbar.getScrollbarWidth();
        int scrollbarX = x + width - scrollbarWidth;

        scrollbar.setCalculatedBounds(scrollbarX, y, scrollbarWidth, height);

        int contentHeight = dataList.size() * itemHeight;
        int viewportHeight = height;
        int maxOffset = Math.max(0, contentHeight - viewportHeight);

        int clampedMaxOffset = Math.min(maxOffset, contentHeight);

        scrollbar.updateScrollState(scrollOffset, clampedMaxOffset, contentHeight, viewportHeight);
        scrollbar.render(context);
    }

    @Override
    public boolean handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (!canScroll()) {
            return false;
        }

        int scrollDelta = (int) (-verticalAmount * itemHeight * 3);
        int newOffset = Math.max(0, Math.min(getMaxScrollOffset(), scrollOffset + scrollDelta));

        if (newOffset != scrollOffset) {
            scrollOffset = newOffset;
            if (scrollbar != null) {
                scrollbar.setScrollOffset(scrollOffset);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        HTMLMyScreen.LOGGER.info("VirtualList.handleClick: mouse=({}, {}), bounds=({}, {}, {}, {})",
                mouseX, mouseY, getCalculatedX(), getCalculatedY(),
                getCalculatedWidth(), getCalculatedHeight());

        if (!isPointInBounds(mouseX, mouseY)) {
            HTMLMyScreen.LOGGER.info("VirtualList: Click outside bounds");
            return false;
        }

        HTMLMyScreen.LOGGER.info("VirtualList: canScroll={}, scrollbar!=null={}",
                canScroll(), scrollbar != null);

        if (canScroll() && scrollbar != null) {
            HTMLMyScreen.LOGGER.info("VirtualList: About to call scrollbar.handleClick()");
            boolean handled = scrollbar.handleClick(mouseX, mouseY, button);
            HTMLMyScreen.LOGGER.info("VirtualList: scrollbar.handleClick returned {}", handled);
            return handled;
        }

        return false;
    }

    public boolean handleDrag(double mouseX, double mouseY) {
        if (scrollbar != null && scrollbar.isDragging()) {
            if (scrollbar.handleDrag(mouseX, mouseY)) {
                scrollOffset = scrollbar.getScrollOffset();
                HTMLMyScreen.LOGGER.debug("VirtualList scrollbar dragged to offset: {}", scrollOffset);
                return true;
            }
        }
        return false;
    }

    public void handleRelease() {
        if (scrollbar != null) {
            scrollbar.handleRelease();
        }
    }

    public boolean isDraggingScrollbar() {
        return scrollbar != null && scrollbar.isDragging();
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
        if (dataList == null || dataList.isEmpty()) {
            return 0;
        }
        int contentHeight = dataList.size() * itemHeight;
        int viewportHeight = getCalculatedHeight();
        return Math.max(0, contentHeight - viewportHeight);
    }

    @Override
    public boolean canScroll() {
        if (dataList == null || dataList.isEmpty()) {
            return false;
        }
        return dataList.size() * itemHeight > getCalculatedHeight();
    }

    public ObservableList<?> getDataList() {
        return dataList;
    }

    public String getItemVariable() {
        return itemVariable;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    protected void dispose() {
        if (dataList != null) {
            @SuppressWarnings("unchecked")
            ObservableList<Object> observableList = (ObservableList<Object>) dataList;
            observableList.removeListener(listChangeListener);
        }
        ScrollManager.getInstance().unregisterScrollable(this);
        super.dispose();
    }
}