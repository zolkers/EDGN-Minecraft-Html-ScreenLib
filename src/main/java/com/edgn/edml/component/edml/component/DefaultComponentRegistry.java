package com.edgn.edml.component.edml.component;

import com.edgn.edml.exceptions.ComponentCreationException;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DefaultComponentRegistry implements ComponentRegistry {
    private final Map<String, ComponentFactory> factories = new ConcurrentHashMap<>();

    @Override
    public void registerComponent(String tagName, ComponentFactory factory) {
        Objects.requireNonNull(tagName, "Tag name cannot be null");
        Objects.requireNonNull(factory, "Factory cannot be null");

        if (tagName.isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be blank");
        }

        factories.put(tagName.toLowerCase(), factory);
    }

    @Override
    public EdmlComponent createComponent(String tagName) throws ComponentCreationException {
        ComponentFactory factory = factories.get(tagName.toLowerCase());
        if (factory == null) {
            throw new ComponentCreationException("Unknown tag: " + tagName);
        }

        try {
            return factory.create();
        } catch (Exception e) {
            throw new ComponentCreationException("Failed to create component for tag: " + tagName, e);
        }
    }

    @Override
    public Set<String> getRegisteredTags() {
        return Set.copyOf(factories.keySet());
    }

    @Override
    public boolean isTagRegistered(String tagName) {
        return factories.containsKey(tagName.toLowerCase());
    }
}