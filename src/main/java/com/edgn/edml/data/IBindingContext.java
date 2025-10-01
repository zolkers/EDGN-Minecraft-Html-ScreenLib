package com.edgn.edml.data;

import java.beans.PropertyChangeListener;

public interface IBindingContext {
    Object getValue(String path);
    void setValue(String path, Object value);
    void bindProperty(String path, PropertyChangeListener listener);
    void unbindProperty(String path, PropertyChangeListener listener);
}