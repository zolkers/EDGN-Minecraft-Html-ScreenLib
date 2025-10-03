package com.edgn.edml.core.events.action;

import com.edgn.edml.core.component.EdmlComponent;

public record ActionEvent(
    EdmlComponent source,
    String actionName,
    Object data
) {}