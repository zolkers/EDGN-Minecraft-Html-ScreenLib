package com.edgn.prog.component.css;

import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.AbstractEdgnComponent;
import com.edgn.prog.component.html.EdgnComponent;

public final class CssSelector {
    
    public static boolean matches(String selector, EdgnComponent component) {
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
    
    private static boolean hasClass(EdgnComponent component, String className) {
        if (component instanceof AbstractEdgnComponent abstractComp) {
            return abstractComp.hasClass(className);
        }
        return false;
    }
    
    private static boolean hasId(EdgnComponent component, String id) {
        if (component instanceof AbstractEdgnComponent abstractComp) {
            String componentId = abstractComp.getAttribute(TagAttribute.ID.getProperty(), "");
            return componentId.equals(id);
        }
        return false;
    }
}