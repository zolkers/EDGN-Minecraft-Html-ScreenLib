package com.edgn.edml.component.edml.component;

import com.edgn.edml.minecraft.MinecraftRenderContext;

import java.util.List;

public interface EdmlComponent {
    String getTagName();
    void render(MinecraftRenderContext context);
    void applyAttribute(String name, String value);
    void addChild(EdmlComponent child);
    List<EdmlComponent> getChildren();
    String getAttribute(String name, String defaultValue);

}