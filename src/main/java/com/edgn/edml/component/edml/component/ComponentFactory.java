package com.edgn.edml.component.edml.component;

@FunctionalInterface
public interface ComponentFactory {
    EdmlComponent create();
}