package com.edgn.edml.core.events.action;

@FunctionalInterface
public interface ActionHandler {
    void handle(ActionEvent event);
}