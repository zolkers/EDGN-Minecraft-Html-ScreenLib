package com.edgn.edml.core.component.registry;

import com.edgn.edml.core.component.ComponentFactory;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.exceptions.ComponentCreationException;
import java.util.Set;

public final class EdmlComponentRegistry implements ComponentRegistry {
    private static final ComponentRegistry INSTANCE = ComponentRegistryBuilder.createDefault();

    public static ComponentRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerComponent(String tagName, ComponentFactory factory) {
        INSTANCE.registerComponent(tagName, factory);
    }

    @Override
    public EdmlComponent createComponent(String tagName) throws ComponentCreationException {
        return INSTANCE.createComponent(tagName);
    }

    @Override
    public Set<String> getRegisteredTags() {
        return INSTANCE.getRegisteredTags();
    }

    @Override
    public boolean isTagRegistered(String tagName) {
        return INSTANCE.isTagRegistered(tagName);
    }
}