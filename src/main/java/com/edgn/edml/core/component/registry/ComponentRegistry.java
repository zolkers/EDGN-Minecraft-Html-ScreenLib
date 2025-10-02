package com.edgn.edml.core.component.registry;

import com.edgn.edml.core.component.ComponentFactory;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.exceptions.ComponentCreationException;

import java.util.Set;

public interface ComponentRegistry {
    void registerComponent(String tagName, ComponentFactory factory);
    EdmlComponent createComponent(String tagName) throws ComponentCreationException;
    Set<String> getRegisteredTags();
    boolean isTagRegistered(String tagName);
}
