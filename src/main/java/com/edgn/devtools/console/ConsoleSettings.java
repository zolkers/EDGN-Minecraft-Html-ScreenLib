package com.edgn.devtools.console;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.component.registry.ComponentRegistry;
import com.edgn.edml.core.component.registry.EdmlComponentRegistry;

import java.util.*;

import static java.awt.Color.HSBtoRGB;

public final class ConsoleSettings {
    private int consoleHeight = 300;
    private float opacity = 0.95f;
    private boolean showLineNumbers = true;
    private int fontSize = 12;
    private boolean debugBoxesEnabled = false;
    private final Map<String, Integer> componentColors = new HashMap<>();

    public ConsoleSettings() {
        discoverComponents();
    }

    private void discoverComponents() {
        ComponentRegistry registry = EdmlComponentRegistry.getInstance();
        Set<String> registeredTags = registry.getRegisteredTags();

        List<String> sortedTags = new ArrayList<>(registeredTags);
        Collections.sort(sortedTags);

        for (int i = 0; i < sortedTags.size(); i++) {
            String tag = sortedTags.get(i);
            int color = generateColorFromIndex(i, sortedTags.size());
            componentColors.put(tag, color);
        }

        HTMLMyScreen.LOGGER.info("Registered {} component colors", componentColors.size());
    }

    private int generateColorFromIndex(int index, int total) {
        float hue = (float) index / total;
        float saturation = 0.85f;
        float brightness = 0.95f;

        int rgb = HSBtoRGB(hue, saturation, brightness);
        return 0xFF000000 | rgb;
    }

    public int getColorForComponent(EdmlComponent component) {
        String tagName = component.getTagName().toLowerCase();

        return componentColors.computeIfAbsent(tagName, tag -> {
            int hash = tag.hashCode();
            float hue = ((hash & 0xFFFF) % 360) / 360.0f;
            int rgb = HSBtoRGB(hue, 0.85f, 0.95f);
            return 0xFF000000 | rgb;
        });
    }

    public void registerComponentColor(String tagName, int color) {
        componentColors.put(tagName.toLowerCase(), color);
    }

    public Map<String, Integer> getAllRegisteredColors() {
        return new HashMap<>(componentColors);
    }

    public void refreshComponents() {
        componentColors.clear();
        discoverComponents();
    }

    public int getConsoleHeight() {
        return consoleHeight;
    }

    public void setConsoleHeight(int height) {
        this.consoleHeight = Math.clamp(height, 100, 600);
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.clamp(opacity, 0.3f, 1.0f);
    }

    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    public void setShowLineNumbers(boolean show) {
        this.showLineNumbers = show;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int size) {
        this.fontSize = Math.clamp(size, 8, 20);
    }

    public boolean isDebugBoxesEnabled() {
        return debugBoxesEnabled;
    }

    public void setDebugBoxesEnabled(boolean enabled) {
        this.debugBoxesEnabled = enabled;
    }
}