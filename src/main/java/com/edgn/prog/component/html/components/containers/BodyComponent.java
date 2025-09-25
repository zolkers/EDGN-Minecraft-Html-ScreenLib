package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.component.ClickableComponent;
import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.EdgnComponent;
import com.edgn.prog.component.html.HtmlTag;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;

import java.util.Set;

public final class BodyComponent extends CssAwareComponent implements ClickableComponent {

    private static final Set<String> BODY_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.LOAD.getProperty(), TagAttribute.UNLOAD.getProperty()
    );

    public BodyComponent() {
        super(HtmlTag.BODY.getTagName(), BODY_ATTRIBUTES);
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        if (!isPointInBounds(mouseX, mouseY)) {
            return false;
        }

        for (EdgnComponent child : children) {
            if (child instanceof ClickableComponent clickable) {
                if (clickable.handleClick(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isPointInBounds(double x, double y) {
        return x >= calculatedX && x < calculatedX + calculatedWidth &&
                y >= calculatedY && y < calculatedY + calculatedHeight;
    }
}