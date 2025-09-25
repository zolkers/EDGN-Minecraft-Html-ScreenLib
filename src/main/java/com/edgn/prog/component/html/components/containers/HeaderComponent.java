package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.annotations.KeepEmpty;
import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.HtmlTag;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

public final class HeaderComponent extends CssAwareComponent {

    private static final Set<String> HEADER_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.ROLE.getProperty()
    );

    public HeaderComponent() {
        super(HtmlTag.HEADER.getTagName(), HEADER_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String role = getAttribute(TagAttribute.ROLE.getProperty(), "");
        if (role.equals(TagAttribute.ROLE_BANNER.getProperty()) &&
                !hasClass(TagAttribute.SITE_HEADER.getProperty())) {
            applySiteBannerBehavior();
        }
        applyHeaderTheme();
    }

    private void applySiteBannerBehavior() {
        if (backgroundColor == 0x00000000 && !hasClass(TagAttribute.CUSTOM_HEADER.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("#34495e"));
        }
    }

    private void applyHeaderTheme() {
        if (backgroundColor == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#2c3e50"));
            } else if (hasClass(TagAttribute.PRIMARY.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#3498db"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        // Le contenu est rendu par les enfants
    }
}