package com.edgn.edml.dom.components.elements;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.core.events.action.ActionEvent;
import com.edgn.edml.core.events.action.ActionRegistry;
import com.edgn.edml.dom.components.attributes.TagAttribute;
import com.edgn.edml.dom.components.attributes.TextCapableComponent;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.dom.components.EdssAwareComponent;
import com.edgn.edml.core.events.ClickableComponent;
import com.edgn.edml.core.rendering.MinecraftRenderContext;
import com.edgn.edml.utils.ColorUtils;
import net.minecraft.client.MinecraftClient;

import java.util.Set;
import java.util.function.Consumer;

public final class ButtonComponent extends EdssAwareComponent implements TextCapableComponent, ClickableComponent {

    private static final Set<String> BUTTON_ATTRIBUTES = Set.of(
            TagAttribute.CLASS.getProperty(),
            TagAttribute.ID.getProperty(),
            TagAttribute.STYLE.getProperty(),
            TagAttribute.DATA_TEXT.getProperty(),
            TagAttribute.DATA_CLICK.getProperty(),
            TagAttribute.CLICK.getProperty()
    );

    private String text = "";
    private int textColor = 0xFFFFFFFF;
    private int hoverColor = 0xFF5599FF;
    private int pressedColor = 0xFF3377DD;
    private boolean isHovered = false;
    private boolean isPressed = false;

    private Consumer<ButtonComponent> clickHandler;
    private String clickAction = "";

    public ButtonComponent() {
        super(EdmlEnum.BUTTON.getTagName(), BUTTON_ATTRIBUTES);
        setDefaultStyles();
    }

    private void setDefaultStyles() {
        if (backgroundColor == 0x00000000) {
            setBackgroundColor(ColorUtils.parseColor("#4CAF50"));
        }
    }

    @Override
    protected void processSpecificAttributes(MinecraftRenderContext context) {
        text = getAttribute(TagAttribute.DATA_TEXT.getProperty(), "Button");
        clickAction = getAttribute(TagAttribute.DATA_CLICK.getProperty(), "");

        if (clickAction.isEmpty()) {
            clickAction = getAttribute(TagAttribute.CLICK.getProperty(), "");
        }

        if (hasClass(TagAttribute.PRIMARY.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("#2196F3"));
        } else if (hasClass(TagAttribute.SUCCESS.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("#4CAF50"));
        } else if (hasClass(TagAttribute.DANGER.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("#f44336"));
        } else if (hasClass(TagAttribute.WARNING.getProperty())) {
            setBackgroundColor(ColorUtils.parseColor("#ff9800"));
        }
    }

    @Override
    protected void renderContent(MinecraftRenderContext context, int x, int y, int width, int height) {
        int bgColor = backgroundColor;

        if (isPressed) {
            bgColor = adjustColor(bgColor, -40);
        } else if (isHovered) {
            bgColor = adjustColor(bgColor, 20);
        }

        context.drawRect(0, 0, width, height, bgColor);

        MinecraftClient client = MinecraftClient.getInstance();
        int textWidth = client.textRenderer.getWidth(text);
        int textX = (width - textWidth) / 2;
        int textY = (height - 10) / 2;

        context.drawText(text, textX, textY, textColor);
    }

    private int adjustColor(int color, int amount) {
        int a = (color >> 24) & 0xFF;
        int r = Math.clamp(((color >> 16) & 0xFF) + amount, 0, 255);
        int g = Math.clamp(((color >> 8) & 0xFF) + amount, 0, 255);
        int b = Math.clamp((color & 0xFF) + amount, 0, 255);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    @Override
    public boolean handleClick(double mouseX, double mouseY, int button) {
        if (!isPointInBounds(mouseX, mouseY) || button != 0) {
            return false;
        }

        isPressed = true;

        HTMLMyScreen.LOGGER.info("Button '{}' clicked!", text);

        if (clickHandler != null) {
            clickHandler.accept(this);
        }

        if (!clickAction.isEmpty()) {
            ActionEvent event = new ActionEvent(this, clickAction, null);
            ActionRegistry.getInstance().executeAction(event);
        }

        scheduleRelease();
        return true;
    }

    private void scheduleRelease() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
                isPressed = false;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    @Override
    public boolean isPointInBounds(double x, double y) {
        int btnX = getCalculatedX();
        int btnY = getCalculatedY();
        int btnWidth = getCalculatedWidth();
        int btnHeight = getCalculatedHeight();

        return x >= btnX && x < btnX + btnWidth &&
                y >= btnY && y < btnY + btnHeight;
    }

    public void updateHoverState(double mouseX, double mouseY) {
        isHovered = isPointInBounds(mouseX, mouseY);
    }

    public void setClickHandler(Consumer<ButtonComponent> handler) {
        this.clickHandler = handler;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getClickAction() {
        return clickAction;
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
        return !text.isEmpty();
    }
}