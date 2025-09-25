package com.edgn.edml.data.binding;

import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.component.html.EdmlEnum;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edss.EdssPropertyName;
import com.edgn.edml.component.html.components.EdssAwareComponent;
import com.edgn.edml.component.html.components.elements.TextComponent;
import com.edgn.edml.data.BindableComponent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SmartBindingProcessor {
    private final ReactiveBindingContext bindingContext;
    private final Set<EdmlComponent> boundComponents = new HashSet<>();

    public SmartBindingProcessor(ReactiveBindingContext bindingContext) {
        this.bindingContext = bindingContext;
    }

    public void processComponent(EdmlComponent component) {
        if (component instanceof BindableComponent bindable) {
            processBindableComponent(bindable);
        }
        processStandardBindings(component);
        component.getChildren().forEach(this::processComponent);
    }

    private void processBindableComponent(BindableComponent bindable) {
        Map<String, String> bindings = bindable.getBindingAttributes();

        for (Map.Entry<String, String> entry : bindings.entrySet()) {
            String property = entry.getKey();
            String path = entry.getValue();

            Object value = bindingContext.getValue(path);
            if (value != null) {
                bindable.updateFromBinding(property, value);
            }

            bindingContext.bindProperty(path, evt -> {
                bindable.updateFromBinding(property, evt.getNewValue());
            });
        }
    }

    private void processStandardBindings(EdmlComponent component) {
        // data-bind
        String bindPath = component.getAttribute(TagAttribute.DATA_BIND.getProperty(), "");
        if (!bindPath.isEmpty()) {
            setupBinding(component, BindingProperty.CONTENT, bindPath);
        }

        // data-visible
        String visiblePath = component.getAttribute(TagAttribute.DATA_VISIBLE.getProperty(), "");
        if (!visiblePath.isEmpty()) {
            setupBinding(component, BindingProperty.VISIBLE, visiblePath);
        }

        // data-class
        String classPath = component.getAttribute(TagAttribute.DATA_CLASS.getProperty(), "");
        if (!classPath.isEmpty()) {
            setupBinding(component, BindingProperty.CLASS, classPath);
        }

        // data-style
        String stylePath = component.getAttribute(TagAttribute.DATA_STYLE.getProperty(), "");
        if (!stylePath.isEmpty()) {
            setupBinding(component, BindingProperty.STYLE, stylePath);
        }
    }

    private void setupBinding(EdmlComponent component, BindingProperty property, String path) {
        Object value = bindingContext.getValue(path);
        applyBinding(component, property, value);

        bindingContext.bindProperty(path, evt -> {
            applyBinding(component, property, evt.getNewValue());
        });

        boundComponents.add(component);
    }

    private void applyBinding(EdmlComponent component, BindingProperty property, Object value) {
        switch (property) {
            case CONTENT -> {
                if (component.getTagName().equals(EdmlEnum.TEXT.getTagName()) &&
                        component instanceof TextComponent textComp && value != null) {
                    textComp.setTextContent(value.toString());
                }
            }
            case VISIBLE -> {
                if (component instanceof EdssAwareComponent cssComp && value instanceof Boolean visible) {
                    cssComp.setVisible(visible);
                }
            }
            case CLASS -> {
                if (value != null) {
                    component.applyAttribute(TagAttribute.CLASS.getProperty(), value.toString());
                }
            }
            case STYLE -> {
                if (value != null) {
                    component.applyAttribute(TagAttribute.STYLE.getProperty(), value.toString());
                }
            }
        }
    }

    public void dispose() {
        boundComponents.clear();
    }

    // Enum pour les types de binding
    private enum BindingProperty {
        CONTENT,
        VISIBLE,
        CLASS,
        STYLE
    }
}