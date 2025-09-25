package com.edgn.prog.component.html;

import com.edgn.prog.exceptions.ComponentCreationException;
import java.util.Set;

public final class EdgnComponentRegistry implements ComponentRegistry {
    private static final ComponentRegistry INSTANCE = ComponentRegistryBuilder.createDefault();

    public static ComponentRegistry getInstance() {
        return INSTANCE;
    }

    @Override
    public void registerComponent(String tagName, ComponentFactory factory) {
        INSTANCE.registerComponent(tagName, factory);
    }

    @Override
    public EdgnComponent createComponent(String tagName) throws ComponentCreationException {
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