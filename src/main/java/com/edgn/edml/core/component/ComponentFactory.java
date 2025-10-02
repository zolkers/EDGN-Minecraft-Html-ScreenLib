package com.edgn.edml.core.component;

@FunctionalInterface
public interface ComponentFactory {
    EdmlComponent create();
}