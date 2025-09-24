package com.edgn.prog.component.html;

@FunctionalInterface
public interface ComponentFactory {
    EdgnComponent create();
}