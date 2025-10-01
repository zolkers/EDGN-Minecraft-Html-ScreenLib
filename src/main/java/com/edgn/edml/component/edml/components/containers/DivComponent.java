package com.edgn.edml.component.edml.components.containers;

import com.edgn.edml.component.TextCapableComponent;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.EdmlEnum;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

public final class DivComponent extends BaseContainer implements TextCapableComponent {
    private static final Set<String> DIV_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.ROLE.getProperty(), TagAttribute.TITLE.getProperty(),
            TagAttribute.SUBTITLE.getProperty(), TagAttribute.DATA_TEXT.getProperty(),
            TagAttribute.DATA_TOOLTIP.getProperty(), TagAttribute.DATA_THEME.getProperty(),
            TagAttribute.DATA_ANIMATION.getProperty(), TagAttribute.DATA_DELAY.getProperty()
    );

    private String tooltip = "";
    private String role = "";
    private String theme = TagAttribute.THEME_DEFAULT.getProperty();
    private int textColor = ColorUtils.parseColor("black");

    public DivComponent() {
        super(EdmlEnum.DIV.getTagName(), DIV_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        role = getAttribute(TagAttribute.ROLE.getProperty(), "");
        tooltip = getAttribute(TagAttribute.DATA_TOOLTIP.getProperty(), "");
        theme = getAttribute(TagAttribute.DATA_THEME.getProperty(), TagAttribute.THEME_DEFAULT.getProperty());

        applyRoleSpecificBehavior();
        applyThemeFromClasses();
    }

    private void applyRoleSpecificBehavior() {
        switch (role) {
            case "banner" -> {
                if (!hasClass(TagAttribute.BANNER.getProperty())) {
                    // Comportement spécifique aux bannières
                }
            }
            case "navigation" -> {
                if (!hasClass(TagAttribute.NAV.getProperty())) {
                    // Comportement spécifique à la navigation
                }
            }
            case "main" -> {
                if (!hasClass(TagAttribute.MAIN_CONTENT.getProperty())) {
                    // Comportement spécifique au contenu principal
                }
            }
            case "complementary" -> {
                if (!hasClass(TagAttribute.SIDEBAR.getProperty())) {
                    // Comportement spécifique aux sidebars
                }
            }
        }
    }

    private void applyThemeFromClasses() {
        if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
            theme = TagAttribute.THEME_DARK.getProperty();
        } else if (hasClass(TagAttribute.LIGHT_THEME.getProperty())) {
            theme = TagAttribute.THEME_LIGHT.getProperty();
        } else if (hasClass(TagAttribute.PRIMARY.getProperty())) {
            theme = TagAttribute.THEME_PRIMARY.getProperty();
        } else if (hasClass(TagAttribute.SUCCESS.getProperty())) {
            theme = TagAttribute.THEME_SUCCESS.getProperty();
        } else if (hasClass(TagAttribute.DANGER.getProperty())) {
            theme = TagAttribute.THEME_DANGER.getProperty();
        }

        applyTheme();
    }

    private void applyTheme() {
        if (backgroundColor == 0x00000000) {
            switch (theme) {
                case "dark" -> setBackgroundColor(ColorUtils.parseColor("#2c3e50"));
                case "light" -> setBackgroundColor(ColorUtils.parseColor("#f8f9fa"));
                case "primary" -> setBackgroundColor(ColorUtils.parseColor("#3498db"));
                case "success" -> setBackgroundColor(ColorUtils.parseColor("#2ecc71"));
                case "danger" -> setBackgroundColor(ColorUtils.parseColor("#e74c3c"));
            }
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        renderText(context, x, y, height);
        renderTooltip(context, x, y);
    }

    private void renderText(MinecraftRenderContext context, int x, int y, int height) {
        String textContent = getTextContent();
        if (!textContent.isEmpty()) {
            int textY = y + (height - 10) / 2;
            context.drawText(textContent, x + 5, textY, textColor);
        }
    }

    private void renderTooltip(MinecraftRenderContext context, int x, int y) {
        if (!tooltip.isEmpty() && shouldShowTooltip()) {
            context.drawText(tooltip, x, y - 20, ColorUtils.parseColor("white"));
        }
    }

    private boolean shouldShowTooltip() {
        return false;
    }

    private String getTextContent() {
        return getAttribute(TagAttribute.DATA_TEXT.getProperty(), "");
    }

    @Override
    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public boolean hasTextContent() {
        String text = getAttribute(TagAttribute.DATA_TEXT.getProperty(), "");
        return !text.isEmpty();
    }

    @Override
    protected void onViewportStatusChanged(boolean inViewport) {
        if (hasClass(TagAttribute.ANIMATED.getProperty())) {
            if (inViewport) {
                startAnimations();
            } else {
                pauseAnimations();
            }
        }
    }

    private void startAnimations() {
        // Animation logic
    }

    private void pauseAnimations() {
        // Pause logic
    }
}