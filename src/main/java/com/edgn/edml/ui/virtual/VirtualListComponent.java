package com.edgn.edml.ui.virtual;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.core.events.ClickableComponent;
import com.edgn.edml.core.events.DraggableComponent;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.components.EdssAwareComponent;
import com.edgn.edml.ui.scroll.ScrollableComponent;
import com.edgn.edml.ui.scroll.ScrollManager;
import com.edgn.edml.ui.scroll.ScrollbarComponent;
import com.edgn.edml.ui.clipping.ClippingRegion;
import com.edgn.edml.ui.clipping.ClippedRectangle;
import com.edgn.edml.ui.clipping.RectangleClipper;
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
    private int contentHeight = 0;
    private int viewportHeight = 0;

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
            itemHeight = 40;
        }

        String template = getAttribute(TagAttribute.DATA_TEMPLATE.getProperty(), "");
        if (!template.isEmpty()) {
            itemTemplate = HtmlUtils.decodeEntities(template);
        } else {
            itemTemplate = "{{" + itemVariable + "}}";
        }

        initializeScrollbar();
        this.viewportHeight = context.height();
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
        scrollbar.applyAttribute(TagAttribute.DATA_TRACK_COLOR.getProperty(), "#e0e0e0");
        scrollbar.applyAttribute(TagAttribute.DATA_THUMB_COLOR.getProperty(), "#888888");
        scrollbar.applyAttribute(TagAttribute.DATA_THUMB_HOVER_COLOR.getProperty(), "#555555");
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

            markNeedsLayout();
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        if (dataList == null || dataList.isEmpty()) {
            renderEmptyState(context, x, y, width, height);
            return;
        }

        this.viewportHeight = height;
        calculateContentHeight();
        updateVisibleRange();

        // Créer la région de clipping
        ClippingRegion clipRegion = new ClippingRegion(x, y, width, height);

        // Rendre avec clipping
        renderVisibleItemsWithClipping(context, clipRegion, x, y, width, height);
    }

    @Override
    protected void renderChildren(MinecraftRenderContext context) {
        if (canScroll() && scrollbar != null) {
            renderScrollbar(context);
        }
    }

    private void calculateContentHeight() {
        if (dataList == null) {
            contentHeight = 0;
            return;
        }
        contentHeight = dataList.size() * itemHeight;
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

        visibleStartIndex = Math.max(0, (scrollOffset / itemHeight) - 1);

        int itemsInViewport = (viewportHeight / itemHeight) + 3;
        visibleEndIndex = Math.min(dataList.size() - 1, visibleStartIndex + itemsInViewport);
    }

    private void renderVisibleItemsWithClipping(MinecraftRenderContext context, ClippingRegion clipRegion,
                                                int x, int y, int width, int height) {
        int scrollbarWidth = scrollbar != null ? scrollbar.getScrollbarWidth() : 0;
        int contentWidth = width - scrollbarWidth;

        for (int i = visibleStartIndex; i <= visibleEndIndex && i < dataList.size(); i++) {
            int itemAbsoluteY = i * itemHeight;
            int itemRenderY = y + itemAbsoluteY - scrollOffset;

            // Clipper l'item verticalement
            ClippedRectangle clippedItem = RectangleClipper.clipVertical(
                    clipRegion,
                    x,
                    itemRenderY,
                    contentWidth,
                    itemHeight
            );

            if (clippedItem.isCompletelyHidden()) {
                continue;
            }

            Object itemData = dataList.get(i);
            renderItemClipped(context, itemData, i, clippedItem, itemRenderY);
        }
    }

    private void renderItemClipped(MinecraftRenderContext context, Object itemData, int index,
                                   ClippedRectangle clipped, int originalY) {
        // Couleur alternée
        int itemColor = (index % 2 == 0) ?
                ColorUtils.parseColor("#ffffff") :
                ColorUtils.parseColor("#f9f9f9");

        // Rendre le background clippé
        context.drawRect(clipped.getX(), clipped.getY(), clipped.getWidth(), clipped.getHeight(), itemColor);

        // Calculer la position du texte
        String processedContent = processTemplate(itemData, index);

        int textX = clipped.getX() + 10;
        int textBaseY = originalY + (itemHeight - 10) / 2;

        // Vérifier si le texte est dans la zone visible
        int textHeight = 10;
        if (textBaseY >= clipped.getY() && textBaseY + textHeight <= clipped.getY() + clipped.getHeight()) {
            context.drawText(processedContent, textX, textBaseY, ColorUtils.parseColor("#333333"));
        }
    }

    private String processTemplate(Object itemData, int index) {
        String result = itemTemplate;
        result = result.replaceAll("\\{\\{\\s*" + Pattern.quote(itemVariable) + "\\s*\\}\\}",
                itemData.toString());
        result = result.replaceAll("\\{\\{\\s*index\\s*\\}\\}", String.valueOf(index));
        result = result.replaceAll("<[^>]*>", "").trim();
        return result;
    }

    private void renderScrollbar(MinecraftRenderContext context) {
        int scrollbarWidth = scrollbar.getScrollbarWidth();
        int x = getCalculatedX() + getCalculatedWidth() - scrollbarWidth;
        int y = getCalculatedY();
        int width = scrollbarWidth;
        int height = getCalculatedHeight();

        scrollbar.setCalculatedBounds(x, y, width, height);

        int maxOffset = getMaxScrollOffset();
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
            return true;
        }

        return false;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        if (!isPointInBounds(mouseX, mouseY)) {
            return false;
        }

        if (canScroll() && scrollbar != null) {
            if (scrollbar.handleClick(mouseX, mouseY, button)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean handleDrag(double mouseX, double mouseY) {
        if (scrollbar != null && scrollbar.isDragging()) {
            boolean handled = scrollbar.handleDrag(mouseX, mouseY);
            if (handled) {
                scrollOffset = scrollbar.getScrollOffset();
            }
            return handled;
        }
        return false;
    }

    @Override
    public void handleRelease() {
        if (scrollbar != null) {
            scrollbar.handleRelease();
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
        return Math.max(0, contentHeight - viewportHeight);
    }

    @Override
    public boolean canScroll() {
        if (dataList == null || dataList.isEmpty()) {
            return false;
        }
        return contentHeight > viewportHeight;
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