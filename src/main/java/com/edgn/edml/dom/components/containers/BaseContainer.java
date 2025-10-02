package com.edgn.edml.dom.components.containers;

import com.edgn.HTMLMyScreen;
import com.edgn.edml.core.events.ClickableComponent;
import com.edgn.edml.core.events.DraggableComponent;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.dom.components.EdssAwareComponent;

import java.util.Set;

public abstract class BaseContainer extends EdssAwareComponent implements ClickableComponent, DraggableComponent {

    protected BaseContainer(String tagName, Set<String> validAttributes) {
        super(tagName, validAttributes);
    }

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
        return false;
    }

    @Override
    public boolean isPointInBounds(double x, double y) {
        return x >= getCalculatedX() && x < getCalculatedX() + getCalculatedWidth() &&
                y >= getCalculatedY() && y < getCalculatedY() + getCalculatedHeight();
    }

    @Override
    public boolean handleDrag(double mouseX, double mouseY) {
        for (EdmlComponent child : children) {
            if (child instanceof DraggableComponent draggable) {
                boolean handled = draggable.handleDrag(mouseX, mouseY);
                if (handled) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void handleRelease() {
        for (EdmlComponent child : children) {
            if (child instanceof DraggableComponent draggable) {
                draggable.handleRelease();
            }
        }
    }
}