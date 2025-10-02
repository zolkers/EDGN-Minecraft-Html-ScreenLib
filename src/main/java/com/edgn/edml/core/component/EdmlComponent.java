package com.edgn.edml.core.component;

import com.edgn.edml.core.rendering.MinecraftRenderContext;

import java.util.List;

public interface EdmlComponent {
    String getTagName();
    void render(MinecraftRenderContext context);
    void applyAttribute(String name, String value);
    void addChild(EdmlComponent child);
    List<EdmlComponent> getChildren();
    String getAttribute(String name, String defaultValue);

}