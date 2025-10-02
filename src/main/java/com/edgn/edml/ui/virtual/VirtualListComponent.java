package com.edgn.edml.ui.virtual;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.core.events.ClickableComponent;
import com.edgn.edml.core.events.DraggableComponent;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.components.EdssAwareComponent;
import com.edgn.edml.ui.scroll.ScrollableComponent;
import com.edgn.edml.ui.scroll.ScrollManager;
import com.edgn.edml.ui.scroll.ScrollbarComponent;
import com.edgn.edml.data.collections.ObservableList;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;
import com.edgn.edml.utils.HtmlUtils;

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
        // Render scrollbar outside any parent transforms
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
        int itemsPerPage = Math.max(1, (viewportHeight / itemHeight) + 2);

        visibleStartIndex = Math.max(0, scrollOffset / itemHeight);
        visibleEndIndex = Math.min(dataList.size() - 1, visibleStartIndex + itemsPerPage);
    }

    private void renderVisibleItems(MinecraftRenderContext context, int x, int y, int width, int height) {
        int scrollbarWidth = scrollbar != null ? scrollbar.getScrollbarWidth() : 0;
        int contentWidth = width - scrollbarWidth;

        int currentY = y - (scrollOffset % itemHeight);

        for (int i = visibleStartIndex; i <= visibleEndIndex && i < dataList.size(); i++) {
            if (currentY + itemHeight < y || currentY >= y + height) {
                currentY += itemHeight;
                continue;
            }

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

    private void renderScrollbar(MinecraftRenderContext context, int x, int y, int width, int viewportHeight) {
        if (scrollbar == null || !canScroll()) {
            return;
        }

        int scrollbarWidth = scrollbar.getScrollbarWidth();
        int scrollbarX = x + width - scrollbarWidth;

        scrollbar.setCalculatedBounds(scrollbarX, y, scrollbarWidth, viewportHeight);

        int contentHeight = dataList.size() * itemHeight;
        int maxOffset = Math.max(0, contentHeight - viewportHeight);

        scrollbar.updateScrollState(scrollOffset, maxOffset, contentHeight, viewportHeight);
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
            HTMLMyScreen.LOGGER.debug("VirtualList scrolled to offset: {}", scrollOffset);
            return true;
        }

        return false;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        HTMLMyScreen.LOGGER.debug("VirtualList.handleClick: mouse=({}, {})", mouseX, mouseY);

        if (!isPointInBounds(mouseX, mouseY)) {
            HTMLMyScreen.LOGGER.debug("VirtualList: Click outside bounds");
            return false;
        }

        if (canScroll() && scrollbar != null) {
            HTMLMyScreen.LOGGER.debug("VirtualList: Checking scrollbar click");
            boolean handled = scrollbar.handleClick(mouseX, mouseY, button);
            HTMLMyScreen.LOGGER.debug("VirtualList: scrollbar handled={}", handled);
            return handled;
        }

        return false;
    }

    @Override
    public boolean handleDrag(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public void handleRelease() {
        if (scrollbar != null) {
            scrollbar.handleRelease();
            HTMLMyScreen.LOGGER.debug("VirtualList: scrollbar released");
        }
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