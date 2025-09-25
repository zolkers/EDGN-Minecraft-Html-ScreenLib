package com.edgn.edml.component.html.components.containers;

import com.edgn.edml.annotations.KeepEmpty;
import com.edgn.edml.component.ClickableComponent;
import com.edgn.edml.component.attribute.TagAttribute;
import com.edgn.edml.component.html.EdmlComponent;
import com.edgn.edml.component.html.EdmlEnum;
import com.edgn.edml.component.html.components.EdssAwareComponent;
import com.edgn.edml.minecraft.MinecraftRenderContext;
import com.edgn.utils.ColorUtils;

import java.util.Set;

public final class BodyComponent extends EdssAwareComponent implements ClickableComponent {

    private static final Set<String> BODY_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(), TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(), TagAttribute.LOAD.getProperty(),
            TagAttribute.UNLOAD.getProperty(), TagAttribute.DATA_LAYOUT.getProperty(),
            TagAttribute.DATA_RESPONSIVE.getProperty(), TagAttribute.DATA_FULLSCREEN.getProperty()
    );

    private String layout = "flow";
    private boolean responsive = true;
    private boolean fullscreen = false;

    public BodyComponent() {
        super(EdmlEnum.BODY.getTagName(), BODY_ATTRIBUTES);
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        String onLoad = getAttribute(TagAttribute.LOAD.getProperty(), "");
        //TODO: onUnload
        layout = getAttribute(TagAttribute.DATA_LAYOUT.getProperty(), "flow");
        responsive = Boolean.parseBoolean(getAttribute(TagAttribute.DATA_RESPONSIVE.getProperty(), "true"));
        fullscreen = Boolean.parseBoolean(getAttribute(TagAttribute.DATA_FULLSCREEN.getProperty(), "false"));

        if (!onLoad.isEmpty()) {
            executeLoadEvent(onLoad);
        }

        if (getWidth() == 0) {
            setWidth(context.width());
        }

        applyBodyTheme();
    }

    private void executeLoadEvent(String loadScript) {
        // Logic pour exécuter les scripts onload
    }

    //TODO: should use ColorUtils instead for more meaningful usage
    private void applyBodyTheme() {
        if (getBackgroundColor() == 0x00000000) {
            if (hasClass(TagAttribute.DARK_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#1a1a1a"));
            } else if (hasClass(TagAttribute.LIGHT_THEME.getProperty())) {
                setBackgroundColor(ColorUtils.parseColor("#f5f5f5"));
            } else {
                setBackgroundColor(ColorUtils.parseColor("#f5f5f5"));
            }
        }
    }

    @Override
    @KeepEmpty
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {}

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        if (!isPointInBounds(mouseX, mouseY)) {
            return false;
        }

        for (EdmlComponent child : children) {
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
        return x >= getCalculatedX() && x < getCalculatedX() + getCalculatedWidth() &&
                y >= getCalculatedY() && y < getCalculatedY() + getCalculatedHeight();
    }

    public String getLayout() { return layout; }
    public boolean isResponsive() { return responsive; }
    public boolean isFullscreen() { return fullscreen; }

    public void onLoad() {
        String onLoadScript = getAttribute(TagAttribute.LOAD.getProperty(), "");
        if (!onLoadScript.isEmpty()) {
            executeLoadEvent(onLoadScript);
        }
    }

    public void onUnload() {
        String onUnloadScript = getAttribute(TagAttribute.UNLOAD.getProperty(), "");
        if (!onUnloadScript.isEmpty()) {
            // Logic pour onUnload
        }
    }
}