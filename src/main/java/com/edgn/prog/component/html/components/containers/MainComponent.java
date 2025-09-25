package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.annotations.KeepEmpty;
import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.HtmlTag;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class MainComponent extends CssAwareComponent {

    private static final Set<String> MAIN_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.ROLE.getProperty()
    );

    public MainComponent() {
        super(HtmlTag.MAIN.getTagName(), MAIN_ATTRIBUTES);
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}
}