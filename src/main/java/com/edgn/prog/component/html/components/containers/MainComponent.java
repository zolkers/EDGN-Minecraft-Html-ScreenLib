package com.edgn.prog.component.html.components.containers;

import com.edgn.prog.annotations.KeepEmpty;
import com.edgn.prog.component.attribute.TagAttribute;
import com.edgn.prog.component.html.HtmlTag;
import com.edgn.prog.component.html.components.CssAwareComponent;
import com.edgn.prog.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

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
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        // Le contenu est rendu par les enfants
    }
}