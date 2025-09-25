package com.edgn.edml.data.binding;

import com.edgn.edml.data.BindingContext;
import com.edgn.edml.data.DataSource;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ReactiveBindingContext implements BindingContext {
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private final Map<String, DataSource<?>> dataSources = new HashMap<>();
    
    @Override
    public Object getValue(String path) {
        if (path.contains("[")) {
            return getArrayValue(path);
        }
        
        String[] parts = path.split("\\.");
        Object current = data;
        
        for (String part : parts) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(part);
            } else {
                current = getReflectionValue(current, part);
            }
            if (current == null) break;
        }
        
        return current;
    }
    
    @Override
    public void setValue(String path, Object newValue) {
        Object oldValue = getValue(path);
        setValueInternal(path, newValue);
        
        // Notification réactive
        changeSupport.firePropertyChange(path, oldValue, newValue);
    }
    
    @Override
    public void bindProperty(String path, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(path, listener);
    }
    
    @Override
    public void unbindProperty(String path, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(path, listener);
    }
    
    // Gestion des DataSources pour les grandes collections
    public void registerDataSource(String path, DataSource<?> source) {
        dataSources.put(path, source);
    }
    
    public DataSource<?> getDataSource(String path) {
        return dataSources.get(path);
    }
    
    private Object getArrayValue(String path) {
        // Gestion des chemins comme "users[5].name" 
        // Implémentation pour les indices de tableaux
        return null; // Placeholder
    }
    
    private void setValueInternal(String path, Object value) {
        data.put(path, value);
    }
    
    private Object getReflectionValue(Object obj, String fieldName) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            return null;
        }
    }
}