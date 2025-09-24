package com.edgn.prog.component.html;

import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.List;

public interface EdgnComponent {
    String getTagName();
    void render(MinecraftRenderContext context);
    void applyAttribute(String name, String value);
    void addChild(EdgnComponent child);
    List<EdgnComponent> getChildren();
}