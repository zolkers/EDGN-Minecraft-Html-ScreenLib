package com.edgn.edml.component.edss.property;

import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.component.AbstractEdmlComponent;
import com.edgn.edml.component.edml.component.EdmlComponent;

public final class EdssSelector {
    
    public static boolean matches(String selector, EdmlComponent component) {
        selector = selector.trim();
        
        if (selector.matches("^[a-zA-Z][a-zA-Z0-9-]*$")) {
            return component.getTagName().equals(selector);
        }
        
        if (selector.startsWith(".")) {
            String className = selector.substring(1);
            return hasClass(component, className);
        }
        
        if (selector.startsWith("#")) {
            String id = selector.substring(1);
            return hasId(component, id);
        }
        
        return false;
    }
    
    private static boolean hasClass(EdmlComponent component, String className) {
        if (component instanceof AbstractEdmlComponent abstractComp) {
            return abstractComp.hasClass(className);
        }
        return false;
    }
    
    private static boolean hasId(EdmlComponent component, String id) {
        if (component instanceof AbstractEdmlComponent abstractComp) {
            String componentId = abstractComp.getAttribute(TagAttribute.ID.getProperty(), "");
            return componentId.equals(id);
        }
        return false;
    }
}