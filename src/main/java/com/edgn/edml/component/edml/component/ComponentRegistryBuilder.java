package com.edgn.edml.component.edml.component;

import com.edgn.edml.component.edml.EdmlEnum;
import com.edgn.edml.component.edml.components.containers.*;
import com.edgn.edml.component.edml.components.elements.ImageComponent;
import com.edgn.edml.component.edml.components.elements.TextComponent;

public final class ComponentRegistryBuilder {

    private final DefaultComponentRegistry registry = new DefaultComponentRegistry();

    public ComponentRegistryBuilder() {}

    public ComponentRegistryBuilder registerComponent(ComponentFactory factory) {
        EdmlComponent tempComponent = factory.create();
        String tagName = tempComponent.getTagName();
        registry.registerComponent(tagName, factory);
        return this;
    }

    public ComponentRegistryBuilder registerComponents(ComponentFactory... factories) {
        for (ComponentFactory factory : factories) {
            registerComponent(factory);
        }
        return this;
    }

    public ComponentRegistryBuilder registerStandardContainers() {
        return registerComponents(
                BodyComponent::new,
                DivComponent::new,
                HeaderComponent::new,
                MainComponent::new,
                FooterComponent::new,
                ImageComponent::new,
                TextComponent::new,
                SectionComponent::new

        );
    }

    public ComponentRegistryBuilder registerTextComponents() {
        return this;
    }

    public ComponentRegistryBuilder registerCustomComponent(String tagName, ComponentFactory factory) {
        registry.registerComponent(tagName, factory);
        return this;
    }
    public ComponentRegistryBuilder registerCustomComponent(EdmlEnum tag, ComponentFactory factory) {
        registry.registerComponent(tag.getTagName(), factory);
        return this;
    }

    public ComponentRegistry build() {
        return registry;
    }

    public static ComponentRegistry createMinimal() {
        return new ComponentRegistryBuilder()
                .registerStandardContainers()
                .build();
    }

    public static ComponentRegistry createDefault() {
        return new ComponentRegistryBuilder()
                .registerStandardContainers()
                .registerTextComponents()
                .build();
    }

    public static ComponentRegistry createFull() {
        return new ComponentRegistryBuilder()
                .registerStandardContainers()
                .registerTextComponents()
                .build();
    }
}