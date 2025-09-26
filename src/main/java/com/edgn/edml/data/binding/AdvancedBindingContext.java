package com.edgn.edml.data.binding;

import com.edgn.edml.data.BindingContext;
import com.edgn.edml.data.collections.ObservableList;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AdvancedBindingContext implements BindingContext {
    private final Map<String, Object> data = new ConcurrentHashMap<>();
    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private final Map<String, ObservableList<?>> observableLists = new HashMap<>();
    
    @Override
    public Object getValue(String path) {
        return data.get(path);
    }
    
    @Override
    public void setValue(String path, Object newValue) {
        Object oldValue = getValue(path);
        data.put(path, newValue);
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
    
    public void registerObservableList(String path, ObservableList<?> list) {
        observableLists.put(path, list);
        data.put(path, list);
    }
    
    @SuppressWarnings("unchecked")
    public <T> ObservableList<T> getObservableList(String path) {
        return (ObservableList<T>) observableLists.get(path);
    }
    
    public <T> ObservableList<T> createObservableList(String path) {
        ObservableList<T> list = new ObservableList<>();
        registerObservableList(path, list);
        return list;
    }
    
    public ObservableList<String> createLargeTestList(String path, int size) {
        ObservableList<String> list = createObservableList(path);
        
        List<String> items = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            items.add("Element " + (i + 1));
        }
        
        list.addRange(items);
        return list;
    }
}