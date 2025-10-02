package com.edgn.edml.screen;

import com.edgn.devtools.DevToolsOverlay;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.core.events.ClickableComponent;
import com.edgn.edml.core.events.DraggableComponent;
import com.edgn.edml.core.events.resize.ResizableComponent;
import com.edgn.edml.core.rendering.context.RenderContext;
import com.edgn.edml.layout.ILayoutEngine;
import com.edgn.edml.ui.scroll.ScrollManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public abstract class EdmlScreen extends Screen {

    protected EdmlScreen(Text title) {
        super(title);
    }

    protected abstract EdmlComponent getRootComponent();
    protected abstract ILayoutEngine getLayoutEngine();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        EdmlComponent rootComponent = getRootComponent();

        if (rootComponent != null) {
            RenderContext renderContext = new RenderContext(context, this.width, this.height);

            if (rootComponent instanceof ResizableComponent resizable && resizable.needsLayout()) {
                getLayoutEngine().layoutComponent(rootComponent, renderContext);
                resizable.clearNeedsLayout();
            }

            rootComponent.render(renderContext);

            DevToolsOverlay.getInstance().render(context, this.width, this.height,
                    mouseX, mouseY, rootComponent);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_F12) {
            DevToolsOverlay.getInstance().toggle();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (DevToolsOverlay.getInstance().handleMouseClick(mouseX, mouseY, button, this.height)) {
            return true;
        }


        EdmlComponent rootComponent = getRootComponent();
        if (rootComponent instanceof ClickableComponent clickable) {
            return clickable.handleClick(mouseX, mouseY, button);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (DevToolsOverlay.getInstance().handleMouseDrag(mouseY, button)) {
            return true;
        }

        EdmlComponent rootComponent = getRootComponent();
        if (rootComponent instanceof DraggableComponent draggable) {
            return draggable.handleDrag(mouseX, mouseY);
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (DevToolsOverlay.getInstance().handleMouseRelease(button)) {
            return true;
        }

        EdmlComponent rootComponent = getRootComponent();
        if (rootComponent instanceof DraggableComponent draggable) {
            draggable.handleRelease();
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (DevToolsOverlay.getInstance().handleScroll(mouseX, mouseY, verticalAmount, this.height)) {
            return true;
        }

        boolean handled = ScrollManager.getInstance().handleScrollEvent(mouseX, mouseY, horizontalAmount, verticalAmount);
        if (handled) {
            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void close() {
        DevToolsOverlay.getInstance().deactivate();
        super.close();
    }
}