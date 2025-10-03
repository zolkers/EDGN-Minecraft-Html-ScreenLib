package com.edgn.edml.dom.components.containers;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;

import java.util.Set;

public final class AsideComponent extends BaseContainer {

    private static final Set<String> ASIDE_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(),
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.ROLE.getProperty()
    );

    public AsideComponent() {
        super(EdmlEnum.ASIDE.getTagName(), ASIDE_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String role = getAttribute(TagAttribute.ROLE.getProperty(), "");
        if (role.equals(TagAttribute.ROLE_COMPLEMENTARY.getProperty()) || role.isEmpty()) {
            applyComplementaryBehavior();
        }
    }

    private void applyComplementaryBehavior() {
        if (backgroundColor == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#34495e"));
            } else {
                setBackgroundColor(ColorUtils.parseColor("#f0f0f0"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}
}