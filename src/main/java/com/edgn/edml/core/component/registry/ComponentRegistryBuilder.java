package com.edgn.edml.core.component.registry;

import com.edgn.edml.core.component.ComponentFactory;
import com.edgn.edml.core.component.EdmlComponent;
import com.edgn.edml.dom.EdmlEnum;
import com.edgn.edml.dom.components.containers.*;
import com.edgn.edml.dom.components.elements.ButtonComponent;
import com.edgn.edml.dom.components.elements.ImageComponent;
import com.edgn.edml.dom.components.elements.TextComponent;
import com.edgn.edml.ui.scroll.ScrollbarComponent;
import com.edgn.edml.ui.virtual.VirtualListComponent;

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
                SectionComponent::new,
                AsideComponent::new,
                ArticleComponent::new
        );
    }

    public ComponentRegistryBuilder registerStandardElements() {
        return registerComponents(
                ImageComponent::new,
                TextComponent::new,
                ButtonComponent::new
        );
    }

    public ComponentRegistryBuilder registerUIComponents() {
        return registerComponents(
                ScrollbarComponent::new,
                VirtualListComponent::new
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
                .registerStandardElements()
                .build();
    }

    public static ComponentRegistry createDefault() {
        return new ComponentRegistryBuilder()
                .registerStandardContainers()
                .registerStandardElements()
                .registerUIComponents()
                .registerTextComponents()
                .build();
    }

    public static ComponentRegistry createFull() {
        return new ComponentRegistryBuilder()
                .registerStandardContainers()
                .registerStandardElements()
                .registerUIComponents()
                .registerTextComponents()
                .build();
    }
}