package com.edgn.edml.core.events.action;

import com.edgn.HTMLMyScreen;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ActionRegistry {
    private static final ActionRegistry INSTANCE = new ActionRegistry();
    
    private final Map<String, ActionHandler> handlers = new ConcurrentHashMap<>();
    
    private ActionRegistry() {}
    
    public static ActionRegistry getInstance() {
        return INSTANCE;
    }
    
    public void registerAction(String actionName, ActionHandler handler) {
        handlers.put(actionName.toLowerCase(), handler);
        HTMLMyScreen.LOGGER.debug("Registered action handler: {}", actionName);
    }
    
    public void unregisterAction(String actionName) {
        handlers.remove(actionName.toLowerCase());
    }
    
    public boolean executeAction(ActionEvent event) {
        ActionHandler handler = handlers.get(event.actionName().toLowerCase());
        
        if (handler != null) {
            try {
                handler.handle(event);
                HTMLMyScreen.LOGGER.info("Executed action: {}", event.actionName());
                return true;
            } catch (Exception e) {
                HTMLMyScreen.LOGGER.error("Error executing action '{}': {}", 
                    event.actionName(), e.getMessage(), e);
            }
        } else {
            HTMLMyScreen.LOGGER.warn("No handler registered for action: {}", event.actionName());
        }
        
        return false;
    }
    
    public boolean hasAction(String actionName) {
        return handlers.containsKey(actionName.toLowerCase());
    }
    
    public void clear() {
        handlers.clear();
    }
}