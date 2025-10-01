package com.edgn.edml.data.binding;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.component.AbstractEdmlComponent;
import com.edgn.edml.component.edml.component.EdmlComponent;
import com.edgn.edml.component.edml.components.ui.VirtualListComponent;
import com.edgn.edml.data.IBindingContext;
import com.edgn.edml.data.collections.ObservableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public final class DataBindingEngine {

    private static final Pattern BINDING_PATTERN = Pattern.compile("\\{\\{([^}]+)\\}\\}");

    private final IBindingContext bindingContext;
    private final Map<String, Set<BoundComponent>> boundComponents = new ConcurrentHashMap<>();
    private final Map<String, VirtualListComponent> virtualListComponents = new ConcurrentHashMap<>();

    public DataBindingEngine(IBindingContext bindingContext) {
        this.bindingContext = Objects.requireNonNull(bindingContext, "BindingContext cannot be null");
    }

    public void processComponent(EdmlComponent component) {
        if (component instanceof VirtualListComponent virtualList) {
            processVirtualList(virtualList);
        } else {
            processStandardBindings(component);
        }

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

    private void processVirtualList(VirtualListComponent virtualList) {
        String forAttribute = virtualList.getAttribute(TagAttribute.DATA_FOR.getProperty(), "");

        if (forAttribute.isEmpty()) {
            HTMLMyScreen.LOGGER.warn("VirtualListComponent found without data-for attribute");
            return;
        }

        String[] parts = forAttribute.split("\\s+in\\s+");
        if (parts.length != 2) {
            HTMLMyScreen.LOGGER.warn("Invalid data-for format: '{}'", forAttribute);
            return;
        }

        String listPath = parts[1].trim();
        Object listData = bindingContext.getValue(listPath);

        if (listData instanceof ObservableList<?> observableList) {
            virtualList.bindToList(observableList);

            String componentId = virtualList.getAttribute(TagAttribute.ID.getProperty(), UUID.randomUUID().toString());
            virtualListComponents.put(componentId, virtualList);

            HTMLMyScreen.LOGGER.info("VirtualListComponent bound to list at '{}' with {} items", listPath, observableList.size());
        } else {
            HTMLMyScreen.LOGGER.error("No ObservableList found at path '{}' for VirtualListComponent", listPath);
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
            processedValue = processedValue.replaceAll(
                    "\\{\\{\\s*" + Pattern.quote(path) + "\\s*\\}\\}",
                    Matcher.quoteReplacement(replacement)
            );
        }

        bound.component.applyAttribute(bound.attributeName, processedValue);
    }

    public VirtualListComponent getVirtualList(String listId) {
        return virtualListComponents.get(listId);
    }

    public void dispose() {
        for (Set<BoundComponent> components : boundComponents.values()) {
            for (BoundComponent bound : components) {
                for (String path : bound.bindingPaths) {
                    bindingContext.unbindProperty(path, evt -> updateBoundComponent(bound));
                }
            }
        }

        boundComponents.clear();
        virtualListComponents.clear();

        HTMLMyScreen.LOGGER.debug("DataBindingEngine disposed");
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BoundComponent that)) return false;
            return Objects.equals(component, that.component) &&
                    Objects.equals(attributeName, that.attributeName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(component, attributeName);
        }
    }
}