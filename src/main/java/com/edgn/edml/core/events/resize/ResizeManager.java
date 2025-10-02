package com.edgn.edml.core.events.resize;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton manager for handling window resize events.
 * Thread-safe implementation using CopyOnWriteArrayList.
 */
public final class ResizeManager {
    private static final ResizeManager INSTANCE = new ResizeManager();
    
    private final List<ResizeListener> listeners = new CopyOnWriteArrayList<>();
    private int currentWidth = 0;
    private int currentHeight = 0;
    private boolean initialized = false;
    
    private ResizeManager() {}
    
    public static ResizeManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Registers a listener for resize events
     * @param listener The listener to register
     */
    public void addListener(ResizeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    /**
     * Unregisters a listener
     * @param listener The listener to remove
     */
    public void removeListener(ResizeListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Updates the dimensions and notifies listeners if changed
     * @param newWidth New window width
     * @param newHeight New window height
     */
    public void updateDimensions(int newWidth, int newHeight) {
        if (!initialized) {
            currentWidth = newWidth;
            currentHeight = newHeight;
            initialized = true;
            return;
        }
        
        if (currentWidth != newWidth || currentHeight != newHeight) {
            ResizeEvent event = new ResizeEvent(
                currentWidth, currentHeight,
                newWidth, newHeight
            );
            
            currentWidth = newWidth;
            currentHeight = newHeight;
            
            notifyListeners(event);
        }
    }
    
    private void notifyListeners(ResizeEvent event) {
        for (ResizeListener listener : listeners) {
            try {
                listener.onResize(event);
            } catch (Exception e) {
                System.err.println("Error in resize listener: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Clears all registered listeners
     */
    public void clearListeners() {
        listeners.clear();
    }
    
    /**
     * Resets the manager state
     */
    public void reset() {
        clearListeners();
        currentWidth = 0;
        currentHeight = 0;
        initialized = false;
    }
    
    public int getCurrentWidth() {
        return currentWidth;
    }
    
    public int getCurrentHeight() {
        return currentHeight;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}