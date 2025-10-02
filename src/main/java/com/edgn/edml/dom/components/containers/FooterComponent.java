package com.edgn.edml.dom.components.containers;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;

import java.util.Set;

public final class FooterComponent extends BaseContainer {

    private static final Set<String> FOOTER_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.ROLE.getProperty()
    );

    public FooterComponent() {
        super(EdmlEnum.FOOTER.getTagName(), FOOTER_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String role = getAttribute(TagAttribute.ROLE.getProperty(), "");
        if (role.equals(TagAttribute.ROLE_CONTENTINFO.getProperty()) &&
                !hasClass(TagAttribute.SITE_FOOTER.getProperty())) {
            applySiteFooterBehavior();
        }
        applyFooterTheme();
    }

    private void applySiteFooterBehavior() {
        if (backgroundColor == 0x00000000 && !hasClass(TagAttribute.CUSTOM_FOOTER.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("#95a5a6"));
        }
    }

    private void applyFooterTheme() {
        if (backgroundColor == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#34495e"));
            } else if (hasClass(TagAttribute.INFO.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#5bc0de"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}
}