package com.edgn.prog.minecraft.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public abstract class EdgnScreen extends Screen {

    protected EdgnScreen(Text title) {
        super(title);
    }

    protected EdgnScreen(Text title, String htmlResourcePath, String cssResourcePath) {
        super(title);
    }
}