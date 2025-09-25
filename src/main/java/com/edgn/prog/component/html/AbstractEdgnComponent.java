package com.edgn.prog.component.html;

import com.edgn.prog.component.attribute.TagAttribute;

import java.util.*;

public abstract class AbstractEdgnComponent implements EdgnComponent {
    protected final String tagName;
    protected final Map<String, String> attributes = new LinkedHashMap<>();
    protected final List<EdgnComponent> children = new ArrayList<>();
    protected final Set<String> validAttributes;

    protected AbstractEdgnComponent(String tagName, Set<String> validAttributes) {
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
    public void addChild(EdgnComponent child) {
        children.add(child);
    }

    @Override
    public List<EdgnComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }

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
}