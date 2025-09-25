package com.edgn.edml.component.html;

import com.edgn.edml.exceptions.ComponentCreationException;

import java.util.Set;

public interface ComponentRegistry {
    void registerComponent(String tagName, ComponentFactory factory);
    EdmlComponent createComponent(String tagName) throws ComponentCreationException;
    Set<String> getRegisteredTags();
    boolean isTagRegistered(String tagName);
}
