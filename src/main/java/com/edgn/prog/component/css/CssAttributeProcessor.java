package com.edgn.prog.component.css;

import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Arrays;
import java.util.List;

public final class CssAttributeProcessor {
    private final CssRegistry cssRegistry;
    
    public CssAttributeProcessor(CssRegistry cssRegistry) {
        this.cssRegistry = cssRegistry;
    }
    
    public void processStyleAttribute(EdgnComponent component, String styleValue, MinecraftRenderContext context) {
        if (styleValue == null || styleValue.trim().isEmpty()) {
            return;
        }
        
        // Parse inline styles: "background-color: red; padding: 10px;"
        String[] declarations = styleValue.split(";");
        
        for (String declaration : declarations) {
            String[] parts = declaration.split(":", 2);
            if (parts.length == 2) {
                String property = parts[0].trim();
                String value = parts[1].trim();
                cssRegistry.applyCssRule(component, property, value, context);
            }
        }
    }
    
    public void processClassAttribute(EdgnComponent component, String classList,
                                      List<CssRule> cssRules, MinecraftRenderContext context) {
        if (classList == null || classList.trim().isEmpty()) {
            return;
        }
        
        String[] classes = classList.trim().split("\\s+");
        
        for (CssRule rule : cssRules) {
            if (matchesClassSelector(rule.selector(), classes)) {
                for (var entry : rule.declarations().entrySet()) {
                    cssRegistry.applyCssRule(component, entry.getKey(), entry.getValue(), context);
                }
            }
        }
    }
    
    private boolean matchesClassSelector(String selector, String[] componentClasses) {
        if (selector.startsWith(".")) {
            String className = selector.substring(1);
            return Arrays.asList(componentClasses).contains(className);
        }
        return false;
    }
}