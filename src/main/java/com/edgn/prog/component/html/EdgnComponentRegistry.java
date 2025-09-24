package com.edgn.prog.component.html;

import com.edgn.prog.component.html.components.containers.*;
import com.edgn.prog.exceptions.ComponentCreationException;

import java.util.Set;

public final class EdgnComponentRegistry implements ComponentRegistry {
    private static final ComponentRegistry INSTANCE = createComponentRegistry();

    public static ComponentRegistry getInstance() {
        return INSTANCE;
    }

    private static ComponentRegistry createComponentRegistry() {
        DefaultComponentRegistry registry = new DefaultComponentRegistry();

        registry.registerComponent("body", BodyComponent::new);
        registry.registerComponent("div", DivComponent::new);
        registry.registerComponent("header", HeaderComponent::new);
        registry.registerComponent("main", MainComponent::new);
        registry.registerComponent("footer", FooterComponent::new);

        return registry;
    }

    @Override
    public void registerComponent(String tagName, ComponentFactory factory) {
        INSTANCE.registerComponent(tagName, factory);
    }

    @Override
    public EdgnComponent createComponent(String tagName) throws ComponentCreationException {
        return INSTANCE.createComponent(tagName);
    }

    @Override
    public Set<String> getRegisteredTags() {
        return INSTANCE.getRegisteredTags();
    }

    @Override
    public boolean isTagRegistered(String tagName) {
        return INSTANCE.isTagRegistered(tagName);
    }
}