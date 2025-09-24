package com.edgn.prog.parser.units;

import com.edgn.prog.minecraft.MinecraftRenderContext;

public final class CssUnitParser {
    
    public static int parseSize(String value, int containerWidth, int containerHeight, MinecraftRenderContext context) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        
        String trimmed = value.trim().toLowerCase();
        
        if (trimmed.endsWith(Unit.PIXEL.getSymbol())) {
            return Integer.parseInt(trimmed.replace(Unit.PIXEL.getSymbol(), ""));
        }
        
        if (trimmed.endsWith(Unit.PERCENTAGE.getSymbol())) {
            int percent = Integer.parseInt(trimmed.replace(Unit.PERCENTAGE.getSymbol(), ""));
            return (containerWidth * percent) / 100;
        }
        
        if (trimmed.endsWith(Unit.VIEWPORT_WIDTH.getSymbol())) {
            int vw = Integer.parseInt(trimmed.replace(Unit.VIEWPORT_WIDTH.getSymbol(), ""));
            return (context.width() * vw) / 100;
        }
        
        if (trimmed.endsWith(Unit.VIEWPORT_HEIGHT.getSymbol())) {
            int vh = Integer.parseInt(trimmed.replace(Unit.VIEWPORT_HEIGHT.getSymbol(), ""));
            return (context.height() * vh) / 100;
        }
        
        // No unit = pixels
        try {
            return Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public static boolean isValidUnit(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = value.trim().toLowerCase();
        return trimmed.endsWith("px") || 
               trimmed.endsWith("%") || 
               trimmed.endsWith("vw") || 
               trimmed.endsWith("vh") || 
               trimmed.matches("\\d+");
    }
}