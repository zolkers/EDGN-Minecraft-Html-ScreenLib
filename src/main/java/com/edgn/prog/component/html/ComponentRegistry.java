package com.edgn.prog.component.html;

import com.edgn.prog.exceptions.ComponentCreationException;

import java.util.Set;

public interface ComponentRegistry {
    void registerComponent(String tagName, ComponentFactory factory);
    EdgnComponent createComponent(String tagName) throws ComponentCreationException;
    Set<String> getRegisteredTags();
    boolean isTagRegistered(String tagName);
}
