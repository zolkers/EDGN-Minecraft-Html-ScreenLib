package com.edgn.edml.dom.components.containers;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;

import java.util.Set;

public final class MainComponent extends BaseContainer {

    private static final Set<String> MAIN_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.ROLE.getProperty()
    );

    public MainComponent() {
        super(EdmlEnum.MAIN.getTagName(), MAIN_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String role = getAttribute(TagAttribute.ROLE.getProperty(), "");
        if (role.equals(TagAttribute.ROLE_MAIN.getProperty()) || role.isEmpty()) {
            applyMainContentBehavior();
        }
        applyMainTheme();
    }

    private void applyMainContentBehavior() {
        if (backgroundColor == 0x00000000 && !hasClass(TagAttribute.CUSTOM_MAIN.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("white"));
        }
    }

    private void applyMainTheme() {
        if (backgroundColor == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#2c3e50"));
            } else if (hasClass(TagAttribute.LIGHT_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#ffffff"));
            } else if (hasClass(TagAttribute.PRIMARY.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#ecf0f1"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}
}