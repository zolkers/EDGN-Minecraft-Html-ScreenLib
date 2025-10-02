package com.edgn.edml.core.component;

import com.edgn.edml.dom.components.attributes.TagAttribute;

import java.util.*;

public abstract class AbstractEdmlComponent implements EdmlComponent {
    protected final String tagName;
    protected final Map<String, String> attributes = new LinkedHashMap<>();
    protected final List<EdmlComponent> children = new ArrayList<>();
    protected final Set<String> validAttributes;

    protected AbstractEdmlComponent(String tagName, Set<String> validAttributes) {
        this.tagName = tagName;
        this.validAttributes = Set.copyOf(validAttributes);
    }

    @Override
    public String getTagName() {
        return tagName;
    }

    @Override
    public void applyAttribute(String name, String value) {
        if (!validAttributes.contains(name) && !name.startsWith(TagAttribute.DATA_BEGIN.getProperty())) {
            throw new IllegalArgumentException("Attribute '" + name + "' is not valid for " + tagName + " component");
        }
        attributes.put(name, value);
    }

    @Override
    public void addChild(EdmlComponent child) {
        children.add(child);
    }

    @Override
    public List<EdmlComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String getAttribute(String name, String defaultValue) {
        return attributes.getOrDefault(name, defaultValue);
    }

    protected String getClassList() {
        return getAttribute(TagAttribute.CLASS.getProperty(), "");
    }

    public boolean hasClass(String className) {
        String classList = getClassList();
        return Arrays.asList(classList.split("\\s+")).contains(className);
    }

    public void clearChildren() {
        children.clear();
    }
}