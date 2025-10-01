package com.edgn.edml.data.binding;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.component.AbstractEdmlComponent;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.component.edml.components.containers.DivComponent;
import com.edgn.edml.component.edml.components.scroll.ScrollManager;
import com.edgn.edml.component.edml.components.scroll.ScrollableComponent;
import com.edgn.edml.data.IBindingContext;
import com.edgn.edml.data.collections.ObservableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBindingEngine {
    private static final Pattern BINDING_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");
    private final IBindingContext bindingContext;
    private final Map<String, Set<BoundComponent>> boundComponents = new ConcurrentHashMap<>();
    private final Map<String, VirtualListBinding> virtualListBindings = new ConcurrentHashMap<>();

    public DataBindingEngine(IBindingContext bindingContext) {
        this.bindingContext = bindingContext;
    }

    public void processComponent(EdmlComponent component) {
        processStandardBindings(component);
        processListBindings(component);

        for (EdmlComponent child : component.getChildren()) {
            processComponent(child);
        }
    }

    private void processStandardBindings(EdmlComponent component) {
        TagAttribute[] bindingAttributes = {
                TagAttribute.DATA_TEXT,
                TagAttribute.DATA_CLASS,
                TagAttribute.DATA_STYLE,
                TagAttribute.DATA_VISIBLE
        };

        for (TagAttribute attribute : bindingAttributes) {
            String value = component.getAttribute(attribute.getProperty(), "");
            if (containsBinding(value)) {
                createBinding(component, attribute.getProperty(), value);
            }
        }
    }

    private void processListBindings(EdmlComponent component) {
        String forAttribute = component.getAttribute("data-for", "");
        HTMLMyScreen.LOGGER.info("Processing component {}, data-for: '{}'", component.getTagName(), forAttribute);

        if (!forAttribute.isEmpty()) {
            String[] parts = forAttribute.split("\\s+in\\s+");
            HTMLMyScreen.LOGGER.info("Split result: {}", java.util.Arrays.toString(parts));

            if (parts.length == 2) {
                String itemVar = parts[0].trim();
                String listPath = parts[1].trim();
                HTMLMyScreen.LOGGER.info("Creating virtual list binding: itemVar='{}', listPath='{}'", itemVar, listPath);
                createVirtualListBinding(component, itemVar, listPath);
            } else {
                HTMLMyScreen.LOGGER.warn("Invalid data-for format: '{}'", forAttribute);
            }
        }
    }


    private boolean containsBinding(String value) {
        return BINDING_PATTERN.matcher(value).find();
    }

    private void createBinding(EdmlComponent component, String attributeName, String value) {
        Set<String> bindingPaths = extractBindingPaths(value);

        for (String path : bindingPaths) {
            BoundComponent bound = new BoundComponent(component, attributeName, value, bindingPaths);
            boundComponents.computeIfAbsent(path, k -> ConcurrentHashMap.newKeySet()).add(bound);

            bindingContext.bindProperty(path, evt -> updateBoundComponent(bound));
            updateBoundComponent(bound);
        }
    }

    private void createVirtualListBinding(EdmlComponent component, String itemVar, String listPath) {
        Object listData = bindingContext.getValue(listPath);
        HTMLMyScreen.LOGGER.info("Looking for list data at path '{}', found: {}", listPath, listData != null ? listData.getClass().getSimpleName() : "null");

        if (listData instanceof ObservableList<?> observableList) {
            HTMLMyScreen.LOGGER.info("Found ObservableList with {} items", observableList.size());
            String componentId = component.getAttribute(TagAttribute.ID.getProperty(), UUID.randomUUID().toString());
            VirtualListBinding binding = new VirtualListBinding(component, itemVar, listPath, (ObservableList<Object>) observableList);
            virtualListBindings.put(componentId, binding);

            for (int i = 0; i < Math.min(5, observableList.size()); i++) {
                HTMLMyScreen.LOGGER.info("List item {}: {}", i, observableList.get(i));
            }

            binding.refresh();
            HTMLMyScreen.LOGGER.info("Virtual list binding created and refreshed");
        } else {
            HTMLMyScreen.LOGGER.error("No ObservableList found at path '{}' for component with id '{}'",
                    listPath, component.getAttribute(TagAttribute.ID.getProperty(), "no-id"));
        }
    }


    private Set<String> extractBindingPaths(String value) {
        Set<String> paths = new HashSet<>();
        Matcher matcher = BINDING_PATTERN.matcher(value);
        while (matcher.find()) {
            paths.add(matcher.group(1).trim());
        }
        return paths;
    }

    private void updateBoundComponent(BoundComponent bound) {
        String processedValue = bound.originalValue;

        for (String path : bound.bindingPaths) {
            Object value = bindingContext.getValue(path);
            String replacement = value != null ? value.toString() : "";
            processedValue = processedValue.replaceAll("\\{\\{\\s*" + Pattern.quote(path) + "\\s*\\}\\}", replacement);
        }

        bound.component.applyAttribute(bound.attributeName, processedValue);
    }

    public void scrollList(String listId, int offset) {
        VirtualListBinding binding = virtualListBindings.get(listId);
        if (binding != null) {
            binding.scroll(offset);
        }
    }

    public void dispose() {
        boundComponents.clear();
        virtualListBindings.clear();
    }

    private static class BoundComponent {
        final EdmlComponent component;
        final String attributeName;
        final String originalValue;
        final Set<String> bindingPaths;

        BoundComponent(EdmlComponent component, String attributeName, String originalValue, Set<String> bindingPaths) {
            this.component = component;
            this.attributeName = attributeName;
            this.originalValue = originalValue;
            this.bindingPaths = bindingPaths;
        }
    }

    private static class VirtualListBinding {
        final EdmlComponent listContainer;
        final String itemVariable;
        final String listPath;
        final ObservableList<Object> observableList;
        final String itemTemplate;
        final List<EdmlComponent> renderedItems = new ArrayList<>();
        int visibleStart = 0;
        int visibleEnd = 0;
        int itemHeight = 40;
        int scrollOffset = 0;

        VirtualListBinding(EdmlComponent container, String itemVar, String listPath, ObservableList<Object> list) {
            this.listContainer = container;
            this.itemVariable = itemVar;
            this.listPath = listPath;
            this.observableList = list;
            this.itemTemplate = decodeHtmlEntities(extractItemTemplate(container));

            String heightAttr = container.getAttribute(TagAttribute.DATA_ITEM_HEIGHT.getProperty(), "40");
            try {
                this.itemHeight = Integer.parseInt(heightAttr);
            } catch (NumberFormatException e) {
                HTMLMyScreen.LOGGER.warn("Invalid item height: {}, using default: 40", heightAttr);
                this.itemHeight = 40;
            }

            makeContainerScrollable(container);
        }

        private void makeContainerScrollable(EdmlComponent container) {
            if (container instanceof EdssAwareComponent cssContainer) {
                ScrollManager.getInstance().registerScrollable(new ScrollableComponent() {
                    @Override
                    public boolean handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
                        if (!canScroll()) return false;

                        int scrollDelta = (int) (-verticalAmount * itemHeight * 2);
                        int newOffset = Math.max(0, Math.min(getMaxScrollOffset(), scrollOffset + scrollDelta));

                        if (newOffset != scrollOffset) {
                            scrollOffset = newOffset;
                            refresh();
                            HTMLMyScreen.LOGGER.debug("Virtual list scrolled to offset: {}", scrollOffset);
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public boolean isPointInBounds(double x, double y) {
                        return x >= cssContainer.getCalculatedX() && x < cssContainer.getCalculatedX() + cssContainer.getCalculatedWidth() &&
                                y >= cssContainer.getCalculatedY() && y < cssContainer.getCalculatedY() + cssContainer.getCalculatedHeight();
                    }

                    @Override
                    public int getScrollOffset() {
                        return scrollOffset;
                    }

                    @Override
                    public void setScrollOffset(int offset) {
                        scrollOffset = Math.max(0, Math.min(getMaxScrollOffset(), offset));
                        refresh();
                    }

                    @Override
                    public int getMaxScrollOffset() {
                        int totalContentHeight = observableList.size() * itemHeight;
                        int containerHeight = cssContainer.getCalculatedHeight();
                        return Math.max(0, totalContentHeight - containerHeight);
                    }

                    @Override
                    public boolean canScroll() {
                        return observableList.size() * itemHeight > cssContainer.getCalculatedHeight();
                    }
                });
            }
        }

        private String decodeHtmlEntities(String html) {
            return html.replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&amp;", "&")
                    .replace("&quot;", "\"")
                    .replace("&apos;", "'");
        }

        private String extractItemTemplate(EdmlComponent container) {
            String template = container.getAttribute(TagAttribute.DATA_TEMPLATE.getProperty(), "");
            if (template.isEmpty()) {
                template = "<div class=\"list-item\"><div>{{" + itemVariable + "}}</div></div>";
            }
            return template;
        }

        void refresh() {
            updateVisibleRange();
            renderVisibleItems();
        }

        void refreshItem(int index) {
            if (index >= visibleStart && index <= visibleEnd) {
                int visibleIndex = index - visibleStart;
                if (visibleIndex < renderedItems.size()) {
                    updateContainerChildren();
                }
            }
        }

        void clear() {
            renderedItems.clear();
            if (listContainer instanceof AbstractEdmlComponent abstractContainer) {
                abstractContainer.clearChildren();
            }
        }

        private void updateVisibleRange() {
            int containerHeight = getContainerHeight();
            int itemsPerPage = Math.max(1, containerHeight / itemHeight);

            visibleStart = Math.max(0, scrollOffset / itemHeight);
            visibleEnd = Math.min(observableList.size() - 1, visibleStart + itemsPerPage + 2);
        }

        private void renderVisibleItems() {
            renderedItems.clear();

            for (int i = visibleStart; i <= visibleEnd && i < observableList.size(); i++) {
                Object itemData = observableList.get(i);
                EdmlComponent item = createSimpleItemComponent(itemData, i);
                if (item != null) {
                    renderedItems.add(item);
                }
            }

            updateContainerChildren();
        }

        private EdmlComponent createSimpleItemComponent(Object itemData, int index) {
            try {
                var divComponent = new DivComponent();

                String processedTemplate = itemTemplate
                        .replaceAll("\\{\\{\\s*" + Pattern.quote(itemVariable) + "\\s*\\}\\}", itemData.toString())
                        .replaceAll("\\{\\{\\s*index\\s*\\}\\}", String.valueOf(index));

                String textContent = extractTextFromTemplate(processedTemplate);
                if (!textContent.isEmpty()) {
                    divComponent.applyAttribute(TagAttribute.DATA_TEXT.getProperty(), textContent);
                }

                String classAttr = extractClassFromTemplate(processedTemplate);
                if (!classAttr.isEmpty()) {
                    divComponent.applyAttribute(TagAttribute.CLASS.getProperty(), classAttr);
                }

                divComponent.setHeight(itemHeight);
                return divComponent;

            } catch (Exception e) {
                HTMLMyScreen.LOGGER.error("Error creating item component at index {}: {}", index, e.getMessage());
                return null;
            }
        }

        private String extractTextFromTemplate(String template) {
            return template.replaceAll("<[^>]*>", "").trim();
        }

        private String extractClassFromTemplate(String template) {
            Pattern classPattern = Pattern.compile("class=\"([^\"]+)\"");
            Matcher matcher = classPattern.matcher(template);
            return matcher.find() ? matcher.group(1) : "";
        }

        private void updateContainerChildren() {
            if (listContainer instanceof AbstractEdmlComponent abstractContainer) {
                abstractContainer.clearChildren();
                for (EdmlComponent item : renderedItems) {
                    abstractContainer.addChild(item);
                }
            }
        }

        private int getContainerHeight() {
            String heightAttr = listContainer.getAttribute(TagAttribute.HEIGHT.getProperty(), "400");
            try {
                return Integer.parseInt(heightAttr);
            } catch (NumberFormatException e) {
                HTMLMyScreen.LOGGER.warn("Invalid container height: {}, using default: 400", heightAttr);
                return 400;
            }
        }

        public void scroll(int offset) {
            this.scrollOffset = Math.max(0, offset);
            refresh();
        }

        public ObservableList<Object> getObservableList() {
            return observableList;
        }
    }
}