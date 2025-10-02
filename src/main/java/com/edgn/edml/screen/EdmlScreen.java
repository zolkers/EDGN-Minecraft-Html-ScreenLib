package com.edgn.edml.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class EdmlScreen extends Screen {

    protected EdmlScreen(Text title) {
        super(title);
    }

    protected EdmlScreen(Text title, String htmlResourcePath, String cssResourcePath) {
        super(title);
    }
}