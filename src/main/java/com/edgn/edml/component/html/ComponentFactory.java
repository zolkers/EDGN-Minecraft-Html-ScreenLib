package com.edgn.edml.component.html;

@FunctionalInterface
public interface ComponentFactory {
    EdmlComponent create();
}