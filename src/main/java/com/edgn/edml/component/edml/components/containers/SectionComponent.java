package com.edgn.edml.component.edml.components.containers;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.edml.components.EdssAwareComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

public final class SectionComponent extends EdssAwareComponent {

    private static final Set<String> SECTION_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), 
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), 
            TagAttribute.ROLE.getProperty(),
            TagAttribute.DATA_TEXT.getProperty(),
            TagAttribute.DATA_TOOLTIP.getProperty(),
            TagAttribute.DATA_THEME.getProperty()
    );

    private String role = "";
    private String tooltip = "";
    private String theme = TagAttribute.THEME_DEFAULT.getProperty();

    public SectionComponent() {
        super("section", SECTION_ATTRIBUTES);
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
            case "region" -> applyRegionBehavior();
            case "banner" -> applyBannerBehavior();
            case "navigation" -> applyNavigationBehavior();
            case "main" -> applyMainBehavior();
            case "complementary" -> applyComplementaryBehavior();
            case "contentinfo" -> applyContentinfoBehavior();
            case "search" -> applySearchBehavior();
            case "form" -> applyFormBehavior();
            default -> applyDefaultSectionBehavior();
        }
    }

    private void applyRegionBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#f8f9fa"));
        }
    }

    private void applyBannerBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#343a40"));
        }
    }

    private void applyNavigationBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#495057"));
        }
    }

    private void applyMainBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#ffffff"));
        }
    }

    private void applyComplementaryBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#e9ecef"));
        }
    }

    private void applyContentinfoBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#6c757d"));
        }
    }

    private void applySearchBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#fff3cd"));
        }
    }

    private void applyFormBehavior() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#f8f9fa"));
        }
    }

    private void applyDefaultSectionBehavior() {
        if (backgroundColor == 0x00000000 && !hasClass(TagAttribute.CUSTOM_MAIN.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("transparent"));
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
        } else if (hasClass(TagAttribute.WARNING.getProperty())) {
            theme = "warning";
        } else if (hasClass(TagAttribute.INFO.getProperty())) {
            theme = "info";
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
                case "warning" -> setBackgroundColor(ColorUtils.parseColor("#f39c12"));
                case "info" -> setBackgroundColor(ColorUtils.parseColor("#17a2b8"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        renderTooltip(context, x, y);
    }

    private void renderTooltip(MinecraftRenderContext context, int x, int y) {
        if (!tooltip.isEmpty() && shouldShowTooltip()) {
            context.drawText(tooltip, x, y - 20, ColorUtils.parseColor("white"));
        }
    }

    private boolean shouldShowTooltip() {
        return false;
    }

    @Override
    protected void onViewportStatusChanged(boolean inViewport) {
        if (hasClass(TagAttribute.ANIMATED.getProperty())) {
            if (inViewport) {
                startSectionAnimations();
            } else {
                pauseSectionAnimations();
            }
        }
    }

    private void startSectionAnimations() {
        // Section-specific animation logic
    }

    private void pauseSectionAnimations() {
        // Section-specific pause logic
    }

    public String getRole() {
        return role;
    }

    public String getTooltip() {
        return tooltip;
    }

    public String getTheme() {
        return theme;
    }

    public boolean isLandmarkSection() {
        return switch (role) {
            case "banner", "navigation", "main", "complementary", "contentinfo", "search" -> true;
            default -> false;
        };
    }

    public boolean isContentSection() {
        return switch (role) {
            case "region", "main", "form" -> true;
            default -> role.isEmpty();
        };
    }
}