package com.edgn.edml.data;

import java.util.Map;

public interface BindableComponent {
    void updateFromBinding(String property, Object value);
    Map<String, String> getBindingAttributes();
}